//  The MIT License (MIT)

//  Copyright (c) 2018 Intuz Solutions Pvt Ltd.

//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
//  (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify,
//  merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:

//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
//  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
//  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.silversnowsoftware.vc.model.customvideoviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.LongSparseArray;
import android.view.View;


import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.ui.editor.EditorViewHolder;
import com.silversnowsoftware.vc.utils.helpers.FileHelper;

import java.util.HashMap;
import java.util.Map;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class TileView extends View {

    private Uri mVideoUri;
    private int mHeightView;
    private LongSparseArray<Bitmap> mBitmapList = null;
    private int viewWidth = 0;
    private int viewHeight = 0;
    private EditorViewHolder viewHolder;

    public TileView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TileView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHeightView = getContext().getResources().getDimensionPixelOffset(R.dimen.frames_video_height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int minW = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minW, widthMeasureSpec, 1);

        final int minH = getPaddingBottom() + getPaddingTop() + mHeightView;
        int h = resolveSizeAndState(minH, heightMeasureSpec, 1);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(final int w, int h, final int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        viewWidth = w;
        viewHeight = h;
        if (w != oldW) {
            if (mVideoUri != null)
                getBitmap();
        }
    }

    private void getBitmap() {
        BackgroundTask
                .execute(new BackgroundTask.Task("", 0L, "") {
                             @Override
                             public void execute() {
                                 try {

                                     MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                                     mediaMetadataRetriever.setDataSource(mVideoUri.getPath());


                                     LongSparseArray<Bitmap> thumbnailList = new LongSparseArray<>();


                                     // Retrieve media data
                                     long videoLengthInMs = Integer.parseInt(mediaMetadataRetriever.extractMetadata(mediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000;

                                     // Set thumbnail properties (Thumbs are squares)
                                     final int thumbWidth = mHeightView;
                                     final int thumbHeight = mHeightView;

                                     int numThumbs = (int) Math.ceil(((float) viewWidth) / thumbWidth);

                                     final long interval = videoLengthInMs / numThumbs;

                                     for (int i = 0; i < numThumbs; ++i) {
                                         long frame = i * interval;

                                         Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(frame, FFmpegMediaMetadataRetriever.OPTION_CLOSEST); // frame at 2 seconds

                                         try {
                                             bitmap = Bitmap.createScaledBitmap(bitmap, thumbWidth, thumbHeight, false);
                                         } catch (Exception e) {
                                             e.printStackTrace();
                                         }
                                         thumbnailList.put(i, bitmap);
                                     }

                                     mediaMetadataRetriever.release();
                                     returnBitmaps(thumbnailList);
                                 } catch (final Throwable e) {
                                     Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                                 }
                             }
                         }
                );
    }

    private void returnBitmaps(final LongSparseArray<Bitmap> thumbnailList) {
        new MainThreadExecutor().runTask("", new Runnable() {
                    @Override
                    public void run() {
                        mBitmapList = thumbnailList;
                        invalidate();
                    }
                }
                , 0L);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmapList != null) {
            canvas.save();
            int x = 0;

            for (int i = 0; i < mBitmapList.size(); i++) {
                Bitmap bitmap = mBitmapList.get(i);
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, x, 0, null);
                    x = x + bitmap.getWidth();
                }
            }
            if (this.viewHolder != null) {
                this.viewHolder.tvPreviewThumb.setVisibility(GONE);
                this.viewHolder.mCustomRangeSeekBarNew.setVisibility(VISIBLE);
                this.viewHolder.btnCompress.setEnabled(true);
                this.viewHolder.btnClear.setEnabled(true);
                this.viewHolder.btnCompress.setAlpha((float) 1.0);
                this.viewHolder.btnClear.setAlpha((float) 1.0);
            }

        }
    }

    public void setVideo(@NonNull Uri data, EditorViewHolder viewHolder) {
        mVideoUri = data;
        this.viewHolder = viewHolder;
        getBitmap();
    }

    public final class MainThreadExecutor {

        private final Handler HANDLER = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Runnable callback = msg.getCallback();
                if (callback != null) {
                    callback.run();
                    decrementToken((Token) msg.obj);
                } else {
                    super.handleMessage(msg);
                }
            }
        };

        private final Map<String, Token> TOKENS = new HashMap<>();

        private MainThreadExecutor() {
            // should not be instantiated
        }

        /**
         * Store a new task in the map for providing cancellation. This method is
         * used by AndroidAnnotations and not intended to be called by clients.
         *
         * @param id    the identifier of the task
         * @param task  the task itself
         * @param delay the delay or zero to run immediately
         */
        public void runTask(String id, Runnable task, long delay) {
            if ("".equals(id)) {
                HANDLER.postDelayed(task, delay);
                return;
            }
            long time = SystemClock.uptimeMillis() + delay;
            HANDLER.postAtTime(task, nextToken(id), time);
        }

        private Token nextToken(String id) {
            synchronized (TOKENS) {
                Token token = TOKENS.get(id);
                if (token == null) {
                    token = new MainThreadExecutor.Token(id);
                    TOKENS.put(id, token);
                }
                token.runnablesCount++;
                return token;
            }
        }

        private void decrementToken(Token token) {
            synchronized (TOKENS) {
                if (--token.runnablesCount == 0) {
                    String id = token.id;
                    Token old = TOKENS.remove(id);
                    if (old != token) {
                        // a runnable finished after cancelling, we just removed a
                        // wrong token, lets put it back
                        TOKENS.put(id, old);
                    }
                }
            }
        }

        /**
         * Cancel all tasks having the specified <code>id</code>.
         *
         * @param id the cancellation identifier
         */
        public void cancelAll(String id) {
            Token token;
            synchronized (TOKENS) {
                token = TOKENS.remove(id);
            }
            if (token == null) {
                // nothing to cancel
                return;
            }
            HANDLER.removeCallbacksAndMessages(token);
        }

        private final class Token {
            int runnablesCount = 0;
            final String id;

            private Token(String id) {
                this.id = id;
            }
        }

    }

}

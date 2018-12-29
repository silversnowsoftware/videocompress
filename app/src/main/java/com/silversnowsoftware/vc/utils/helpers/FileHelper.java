package com.silversnowsoftware.vc.utils.helpers;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import com.silversnowsoftware.vc.R;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.ui.main.MainActivity;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.silversnowsoftware.vc.utils.constants.Arrays.MIME_MapTable;

/**
 * Created by burak on 10/15/2018.
 */

public class FileHelper {
    private static final String className = FileHelper.class.getSimpleName();

    public static String generateVideoName() {
        String filename = null;
        String dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_TWO).format(new Date());
        filename = "V-" + dateFormat + ".mp4";
        return filename;
    }
    public static String originalGenerateVideoName() {
        String filename = null;
        String dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_TWO).format(new Date());
        filename = "O-" + dateFormat + ".mp4";
        return filename;
    }

    public static String getMIMEType(File file) {

        String type = "*/*";
        try {
            String fName = file.getName();

            int dotIndex = fName.lastIndexOf(".");
            if (dotIndex < 0) {
                return type;
            }

            String end = fName.substring(dotIndex, fName.length()).toLowerCase();
            if (end == "") return type;

            for (int i = 0; i < MIME_MapTable.length; i++) {
                if (end.equals(MIME_MapTable[i][0]))
                    type = MIME_MapTable[i][1];
            }
        } catch (Exception ex) {
            LogManager.Log(className, ex);
        } finally {
            return type;
        }

    }

    public static String getFileSize(String path) {
        String size2 = "";
        try {
            File f = new File(path);
            if (!f.exists()) {
                size2 = "0 MB";
                return size2;
            } else {
                long size = f.length();
                size2 = (size / 1024f) / 1024f + "MB";
                return size2;
            }
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        } finally {
            return size2;
        }
    }

    public static Intent openFile(File file) throws Exception {
        Intent intent = null;

        try {
            intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.setAction(Intent.ACTION_VIEW);

            String type = FileHelper.getMIMEType(file);

            intent.setDataAndType(/*uri*/Uri.fromFile(file), type);

        } catch (Exception ex) {
            intent = null;

            LogManager.Log(className, ex);
        }
        return intent;
    }


    public static ArrayList<String> GetAllPath(final Context context, Intent data) {


        ArrayList<String> Paths = new ArrayList<>();
        try {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Paths.add(
                            getRealPath(context, data.getClipData().getItemAt(i).getUri())
                    );
                }
            }

            if (data.getData() != null) {
                Paths.add(
                        getRealPath(context, data.getData())
                );
            }
        } catch (Exception ex) {


            LogManager.Log(className, ex);
        } finally {
            return Paths;
        }

    }

    public static String getRealPath(final Context context, final Uri uri) {

        try {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

        } catch (Exception ex) {


            LogManager.Log(className, ex);
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception ex) {


            LogManager.Log(className, ex);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getBinaryOfImage(String filePath) {

        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try {
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }
        return encodeString;
    }

    public static String getBinaryFromFile(String filePath) {
        File file = new File(filePath);
        byte[] fileData = new byte[(int) file.length()];
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            dis.readFully(fileData);

            dis.close();
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }
        return Base64.encodeToString(fileData, Base64.DEFAULT);
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
           /* if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());*/
         /*   else*/
            mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(25000000, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception ex) {

            LogManager.Log(className, ex);
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + ex.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public static Bitmap getBitmapFromBase64(String encodedImage) {

        try {
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;

        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }
        return null;
    }

    public static String getBase64FromBitmap(Bitmap bitmap) {
        String encoded = "";
        try {


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        } finally {
            return encoded;
        }

    }

    public static String getFileNameFromPath(String path) {
        String filename = "";
        try {
            filename = path.substring(path.lastIndexOf("/") + 1);
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        } finally {
            return filename;
        }

    }

    public static int getVideoDuration(Activity activity, String path) {
        int duration = 0;
        try {
            if (activity != null) {
                MediaPlayer mp = MediaPlayer.create(activity, Uri.parse(path));
                duration = mp.getDuration() / 1000;
            }
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        } finally {
            return duration;
        }

    }


    public static Map<String, Integer> getVideoResoution(String path) {
        Map<String, Integer> values = new HashMap<String, Integer>();
        try {

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            values.put("width", width);
            values.put("height", height);
            retriever.release();
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        } finally {
            return values;
        }

    }

    private static void bugFixApi23ThanForShowMedia() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception ex) {

                LogManager.Log(className, ex);
            }
        }
    }

    public static void startVideoActivity(Context context, String path) {
        try {
            bugFixApi23ThanForShowMedia();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent = intent.setDataAndType(Uri.parse(path), "video/*");
            context.startActivity(intent);
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }
    }

    public static void deleteFile(String path){
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

}
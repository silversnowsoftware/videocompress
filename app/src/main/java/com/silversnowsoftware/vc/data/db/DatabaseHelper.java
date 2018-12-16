package com.silversnowsoftware.vc.data.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.helpers.LogHelper;

/**
 * Created by burak on 10/21/2018.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String className = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "VideoCompress.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<FileModel, Integer> mFileModel = null;
    private RuntimeExceptionDao<FileModel, Integer> mFileModelRuntimeExceptionDao = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,FileModel.class);
        } catch (SQLException | java.sql.SQLException e) {
            LogModel logModel = new LogModel.LogBuilder()
                    .apiVersion(Utility.getAndroidVersion())
                    .appName(Constants.APP_NAME)
                    .className(className)
                    .errorMessage(e.getMessage())
                    .methodName(e.getStackTrace()[0].getMethodName())
                    .stackTrace(e.getStackTrace().toString())
                    .build();
            LogHelper logHelper = new LogHelper();
            logHelper.Log(logModel);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,FileModel.class,true);
            onCreate(database,connectionSource);
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<FileModel, Integer> getFileModel() throws SQLException, java.sql.SQLException {
        if(mFileModel==null)
        {
            mFileModel=getDao(FileModel.class);
        }
        return mFileModel;
    }


    public RuntimeExceptionDao<FileModel, Integer> getFileModelRuntimeExceptionDao() {
        if(mFileModelRuntimeExceptionDao==null)
        {
            mFileModelRuntimeExceptionDao=getRuntimeExceptionDao(FileModel.class);
        }
        return mFileModelRuntimeExceptionDao;
    }


    @Override
    public void close() {
        mFileModel = null;

        super.close();
    }


}

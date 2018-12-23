package com.silversnowsoftware.vc.data.db;

import android.content.Context;

import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.model.logger.LogModel;
import com.silversnowsoftware.vc.utils.Utility;
import com.silversnowsoftware.vc.utils.constants.Constants;
import com.silversnowsoftware.vc.utils.enums.FileStatusEnum;
import com.silversnowsoftware.vc.utils.helpers.LogManager;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by burak on 10/22/2018.
 */

public class DbFileModel extends DbBaseModel implements IRepository<FileModel> {

    private static final String className = DbFileModel.class.getSimpleName();

    public DbFileModel(Context context) {
        super(context);
    }

    @Override
    public void add(FileModel item) {

        try {

            db.getFileModel().createOrUpdate(item);
        } catch (SQLException ex) {


            LogManager.Log(className, ex);
        }

    }

    @Override
    public void add(Iterable<FileModel> items) {
        for (FileModel item : items) {

            try {
                db.getFileModel().createOrUpdate(item);
            } catch (SQLException ex) {

                LogManager.Log(className, ex);
            }
        }
    }

    @Override
    public void update(FileModel item) {
        try {
            db.getFileModel().update(item);
        } catch (SQLException ex) {


            LogManager.Log(className, ex);
        }
    }

    @Override
    public void remove(FileModel item) {
        try {
            db.getFileModel().delete(item);
        } catch (SQLException ex) {

            LogManager.Log(className, ex);
        }
    }

    @Override
    public void removeAll() {
        try {
            List<FileModel> list = getAll();
            db.getFileModel().delete(list);
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }
    }

    @Override
    public int removeIds(List<Integer> ids) {
        int result = 0;
        try {
            result = db.getFileModel().deleteIds(ids);
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }
        return result;
    }

    @Override
    public boolean removeItems(List<FileModel> items) {
        boolean result = true;
        try {
            db.getFileModel().delete(items);
        } catch (Exception ex) {
            result = false;

            LogManager.Log(className, ex);
        }
        return result;
    }

    @Override
    public FileModel getById(Integer id) {
        try {
            return db.getFileModel().queryForId(id);
        } catch (SQLException ex) {

            LogManager.Log(className, ex);
        }
        return null;
    }

    @Override
    public List<FileModel> getAll() {
        List<FileModel> fileModelsList = null;
        try {
            fileModelsList = db.getFileModel().queryForAll();
            return fileModelsList;
        } catch (SQLException ex) {

            LogManager.Log(className, ex);
        }
        return fileModelsList;
    }

    @Override
    public FileModel getByObject(FileModel item) {
        FileModel file = null;
        try {
            file = db.getFileModel().queryForSameId(item);
        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }
        return file;
    }

    @Override
    public boolean exist(Integer id) {
        boolean b = false;
        try {
            b = db.getFileModel().idExists(id);

        } catch (Exception ex) {

            LogManager.Log(className, ex);
        }
        return b;
    }

    public List<FileModel> getFileModelListWithFileStatus(String field, FileStatusEnum value) {
        List<FileModel> fileModels = null;
        try {
            fileModels = db.getFileModel().queryForEq(field, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fileModels;
    }
}

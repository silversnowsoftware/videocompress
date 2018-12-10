package com.silversnowsoftware.vc.data.db;

import android.content.Context;

import com.silversnowsoftware.vc.model.FileModel;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by burak on 10/22/2018.
 */

public class DbFileModel extends DbBaseModel implements IRepository<FileModel> {

    public DbFileModel(Context context) {
        super(context);
    }

    @Override
    public void add(FileModel item) {

        try {
            db.getFileModel().createOrUpdate(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void add(Iterable<FileModel> items) {
        for (FileModel item : items) {

            try {
                db.getFileModel().createOrUpdate(item);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(FileModel item) {
        try {
            db.getFileModel().update(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(FileModel item) {
        try {
            db.getFileModel().delete(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAll() {
        try {
            List<FileModel> list = getAll();
            db.getFileModel().delete(list);
        } catch (Exception ex) {

        }
    }

    @Override
    public int removeIds(List<Integer> ids) {
        int result = 0;
        try {
            result = db.getFileModel().deleteIds(ids);
        } catch (Exception ex) {

        }
        return result;
    }

    @Override
    public FileModel getById(Integer id) {
        try {
            return db.getFileModel().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FileModel> getAll() {
        try {
            return db.getFileModel().queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FileModel getByObject(FileModel item) {
        FileModel file = null;
        try {
            file = db.getFileModel().queryForSameId(item);
        } catch (Exception ex) {

        }
        return file;
    }

    @Override
    public boolean exist(Integer id) {
        boolean b = false;
        try {
            b = db.getFileModel().idExists(id);

        } catch (Exception ex) {
        }
        return b;
    }
}

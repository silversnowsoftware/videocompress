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
    public FileModel getbyId(Integer id) {
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
}

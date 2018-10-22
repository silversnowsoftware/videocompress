package com.silversnowsoftware.vc.data.db;

import android.content.Context;

/**
 * Created by burak on 10/22/2018.
 */

public class DbBaseModel {
    protected DatabaseHelper db;

    DbBaseModel(Context context) {
        this.db = new DatabaseHelper(context);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (db != null && db.isOpen())
            db.close();
    }
}

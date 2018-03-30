package com.jhobor.ddc.greendao;

import android.content.Context;

/**
 * Created by Administrator on 2017/3/7.
 * 数据库业务类，这里可以获得不同实例的dao
 */

public class DBService {
    private static final String DB_NAME = "jhoborDDC.db";
    private static DaoSession daoSession;
    private static DBService dbService;

    private static void init(Context context, boolean isDev) {
        if (isDev) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME);
            DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
            daoSession = daoMaster.newSession();
        } else {
            MigrationHelper helper = new MigrationHelper(context, DB_NAME);
            DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
            daoSession = daoMaster.newSession();
        }
    }

    public static DBService getInstance(Context context) {
        if (dbService == null) {
            dbService = new DBService();
            init(context, false);
        }
        return dbService;
    }

    public ShopCarDao getShopCarDao() {
        return daoSession.getShopCarDao();
    }
}

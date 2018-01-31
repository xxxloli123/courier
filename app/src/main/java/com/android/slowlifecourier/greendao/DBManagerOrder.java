package com.android.slowlifecourier.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.android.slowlifecourier.greendao.gen.DaoMaster;
import com.android.slowlifecourier.greendao.gen.DaoSession;
import com.android.slowlifecourier.greendao.gen.OrderEntityDao;
import com.android.slowlifecourier.greendao.gen.SpecialOrderDao;
import com.android.slowlifecourier.objectmodle.OrderEntity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */

public class DBManagerOrder {
    private final static String dbName = "test_db";
    private static DBManagerOrder mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManagerOrder(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManagerOrder getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManagerOrder.class) {
                if (mInstance == null) {
                    mInstance = new DBManagerOrder(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 插入用户集合
     *
     * @param
     */
    public void insertOrderList(List<OrderEntity> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        OrderEntityDao shopDao = daoSession.getOrderEntityDao();
        shopDao.insertInTx(accounts);
    }
    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }
    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }
    /**
     * 插入一条User记录
     *
     * @param shop
     */
    public void insertSpecialOrder(OrderEntity shop) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        OrderEntityDao shopDao = daoSession.getOrderEntityDao();
        shopDao.insert(shop);
    }

    /**
     * 删除一条记录
     *
     * @param
     */
    public void deleteSpecialOrder(OrderEntity shop) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        OrderEntityDao shopDao = daoSession.getOrderEntityDao();
        shopDao.delete(shop);
    }

    /**
     * 查询用户列表
     */
    public List<OrderEntity> queryList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        OrderEntityDao shopDao = daoSession.getOrderEntityDao();
        QueryBuilder<OrderEntity> qb = shopDao.queryBuilder();
        List<OrderEntity> list = qb.list();
        return list;
    }
//    /**
//     * 查询LoginIfio列表
//     */
//    public List<LoginIfio> queryLoginIfioList() {
//        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        LoginIfioDao loginIfioDao = daoSession.getLoginIfioDao();
//        QueryBuilder<LoginIfio> qb = loginIfioDao.queryBuilder();
//        List<LoginIfio> list = qb.list();
//        return list;
//    }

//    /**
//     * 插入一条LoginIfio记录
//     *
//     * @param loginIfio
//     */
//    public void insertLoginIfio(LoginIfio loginIfio) {
//        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        LoginIfioDao loginIfioDao = daoSession.getLoginIfioDao();
//        loginIfioDao.insert(loginIfio);
//    }
//    /**
//     * 更新一条记录
//     *
//     * @param loginIfio
//     */
//    public void updateLoginIfio(LoginIfio loginIfio) {
//        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        LoginIfioDao loginIfioDao = daoSession.getLoginIfioDao();
//        loginIfioDao.update(loginIfio);
//    }
}
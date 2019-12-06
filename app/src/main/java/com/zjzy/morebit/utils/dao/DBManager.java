package com.zjzy.morebit.utils.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.anye.greendao.gen.DaoMaster;
import com.anye.greendao.gen.DaoSession;
import com.anye.greendao.gen.GoodsBrowsHistoryDao;
import com.anye.greendao.gen.GoodsImgHistoryDao;
import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.pojo.GoodsBrowsHistory;
import com.zjzy.morebit.pojo.grenndao.GoodsImgHistory;

import java.util.List;

/**
 * Created by YangBoTian on 2018/11/26.
 * 数据库操作类
 */

public class DBManager {
    private final static String dbName = "morebit";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param
     * @return
     */
    public static DBManager getInstance() {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(App.getAppContext());
                }
            }
        }
        return mInstance;
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
     * 返回已存储的记录
     *
     * @return
     */
    public long getCount() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsImgHistoryDao GoodsImgHistoryDao = daoSession.getGoodsImgHistoryDao();
        return GoodsImgHistoryDao.count();
    }

    /**
     * 插入一条记录商品记录
     *
     * @param goodsHistory 浏览的商品
     */
    public void insertGoodsHistory(GoodsImgHistory goodsHistory) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsImgHistoryDao GoodsImgHistoryDao = daoSession.getGoodsImgHistoryDao();
        GoodsImgHistoryDao.insert(goodsHistory);
    }

    /**
     * 更新一条记录商品记录
     *
     * @param goodsHistory 浏览的商品
     */
    public void updateGoodsHistory(GoodsImgHistory goodsHistory) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsImgHistoryDao goodsHistoryDao = daoSession.getGoodsImgHistoryDao();
        long count = goodsHistoryDao.queryBuilder().where(GoodsImgHistoryDao.Properties.ItemSourceId.eq(goodsHistory.getItemSourceId())).count();
        if (count != 0) {
            goodsHistoryDao.update(goodsHistory);
        } else {
            goodsHistoryDao.insert(goodsHistory);
        }
    }


    /**
     * 删除全部商品浏览记录
     */
    public void deleteGoodsHistoryList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        daoSession.deleteAll(GoodsImgHistory.class);
    }

    /**
     * 超过 2条
     */
    public List<GoodsImgHistory> getAllGoodsHistoryList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsImgHistoryDao goodsHistoryDao = daoSession.getGoodsImgHistoryDao();
        List<GoodsImgHistory> goodsHistoryList = goodsHistoryDao.queryBuilder().list();
        return goodsHistoryList;
    }

    /**
     * 超过当前时间30分钟
     */
    public List<GoodsImgHistory> getPastDueGoods(long time) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsImgHistoryDao goodsHistoryDao = daoSession.getGoodsImgHistoryDao();
        List<GoodsImgHistory> goodsHistoryList = goodsHistoryDao.queryBuilder()
                .where(GoodsImgHistoryDao.Properties.Time.le(time)).list();
        return goodsHistoryList;
    }



    /**
     * 返回已存储的记录
     * @return
     */
    public long getGoodsHistoryCount(){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsBrowsHistoryDao goodsHistoryDao = daoSession.getGoodsBrowsHistoryDao();
        String phone = UserLocalData.getUser(App.getAppContext()).getPhone();
        long count = goodsHistoryDao.queryBuilder().where(GoodsBrowsHistoryDao.Properties.Phone.eq(phone)).count();
        return count;
    }
    /**
     * 插入一条记录商品记录
     *
     * @param goodsHistory  浏览的商品
     */
    public void insertGoodsHistory(GoodsBrowsHistory goodsHistory) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsBrowsHistoryDao goodsHistoryDao = daoSession.getGoodsBrowsHistoryDao();
        goodsHistoryDao.insert(goodsHistory);
    }
    /**
     * 更新一条记录商品记录
     *
     * @param goodsHistory  浏览的商品
     */
    public void updateGoodsHistory(GoodsBrowsHistory goodsHistory) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsBrowsHistoryDao userDao = daoSession.getGoodsBrowsHistoryDao();
        userDao.update(goodsHistory);
    }
    /**
     * 根据时间查询商品浏览记录列表
     */
    public List<GoodsBrowsHistory> queryGoodsHistoryListByTime(int offset) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsBrowsHistoryDao goodsHistoryDao = daoSession.getGoodsBrowsHistoryDao();
        String phone = UserLocalData.getUser(App.getAppContext()).getPhone();
        List<GoodsBrowsHistory> list = goodsHistoryDao.queryBuilder()
                .where(GoodsBrowsHistoryDao.Properties.Phone.eq(phone))
                .offset(offset *10).limit(10).orderDesc(GoodsBrowsHistoryDao.Properties.Browse_time).list();
        return list;
    }

    /**
     * 查询商品浏览记录列表
     */
    public List<GoodsBrowsHistory> queryGoodsHistoryList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsBrowsHistoryDao goodsHistoryDao = daoSession.getGoodsBrowsHistoryDao();
        List<GoodsBrowsHistory> list = goodsHistoryDao.queryBuilder().list();
        return list;
    }
    /**
     * 根据淘宝ID查询某一条商品浏览记录列表
     */
    public GoodsBrowsHistory queryGoodsHistory(String taobao_id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsBrowsHistoryDao goodsHistoryDao = daoSession.getGoodsBrowsHistoryDao();
        GoodsBrowsHistory goodsHistory = goodsHistoryDao.queryBuilder().where(GoodsBrowsHistoryDao.Properties.ItemSourceId.eq(taobao_id)).build().unique();
        return goodsHistory;
    }
    /**
     * 查询最后一条商品浏览记录
     */
    public GoodsBrowsHistory queryLastGoodsHistory() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsBrowsHistoryDao goodsHistoryDao = daoSession.getGoodsBrowsHistoryDao();
        String phone = UserLocalData.getUser(App.getAppContext()).getPhone();
        List<GoodsBrowsHistory> goodsHistory = goodsHistoryDao.queryBuilder()
                .where(GoodsBrowsHistoryDao.Properties.Phone.eq(phone))
                .orderAsc(GoodsBrowsHistoryDao.Properties.Browse_time).list();
        return goodsHistory.get(0);
    }
    /**
     * 删除某一条商品浏览记录
     */
    public void deleteGoodsHistory(GoodsBrowsHistory goodsHistory) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsBrowsHistoryDao goodsHistoryDao = daoSession.getGoodsBrowsHistoryDao();

        goodsHistoryDao.delete(goodsHistory);
    }
    /**
     * 删除多条商品浏览记录
     */
    public void deleteGoodsHistoryList(List<GoodsBrowsHistory> list) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsBrowsHistoryDao goodsHistoryDao = daoSession.getGoodsBrowsHistoryDao();
        goodsHistoryDao.deleteInTx(list);
    }

    /**
     * 清空多条商品浏览记录
     */
    public void cleanGoodsHistoryList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GoodsBrowsHistoryDao goodsHistoryDao = daoSession.getGoodsBrowsHistoryDao();
        goodsHistoryDao.deleteAll();
    }
}

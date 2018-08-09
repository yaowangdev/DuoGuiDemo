package com.appdev.framework.db.liteorm.db.assit;


import com.appdev.framework.db.liteorm.log.OrmLog;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * 辅助事务
 *
 * @author mty
 * @date 2013-6-15下午11:09:15
 */
public final class Transaction {
    private static final String TAG = Transaction.class.getSimpleName();

    private Transaction() {
    }

    /**
     * 因为每个具体事物都不一样，但又有相同的结构，既要维持代码的统一性，也要可以个性化解析。
     */
    public static <T> T execute(SQLiteDatabase db, Worker<T> worker) {
        db.beginTransaction();
        OrmLog.i(TAG, "----> BeginTransaction");
        T data = null;
        try {
            data = worker.doTransaction(db);
            db.setTransactionSuccessful();
            if (OrmLog.isPrint)
                OrmLog.i(TAG, "----> Transaction Successful");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return data;
    }

    public interface Worker<T> {
        T doTransaction(SQLiteDatabase db) throws Exception;
    }

}

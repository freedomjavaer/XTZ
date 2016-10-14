package com.ypwl.xiaotouzi.manager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.ypwl.xiaotouzi.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * function : 收藏数据库帮助类.
 * <p/>
 * Modify by lzj on 2015/11/6.
 */
public class CollectDbOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "CollectDbOpenHelper";
    /** 数据库名 */
    public static final String DBName = "collect.db";
    /** 数据库版本 */
    public static final int DBVersion = 1;
    /** 收藏 表 */
    public static final String TABLE_COLLECT_INFO = "collect_info";

    public static final class CollectDbHelper {
        protected volatile static CollectDbHelper instance = null;

        public static CollectDbHelper getInstance(Context context) {
            if (null == instance) {
                synchronized (CollectDbHelper.class) {
                    if (null == instance) {
                        instance = new CollectDbHelper(context);
                    }
                }
            }
            return instance;
        }

        protected Context mContext;
        /** 数据库操作 */
        protected CollectDbOpenHelper mDbOpenHelper = null;
        /** 数据库操作对象 */
        protected SQLiteDatabase mDatabase = null;

        public CollectDbHelper(Context context) {
            this.mContext = context;
            mDbOpenHelper = new CollectDbOpenHelper(context, DBName, null, DBVersion);
            mDatabase = mDbOpenHelper.getWritableDatabase();
        }

        public SQLiteDatabase getSQLiteDatabase() {
            if (null == mDatabase) {
                mDatabase = mDbOpenHelper.getWritableDatabase();
            }
            return mDatabase;
        }

        /** 插入一条记录 */
        public long insert(String pid) {
            final SQLiteDatabase database = getSQLiteDatabase();
            ContentValues values = new ContentValues();
            values.put(CollectColumns.BID_PID, pid);
            long row = database.insert(TABLE_COLLECT_INFO, null, values);
            return row;
        }

        /** 查询所有 */
        public List<String> queryAll() {
            List<String> result = new ArrayList<>();
            Cursor cursor = null;
            try {
                final SQLiteDatabase database = getSQLiteDatabase();
                cursor = database.query(TABLE_COLLECT_INFO, null, null, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index_of_url = cursor.getColumnIndex(CollectColumns.BID_PID);
                    do {
                        result.add(cursor.getString(index_of_url));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                LogUtil.e(TAG, TAG + "------->Exception : " + e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return result;
        }

        /** 根据pid查询该详情是否为收藏详情 true-标记为喜欢的，false-未标记 */
        public boolean queryByPid(String pid) {
            Cursor cursor = null;
            try {
                final SQLiteDatabase database = getSQLiteDatabase();
                cursor = database.query(TABLE_COLLECT_INFO, null, CollectColumns.BID_PID + "=?", new String[]{pid}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    return true;
                }
            } catch (Exception e) {
                LogUtil.e(TAG, TAG + "------->Exception : " + e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return false;
        }

        /** 根据pid移除收藏的详情 */
        public boolean deleteByPid(String pid) {
            final SQLiteDatabase database = getSQLiteDatabase();
            long row = database.delete(TABLE_COLLECT_INFO, CollectColumns.BID_PID + "=?", new String[]{pid});
            return row > 0;
        }

    }

    public static final class CollectColumns implements BaseColumns {
        public static final String BID_PID = "pid";
    }

    public CollectDbOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("CREATE TABLE IF NOT EXISTS [").append(TABLE_COLLECT_INFO).append("] ([");
        sb.append(CollectColumns._ID).append("] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,[");
        sb.append(CollectColumns.BID_PID).append("]VARCHAR);");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {//强制升级数据库表
            this.onCreate(db);
        }
    }
}

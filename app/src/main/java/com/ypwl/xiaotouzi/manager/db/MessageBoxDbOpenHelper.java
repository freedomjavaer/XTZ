package com.ypwl.xiaotouzi.manager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;


/**
 * function : 收件箱系统消息数据库帮助类.
 * <p/>
 * Created by lzj on 2016/4/5.
 */
public class MessageBoxDbOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = MessageBoxDbOpenHelper.class.getSimpleName();

    /** 数据库名 */
    public static final String DBName = "message_box.db";
    /** 数据库版本 */
    public static final int DBVersion = 1;
    /** 表 */
    public static final String TABLE_MESSAGE_PUSH = "message_push";

    public static final class MessageBoxDbHelper {
        protected volatile static MessageBoxDbHelper instance = null;

        public static MessageBoxDbHelper getInstance() {
            if (null == instance) {
                synchronized (MessageBoxDbHelper.class) {
                    if (null == instance) {
                        instance = new MessageBoxDbHelper();
                    }
                }
            }
            return instance;
        }

        /** 数据库操作 */
        protected MessageBoxDbOpenHelper mDbOpenHelper = null;
        /** 数据库操作对象 */
        protected SQLiteDatabase mDatabase = null;

        public MessageBoxDbHelper() {
            mDbOpenHelper = new MessageBoxDbOpenHelper(UIUtil.getContext(), DBName, null, DBVersion);
            mDatabase = mDbOpenHelper.getWritableDatabase();
        }

        public SQLiteDatabase getSQLiteDatabase() {
            if (null == mDatabase) {
                mDatabase = mDbOpenHelper.getWritableDatabase();
            }
            return mDatabase;
        }

        /** 插入一条记录 */
        public long insert(String stand_id) {
            final SQLiteDatabase database = getSQLiteDatabase();
            ContentValues values = new ContentValues();
            values.put(CollectColumns.STAND_ID, stand_id);
            return database.insert(TABLE_MESSAGE_PUSH, null, values);
        }

        public boolean queryByStandId(String stand_id) {
            Cursor cursor = null;
            try {
                final SQLiteDatabase database = getSQLiteDatabase();
                cursor = database.query(TABLE_MESSAGE_PUSH, null, CollectColumns.STAND_ID + "=?", new String[]{stand_id}, null, null, null);
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
    }

    public static final class CollectColumns implements BaseColumns {
        public static final String STAND_ID = "stand_id";
    }

    public MessageBoxDbOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @SuppressWarnings({"StringBufferReplaceableByString", "StringBufferMayBeStringBuilder"})
    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("CREATE TABLE IF NOT EXISTS [").append(TABLE_MESSAGE_PUSH).append("] ([");
        sb.append(CollectColumns._ID).append("] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,[");
        sb.append(CollectColumns.STAND_ID).append("]VARCHAR);");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {//强制升级数据库表
            this.onCreate(db);
        }
    }
}

package com.ypwl.xiaotouzi.manager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.ypwl.xiaotouzi.bean.PlatformChooseAdapterBean;
import com.ypwl.xiaotouzi.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * function:平台选择-->历史记录的数据库管理类
 * <p/>
 * Created by tengtao on 2015/11/28.
 */
public class ContrastHistoryDbOpenHelper extends SQLiteOpenHelper{
    private static final String TAG = "ContrastHistoryDbOpenHelper";
    /** 数据库名 */
    public static final String DBName = "history_platform_contrast.db";
    /** 数据库版本 */
    public static final int DBVersion = 1;
    /** 收藏 表 */
    public static final String TABLE_HISTORY_INFO = "contrast_history_info";

    public static final class HistoryDbHelper {
        protected volatile static HistoryDbHelper instance = null;

        public static HistoryDbHelper getInstance(Context context) {
            if (null == instance) {
                synchronized (HistoryDbHelper.class) {
                    if (null == instance) {
                        instance = new HistoryDbHelper(context);
                    }
                }
            }
            return instance;
        }

        protected Context mContext;
        /** 数据库操作 */
        protected ContrastHistoryDbOpenHelper mDbOpenHelper = null;
        /** 数据库操作对象 */
        protected SQLiteDatabase mDatabase = null;

        public HistoryDbHelper(Context context) {
            this.mContext = context;
            mDbOpenHelper = new ContrastHistoryDbOpenHelper(context, DBName, null, DBVersion);
            mDatabase = mDbOpenHelper.getWritableDatabase();
        }

        public SQLiteDatabase getSQLiteDatabase() {
            if (null == mDatabase) {
                mDatabase = mDbOpenHelper.getWritableDatabase();
            }
            return mDatabase;
        }

        /** 插入一条记录 */
        public long insert(String pid,String p_name,String date) {
            final SQLiteDatabase database = getSQLiteDatabase();
            ContentValues values = new ContentValues();
            values.put(HistoryColumns.PID, pid);
            values.put(HistoryColumns.P_NAME,p_name);
            values.put(HistoryColumns.DATE,date);
            long row = database.insert(TABLE_HISTORY_INFO, null, values);
            return row;
        }

        /** 查询所有 */
        public List<PlatformChooseAdapterBean> queryAll() {
            List<PlatformChooseAdapterBean> result = new ArrayList<>();
            Cursor cursor = null;
            SQLiteDatabase database = getSQLiteDatabase();
            try {
                cursor = database.query(TABLE_HISTORY_INFO, null, null, null, null, null, null);
                while (cursor.moveToNext()){
                    PlatformChooseAdapterBean bean = new PlatformChooseAdapterBean();
                    bean.setPid(cursor.getString(cursor.getColumnIndex(HistoryColumns.PID)));
                    bean.setP_name(cursor.getString(cursor.getColumnIndex(HistoryColumns.P_NAME)));
                    bean.setDate(cursor.getString(cursor.getColumnIndex(HistoryColumns.DATE)));
                    bean.setDelete(true);//历史记录，带有删除按钮
                    result.add(0,bean);
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

        /** 根据pid查询该平台是否存在于历史记录数据库中 */
        public boolean queryByPid(String pid) {
            Cursor cursor = null;
            try {
                final SQLiteDatabase database = getSQLiteDatabase();
                String[] columns = new String[]{ HistoryColumns.P_NAME };
                cursor = database.query(TABLE_HISTORY_INFO, columns, HistoryColumns.PID + "=?", new String[]{pid}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    String p_name = cursor.getString(cursor.getColumnIndex(HistoryColumns.P_NAME));
                    return !(p_name.equals(""));
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

        /** 根据pid移除该平台数据 */
        public boolean deleteByPid(String pid) {
            final SQLiteDatabase database = getSQLiteDatabase();
            long row = database.delete(TABLE_HISTORY_INFO, HistoryColumns.PID + "=?", new String[]{pid});
            return row > 0;
        }

        /**清空表*/
        public void clearTable(String tabName){
            final SQLiteDatabase database = getSQLiteDatabase();
            String sql = "DELETE FROM "+ tabName;
            database.execSQL(sql);
        }

        /** 根据pid修改历史记录数据 */
        public void updateByPid(String pid,String p_name,String is_auto,String date)
        {
            SQLiteDatabase db = getSQLiteDatabase();
            String where = HistoryColumns.PID + " = ?";
            String[] whereValue = { pid };
            ContentValues cv = new ContentValues();
            cv.put(HistoryColumns.PID, pid);
            cv.put(HistoryColumns.P_NAME, p_name);
            cv.put(HistoryColumns.DATE,date);
            db.update(TABLE_HISTORY_INFO, cv, where, whereValue);
        }

    }

    public static final class HistoryColumns implements BaseColumns {
        public static final String PID = "pid";//平台id
        public static final String P_NAME = "p_name";//平台名称
        public static final String DATE = "date";//添加日期
    }

    public ContrastHistoryDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("CREATE TABLE IF NOT EXISTS [").append(TABLE_HISTORY_INFO).append("] ([");
        sb.append(HistoryColumns._ID).append("] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,[");
        sb.append(HistoryColumns.PID).append("] VARCHAR,[");
        sb.append(HistoryColumns.P_NAME).append("] VARCHAR,[");
        sb.append(HistoryColumns.DATE).append("] VARCHAR);");
        db.execSQL(sb.toString());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {//强制升级数据库表
            this.onCreate(db);
        }
    }
}

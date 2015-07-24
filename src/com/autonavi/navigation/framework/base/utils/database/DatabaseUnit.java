
package com.autonavi.navigation.framework.base.utils.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.FileNotFoundException;

/**
 * 数据库 操作 功能模块
 */
public class DatabaseUnit {
    private Context mContext = null;

    private String mDatabaseName = null;

    private SQLiteDatabase mSQLiteDatabase = null;

    private final String TAG = "DatabaseUnit";

    public DatabaseUnit(Context context) {
        mContext = context;
    }

    /**
     * 判断数据库是否打开
     * 
     * @return
     */
    public boolean isDataBaseOpen() {
        if (mSQLiteDatabase != null) {
            return mSQLiteDatabase.isOpen();
        }
        return false;
    }

    /**
     * 关闭数据库
     * 
     * @return
     */
    public boolean closeDatabase() {
        if (mSQLiteDatabase == null) {
            Log.e(TAG, "数据库--closeDatabase--未连接上");
            return false;
        }

        mSQLiteDatabase.close();
        return true;
    }

    /**
     * 创建表
     * 
     * @param tableName
     * @param columnName
     * @return
     */
    public boolean createTable(String tableName, String... columnName) {
        if (mSQLiteDatabase == null) {
            Log.e(TAG, "数据库--createTable--未连接上");
            return false;
        }

        if (columnName != null) {
            String tColName = columnName[0];
            for (int i = 1; i < columnName.length; i++) {
                tColName = tColName + "," + columnName[i];
            }
            try {
                String CREATE_TABLE = "create table if not exists " + tableName
                        + "(id integer primary key autoincrement," + tColName + ")";
                mSQLiteDatabase.execSQL(CREATE_TABLE);
            } catch (SQLException e) {
                Log.e(TAG, "表创建失败");
            }
        } else {
            Log.w(TAG, "表已经存在");
            return false;
        }

        return true;
    }

    /**
     * 删除数据
     * 
     * @param tableName
     * @param whereName
     * @param whereVal
     * @return
     */
    public boolean deleteData(String tableName, String whereName, int whereVal) {
        if (mSQLiteDatabase == null) {
            Log.e(TAG, "数据库--deleteData--未连接上");
            return false;
        }

        String DELETE_DATA = "delete from " + tableName + " where " + whereName + "=" + whereVal;
        mSQLiteDatabase.execSQL(DELETE_DATA);
        return true;
    }

    /**
     * 删除 数据库
     * 
     * @return
     */
    public boolean deleteDatabase() {
        if (mContext == null || mDatabaseName == null) {
            Log.e(TAG, "数据库--destroyDatabase--环境未配置");
            return false;
        }

        mContext.deleteDatabase(mDatabaseName);
        return true;
    }

    /**
     * 删除表
     * 
     * @param tableName
     * @return
     */
    public boolean dropTable(String tableName) {
        if (mSQLiteDatabase == null) {
            Log.e(TAG, "数据库--dropTable--未连接上");
            return false;
        }

        String DROP_TABLE = "drop table " + tableName;
        mSQLiteDatabase.execSQL(DROP_TABLE);
        return true;
    }

    /**
     * 通用SQL执行函数
     * 
     * @param sql
     * @return
     */
    public boolean execSql(String sql) {
        if (mSQLiteDatabase == null) {
            Log.e(TAG, "数据库--execSql--未连接上");
            return false;
        }

        mSQLiteDatabase.execSQL(sql);
        return true;
    }

    public SQLiteDatabase getDatabase() {
        return mSQLiteDatabase;
    }

    /**
     * 插入数据
     * 
     * @param tableName
     * @param columnName
     * @param columnVal
     * @return
     */
    public boolean insertData(String tableName, String columnName, String columnVal) {
        if (mSQLiteDatabase == null) {
            Log.e(TAG, "数据库--insertData--未连接上");
            return false;
        }

        String INSERT_DATA = "insert into " + tableName + "(" + columnName + ") values ("
                + columnVal + ")";
        mSQLiteDatabase.execSQL(INSERT_DATA);
        return true;
    }

    /**
     * 打开数据库(若不存在，则创建)
     * 
     * @param databaseName 数据库名称
     * @return
     * @throws FileNotFoundException
     */
    public SQLiteDatabase openDatabase(String databaseName) throws FileNotFoundException {

        mDatabaseName = databaseName;

        if (mContext == null || mDatabaseName == null) {
            Log.e(TAG, "数据库--openDatabase--环境未配置");
            return null;
        }
        if (!isDataBaseOpen()) {
            // 打开/建立新的 数据库
            mSQLiteDatabase = mContext.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, // Context.MODE_PRIVATE
                    null);
        }

        return mSQLiteDatabase;
    }

    public Cursor rawQuery(String sql) {
        if (!isDataBaseOpen()) {
            return null;
        }
        Cursor result = null;
        try {
            result = mSQLiteDatabase.rawQuery(sql, null);
        } catch (SQLException e) {
            Log.e(TAG, "查询全部-失败");
            if (result != null) {
                result.close();
            }
            return null;
        }
        return result;
    }

    // /////////////////////////////////////////////////////////////////////////

    public Cursor selectData(String tableName, String columnName, String whereName, int whereVal) {
        if (mSQLiteDatabase == null) {
            Log.e(TAG, "数据库--selectData--未连接上");
            return null;
        }

        String SELECT_DATA;
        if (whereName == null) {
            SELECT_DATA = "select " + columnName + " from " + tableName;
        } else {
            SELECT_DATA = "select " + columnName + " from " + tableName + " where " + whereName
                    + "=" + whereVal;
        }
        Cursor result = mSQLiteDatabase.rawQuery(SELECT_DATA, null);
        return result;
    }

    /**
     * 简易查询表数据
     * 
     * @param tableName 表名称
     * @param columnName 表列名称
     * @param whereName 条件名称
     * @param whereVal 条件值
     * @return 结果集
     */
    public Cursor selectData(String tableName, String columnName, String whereName, String whereVal) {
        if (mSQLiteDatabase == null) {
            Log.e(TAG, "数据库--selectData--未连接上");
            return null;
        }

        String SELECT_DATA;
        if (whereName == null) {
            SELECT_DATA = "select " + columnName + " from " + tableName;
        } else {
            SELECT_DATA = "select " + columnName + " from " + tableName + " where " + whereName
                    + "=" + "'" + whereVal + "'";
        }
        Cursor result = mSQLiteDatabase.rawQuery(SELECT_DATA, null);
        return result;
    }

    /**
     * 判断是否存在 表
     * 
     * @param tableName 要判断的表名称
     * @return
     */
    public boolean tabIsExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }

        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
                    + tableName.trim() + "' ";
            cursor = mSQLiteDatabase.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            if (cursor != null) {
                cursor.close();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return result;
    }

    public boolean updateData(String tableName, String columnName, String columnVal,
            String whereName, int whereVal) {
        if (mSQLiteDatabase == null) {
            Log.e(TAG, "数据库--updateData--未连接上");
            return false;
        }

        String UPDATE_DATA = "update " + tableName + " set " + columnName + "=" + columnVal
                + " where " + whereName + "=" + whereVal;
        mSQLiteDatabase.execSQL(UPDATE_DATA);
        return true;
    }

    /**
     * 更新数据
     * 
     * @param tableName
     * @param columnName
     * @param columnVal
     * @param whereName
     * @param whereVal
     * @return
     */
    public boolean updateData(String tableName, String columnName, String columnVal,
            String whereName, String whereVal) {
        if (mSQLiteDatabase == null) {
            Log.e(TAG, "数据库--updateData--未连接上");
            return false;
        }

        String UPDATE_DATA = "update " + tableName + " set " + columnName + "=" + columnVal
                + " where " + whereName + "=" + "'" + whereVal + "'";
        mSQLiteDatabase.execSQL(UPDATE_DATA);
        return true;
    }
}

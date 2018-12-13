package cn.ydw.www.toolslib.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;


/**
 * @author 杨德望
 * Create on 2017/6/27.
 * 描述: 数据库的管理类
 */
public class DataBaseManger {
    private final DBHelper dbHelper;
    private DBLoader mLoader;

    /**
     * 构造化管理类, 最好在 application 里面创建, 然后全局调用
     * @param mContext 上下文
     * @param mLoader 数据库辅助类,
     */
    public DataBaseManger(Context mContext, @NonNull DBLoader mLoader) {
        dbHelper = new DBHelper(mContext, mLoader);
        this.mLoader = mLoader;
    }

    /**
     * 写入一条数据
     * @param values 数据, 请根据之前设置的数据key来设置, {@link DBLoader#dbModel}
     * @return 如果添加成功返回 true
     */
    public boolean addInfo(ContentValues values){
        if (values == null || values.size() != mLoader.dbModel.length )
            return false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long insert = db.insert(mLoader.getTableName(), null, values);
        db.close();
        return insert != -1;
    }

    /**
     * 删除一组数据
     * @param whereArgs 主键对应的值, 例如 主键是id, 那么 whereArgs 传入的是准备删除的所有id值
     * @return 如果删除成功返回 true
     */
    public boolean delInfo(String...  whereArgs){//根据键删除数据
        if (whereArgs == null || whereArgs.length <= 0)
            return false;
        SQLiteDatabase  db = dbHelper.getWritableDatabase();
        String whereClause = mLoader.mPrimaryKey + "=?";
        int delete  = db.delete(mLoader.getTableName(), whereClause, whereArgs);
        db.close();
        return delete != 0;
    }

    /**
     * 更新一组数据
     * @param values 数据, 请根据之前设置的数据key来设置, {@link DBLoader#dbModel}
     * @param whereArgs 主键对应的值, 例如 主键是id, 那么 whereArgs 传入的是准备修改的所有id值
     * @return 如果修改成功返回 true
     */
    public boolean updateInfo(ContentValues values, String... whereArgs){
        if (values == null || values.size() != mLoader.dbModel.length
                || whereArgs == null || whereArgs.length <= 0) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String whereClause = mLoader.mPrimaryKey + "=?";
        int update  = db.update(mLoader.getTableName(), values, whereClause, whereArgs);
        db.close();
        return update!=0;
    }

    /**
     * 查询一组数据
     * @param whereArgs 主键对应的值, 例如 主键是id, 那么 whereArgs 传入的是准备查询的所有id值
     * @return 二维数据, 一组[ 完整对象[ 各种键对应的内容]]
     */
    public ArrayList<ArrayList<DBModel>> findInfo(String... whereArgs){
        if (whereArgs == null || whereArgs.length <= 0)
            return null;
        ArrayList<ArrayList<DBModel>> dmList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + mLoader.getTableName()
                + " where " + mLoader.mPrimaryKey + "=?", whereArgs);
        boolean toFirst = cursor.moveToFirst();
        while (toFirst){
            ArrayList<DBModel> info = new ArrayList<>();
            for (DBModel mModel: mLoader.dbModel) {
                mModel.setDbValue(cursor.getString(cursor.getColumnIndex(mModel.getDbKey())));
                info.add(mModel);
            }
            dmList.add(info);
            toFirst = cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return dmList;
    }


}

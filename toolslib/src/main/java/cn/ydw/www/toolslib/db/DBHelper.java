package cn.ydw.www.toolslib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 杨德望
 * Create on 2017/6/27.
 */
class DBHelper extends SQLiteOpenHelper {
    private DBLoader mLoader;
    DBHelper(Context context, DBLoader mLoader){
        super(context, mLoader.getDBName(), null, mLoader.getVersion());
        this.mLoader = mLoader;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(mLoader.createTable());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS [" + mLoader.getTableName() + "]");//数据库升级
        onCreate(db);
    }
}

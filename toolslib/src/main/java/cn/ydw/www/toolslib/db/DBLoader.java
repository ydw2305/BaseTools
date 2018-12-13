package cn.ydw.www.toolslib.db;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/11/12
 * 描述:数据库参数,
 * =========================================
 */
public abstract class DBLoader implements Serializable {
    private static final int DefaultKeyLen = 50; // 默认键值对的内容可以存储的字符数量
    DBModel[] dbModel;
    String mPrimaryKey;

    public DBLoader(DBModel... dbModel) {
        this.dbModel = dbModel;
    }

    /**
     * @return
     * 返回数据库版本号
     */
    public int getVersion(){
        return 1;
    }

    /**
     * @return
     * 返回数据库的名字
     */
    public String getDBName(){
        return "ydw.db";
    }

    /**
     * @return
     * 返回表的名字
     */
    public String getTableName() {
        return "TableData";
    }
    /**
     * 建表语句
     */
    public String createTable(){
        StringBuilder SQL_CREATE_TABLE = new StringBuilder()
                .append("create table ").append(getTableName())
                .append("(");
        boolean hasSetPrimary = false;
        for (DBModel mModel: dbModel) {
            SQL_CREATE_TABLE.append(", ")
                    .append(mModel.getDbKey())
                    .append(" varchar(").append(mModel.getDbKeyLen()).append(")");
            if (mModel.isPrimary() && !hasSetPrimary) {
                hasSetPrimary = true;
                mPrimaryKey = mModel.getDbKey();
                SQL_CREATE_TABLE.append(" primary key");
            }
        }
        if (!hasSetPrimary) {
            mPrimaryKey = "dbID";
            SQL_CREATE_TABLE.append(", ")
                    .append(mPrimaryKey)
                    .append(" varchar(").append(DefaultKeyLen).append(") primary key");
        }
        SQL_CREATE_TABLE.append(")");
        return SQL_CREATE_TABLE.toString();
    }
}

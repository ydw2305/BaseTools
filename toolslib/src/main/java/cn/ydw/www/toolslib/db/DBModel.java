package cn.ydw.www.toolslib.db;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/11/12
 * 描述:
 * =========================================
 */
public class DBModel {
    private String dbKey;
    private int dbKeyLen = 50;
    private String dbValue;
    private boolean isPrimary;

    public DBModel(String dbKey, int dbKeyLen, boolean isPrimary) {
        this.dbKey = dbKey;
        if (dbKeyLen > 0) {
            this.dbKeyLen = dbKeyLen;
        }
        this.isPrimary = isPrimary;
    }

    public String getDbKey() {
        return dbKey;
    }

    public int getDbKeyLen() {
        return dbKeyLen;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public String getDbValue() {
        return dbValue;
    }

    public void setDbValue(String dbValue) {
        this.dbValue = dbValue;
    }

}

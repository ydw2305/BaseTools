package cn.ydw.www.toolslib.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/12/8
 * 描述:
 * =========================================
 */
public class AreaCodeModel implements Parcelable{
    @Expose
    private String code; // 地区码
    @Expose
    private String locale;// 地区号
    @Expose
    private String tw;// 繁体中文
    @Expose
    private String en;// 英文
    @Expose
    private String zh;// 中文
    @Expose
    private String pinyin;//拼音

    public AreaCodeModel() {
    }

    private AreaCodeModel(Parcel in) {
        code = in.readString();
        locale = in.readString();
        tw = in.readString();
        en = in.readString();
        zh = in.readString();
        pinyin = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(locale);
        dest.writeString(tw);
        dest.writeString(en);
        dest.writeString(zh);
        dest.writeString(pinyin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AreaCodeModel> CREATOR = new Creator<AreaCodeModel>() {
        @Override
        public AreaCodeModel createFromParcel(Parcel in) {
            return new AreaCodeModel(in);
        }

        @Override
        public AreaCodeModel[] newArray(int size) {
            return new AreaCodeModel[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTw() {
        return tw;
    }

    public void setTw(String tw) {
        this.tw = tw;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String toString() {
        return "AreaCodeModel{" +
                "code='" + code + '\'' +
                ", locale='" + locale + '\'' +
                ", tw='" + tw + '\'' +
                ", en='" + en + '\'' +
                ", zh='" + zh + '\'' +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }
}

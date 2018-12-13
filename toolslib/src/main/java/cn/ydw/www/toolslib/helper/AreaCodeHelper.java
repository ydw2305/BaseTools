package cn.ydw.www.toolslib.helper;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cn.ydw.www.toolslib.listener.NetCallBack;
import cn.ydw.www.toolslib.model.AreaCodeModel;
import cn.ydw.www.toolslib.utils.Logger;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/12/8
 * 描述: 地区码相关信息辅助类
 * =========================================
 */
public class AreaCodeHelper {



    public static void getAreaCode(Context mContext, NetCallBack<ArrayList<AreaCodeModel>> callBack) {
        if (mContext != null && callBack != null) {
            new AreaCodeLoader(callBack).execute(mContext.getResources());
        }
    }


    private static class AreaCodeLoader extends AsyncTask<Resources, Void, ArrayList<AreaCodeModel>> {
        private NetCallBack<ArrayList<AreaCodeModel>> mCallBack;
        private String errorMsg;

        AreaCodeLoader(NetCallBack<ArrayList<AreaCodeModel>> callBack) {
            mCallBack = callBack;
        }

        @Override
        protected ArrayList<AreaCodeModel> doInBackground(Resources... resources) {
            if (resources != null && resources.length > 0) {
                Resources res = resources[0];
                if (res != null) {
                    InputStream is = null;
                    InputStreamReader isr = null;
                    BufferedReader br = null;
                    StringBuilder sb = null;
                    try {
                        is = res.getAssets().open("code.json");
                        isr = new InputStreamReader(is);
                        br = new BufferedReader(isr);
                        sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) sb.append(line);

                    } catch (IOException e) {
                        errorMsg = "未读取到资源";
                        Logger.e(errorMsg, e);
                    } finally {
                        try {
                            if (is != null) {
                                is.close();
                            }
                        } catch (Exception e) {
                            Logger.e("关流异常", e);
                        }
                        try {
                            if (isr != null) {
                                isr.close();
                            }
                        } catch (Exception e) {
                            Logger.e("关流异常", e);
                        }
                        try {
                            if (br != null) {
                                br.close();
                            }
                        } catch (Exception e) {
                            Logger.e("关流异常", e);
                        }
                    }
                    if (sb != null) {
                        try {
                            JSONArray mJSONArray = new JSONArray(sb.toString());
                            ArrayList<AreaCodeModel> mAreaCodeList = new ArrayList<>();
                            for (int index = 0; index < mJSONArray.length(); index++) {
                                JSONObject jo = mJSONArray.getJSONObject(index);
                                AreaCodeModel mModel = new AreaCodeModel();
                                mModel.setCode(jo.getString("code"));
                                mModel.setEn(jo.getString("en"));
                                mModel.setTw(jo.getString("tw"));
                                mModel.setZh(jo.getString("zh"));
                                mModel.setLocale(jo.getString("locale"));
                                mModel.setPinyin(jo.getString("pinyin"));
                                mAreaCodeList.add(mModel);
                            }
                            return mAreaCodeList;
                        } catch (Exception e) {
                            errorMsg = "数据解析异常";
                            Logger.e(errorMsg, e);
                        }
                    } else {
                        errorMsg = "数据异常";
                    }
                }
            } else {
                errorMsg = "加载器异常";
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<AreaCodeModel> areaCodeModels) {
            super.onPostExecute(areaCodeModels);
            if (mCallBack != null) {
                if (areaCodeModels != null) {
                    mCallBack.onSuccess(areaCodeModels);
                } else {
                    mCallBack.onError(errorMsg);
                }
            }
        }
    }
}

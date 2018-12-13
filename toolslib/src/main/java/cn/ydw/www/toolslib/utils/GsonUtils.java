/*******************************************************************************
 * <p>Copyright(c) 2017</p>
 * <p>
 * @autor 杨德望
 * @date ${DATE}
 * @see ${PACKAGE_NAME}
 * </p>
 *
 ******************************************************************************/

package cn.ydw.www.toolslib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;

public final class GsonUtils {

    private static Gson getExposeGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(
                Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        builder.excludeFieldsWithoutExposeAnnotation();
        return builder.create();
    }

    public static <T> T parseJSON(String json, Class<T> clazz) {
        return getExposeGson().fromJson(json, clazz);
    }

    /**
     * 注: 映射会有问题, 解析的时候会报 hashMap对象无法装换成 T 对象的问题,
     *
     * Type type = new TypeToken&lt;ArrayList&lt;TypeInfo>>(){}.getType(); <br>
     * Type所在的包：java.lang.reflect <br>
     * TypeToken所在的包：com.google.gson.reflect.TypeToken
     *
     * @param jsonArr json 数组字符串
     * @return 已解析对象
     */
    @SuppressWarnings("unused")
//    public static <T> T parseJSONArray(String jsonArr, Type type) {
//        return getExposeGson().fromJson(jsonArr, type);
//    }

    /**
     * 解析json 形如:
     * {[{"name": "zhangsan", "email": "11@11.com" }, {"name": "lisi", "email": "22@22.com" }, ...]}
     *
     * @param jsonArr 字符串
     * @param <T> 对象类型
     * @return 解析好的数组
     */
    public static <T> ArrayList<T> parseJSONArray(String jsonArr,  Class<T> clazz) {
        // 将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = new JsonParser().parse(jsonArr).getAsJsonArray();
        if (jsonArray == null) return null;
        ArrayList<T> mList = new ArrayList<>();
        for (JsonElement jsonItem : jsonArray) {
            T info = getExposeGson().fromJson(jsonItem, clazz);
            mList.add(info);
        }
        return mList;
    }


}

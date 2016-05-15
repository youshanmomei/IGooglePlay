package cn.qiuc.org.igoogleplay.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by admin on 2016/5/15.
 */
public class JsonUtil {

    public static String parseMapToJson(Map<?, ?> map) {
        try {
            Gson gson = new Gson();
            return gson.toJson(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T parseJsonToBean(String json, Class<T> cls) {
        Gson gson = new Gson();
        T t = null;
        try {
            t = gson.fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;

    }

    public static HashMap<String, Object> parseJsonToMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Objects>>() {
        }.getType();
        HashMap<String, Object> map = null;
        return gson.fromJson(json, type);
    }

    public static List<?> parseJsonToList(String json, Type type){
        Gson gson = new Gson();
        List<?> list = gson.fromJson(json, type);
        return list;
    }

    public static String getFieldValue(String json, String key) {
        if (TextUtils.isEmpty(json)) {
            return  null;
        }
        if (!json.contains(key)) {
            return "";
        }
        JSONObject jsonObject = null;
        String value = null;
        try {
            jsonObject = new JSONObject(json);
            value = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return value;
    }


}

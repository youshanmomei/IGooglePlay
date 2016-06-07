package cn.qiuc.org.igoogleplay.http;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2016/6/8.
 */
public class HttpHelper {
    private static String TAG = "HttpHelper";
    static OkHttpClient okHttpClient = new OkHttpClient();



    /**
     * get请求获取服务器返回数据
     * @param url
     * @return
     */
    public static String get(String url) {

        Log.e(TAG, "request url: " + url);
        String result = null;
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "response: " + result);
        return result;
    }
}

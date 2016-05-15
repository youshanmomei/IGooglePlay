package cn.qiuc.org.igoogleplay.http;

/**
 * Created by admin on 2016/5/15.
 */
public interface Url {
    String SERVER_HOST = "http://127.0.0.1:8090/";

    String HOME = SERVER_HOST + "home?index=%1$d";//one kind of placeholder wording


    String IMAGE_PREFIX = SERVER_HOST + "image?name=";
}

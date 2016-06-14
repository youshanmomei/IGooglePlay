package cn.qiuc.org.igoogleplay.http;

/**
 * Created by admin on 2016/5/15.
 */
public interface Url {
    String SERVER_HOST = "http://127.0.0.1:8090/";

    String HOME = SERVER_HOST + "home?index=%1$d";//one kind of placeholder wording
    String IMAGE_PREFIX = SERVER_HOST + "image?name=";


    String APP_DOWNLOAD = SERVER_HOST+"download?name=%1$s";
    String APP_BREAK_DOWNLOAD = SERVER_HOST+"download?name=%1$s&range=%2$d";
    String APP_DETAIL = SERVER_HOST + "detail?packageName=%2$s";
    String App = SERVER_HOST + "app?index=%1$d";
    String GAME = SERVER_HOST + "game?index=%1$d";
    String SUBJECT = SERVER_HOST + "subject?index=%1$d";
    String RECOMMEND = SERVER_HOST + "recommend?index=%1%d";
}

package cn.qiuc.org.igoogleplay.bean;

import java.util.ArrayList;

import cn.qiuc.org.igoogleplay.util.JsonUtil;

/**
 * Created by admin on 2016/5/15.
 */
public class Home {
    public static Home fromJson(String json){
        return JsonUtil.parseJsonToBean(json, Home.class);
    }

    private ArrayList<String> picture;
    private ArrayList<AppInfo> list;

    public ArrayList<String> getPicture(){
        return picture;
    }

    public void setPicture(ArrayList<String> picture) {
        this.picture = picture;
    }

    public ArrayList<AppInfo> getList(){
        return list;
    }

    public void setList(ArrayList<AppInfo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Home [picture=" + picture + ", list=" + list + "]";
    }
}

package cn.qiuc.org.igoogleplay.bean;

import java.util.ArrayList;

/**
 * Created by admin on 2016/5/15.
 */
public class AppInfo {
    public int id;
    public float stars;
    public long size;
    public String name;
    public String packageName;
    public String iconUrl;
    public String downloadUrl;
    public String des;

    public String downloadNum;
    public String version;
    public String date;
    public String autor;
    public ArrayList<String> screen;
    public ArrayList<SafeInfo> safe;

    @Override
    public String toString() {
        return "AppInfo [id=" + id + ", stars=" + stars + ", size=" + size
                + ", name=" + name + ", packageName=" + packageName + ", iconUrl=" + iconUrl
                + ", downloadUrl=" + downloadUrl + ", des" + des + "]";
    }
}

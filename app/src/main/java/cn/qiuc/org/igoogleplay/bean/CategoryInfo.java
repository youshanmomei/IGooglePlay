package cn.qiuc.org.igoogleplay.bean;

/**
 * Created by admin on 2016/6/16.
 */
public class CategoryInfo {
    public String title;
    public String url1;
    public String url2;
    public String url3;
    public String name1;
    public String name2;
    public String name3;

    public CategoryInfo(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "categoryInfo [tile="+title+", url1="+url1+", url2="+url2+", url3="+url3+", name1="+name1+", name2="+name2+", name3="+name3+"]";
    }
}

package cn.qiuc.org.igoogleplay.manager;

import cn.qiuc.org.igoogleplay.bean.AppInfo;

/**
 * Created by admin on 2016/5/17.
 */
public class DownloadInfo {
    public int id;
    public long size;
    public long currentLength;
    public int state;
    public String downloadUrl;
    public String path;

    public static DownloadInfo create(AppInfo appInfo){
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.id = appInfo.id;
        downloadInfo.size = appInfo.size;
        downloadInfo.downloadUrl = appInfo.downloadUrl;
        downloadInfo.currentLength = 0;

        downloadInfo.state = DownloadManager.STATE_NONE;
        downloadInfo.path = DownloadManager.DOWNLOAD_DIR + "/" + appInfo.name + ".apk";
        return downloadInfo;
    }
}

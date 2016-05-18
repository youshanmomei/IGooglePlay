package cn.qiuc.org.igoogleplay.manager;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.qiuc.org.igoogleplay.bean.AppInfo;
import cn.qiuc.org.igoogleplay.global.IGooglePlayApplication;
import cn.qiuc.org.igoogleplay.http.Url;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2016/5/17.
 */
public class DownloadManager {
    private final String TAG = "DownloadManager";

    //download directory
    public static String DOWNLOAD_DIR = Environment.getExternalStorageDirectory() + "/" + IGooglePlayApplication.getContext().getPackageName() + "/download";
    public static final int STATE_NONE = 0;
    public static final int STATE_WAITTING = 1;
    public static final int STATE_DOWNLOADING = 2;
    public static final int STATE_FINISH = 3;
    public static final int STATE_ERROR = 4;
    public static final int STATE_PAUSE = 5;

    private List<DownloadObserver> observers = new ArrayList<DownloadObserver>();
    private HashMap<Integer, DownloadInfo> downloadInfoMap = new HashMap<Integer, DownloadInfo>();
    private HashMap<Integer, DownloadTask> downloadTaskMap = new HashMap<Integer, DownloadTask>();

    private OkHttpClient okHttpClient = new OkHttpClient();

    private static DownloadManager mInstatnce = new DownloadManager();

    private DownloadManager() {
        File file = new File(DOWNLOAD_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static DownloadManager getmInstatnce() {
        return mInstatnce;
    }

    public void download(AppInfo appInfo) {
        if (appInfo == null) return;

        //1. get downloadInfo
        DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.id);
        if (downloadInfo == null) {
            //if there null, create it
            //and then put it in downloadInfoMap
            downloadInfo = DownloadInfo.create(appInfo);
            downloadInfoMap.put(appInfo.id, downloadInfo);
        }

        //2.to determine whether to start downloading
        //not downloading or downloaded can be downloaded
        if (downloadInfo.state == STATE_NONE || downloadInfo.state == STATE_PAUSE || downloadInfo.state == STATE_ERROR) {
            //3. create download task
            //   and maintained up into downloadTask Map
            DownloadTask downloadTask = new DownloadTask(downloadInfo);
            downloadTaskMap.put(downloadInfo.id, downloadTask);

            downloadInfo.state = STATE_WAITTING;
            notifyStateChange(downloadInfo);

            //4.task to ThreadPoolManager execution
            ThreadPoolManager.getInstance().execute(downloadTask);
        }


    }

    class DownloadTask implements Runnable {
        private DownloadInfo downloadInfo;

        public DownloadTask(DownloadInfo downloadInfo) {
            super();
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            //5. once run method is running,
            //   it means task has been execute
            //   need to change state to downloading
            downloadInfo.state = STATE_DOWNLOADING;
            notifyStateChange(downloadInfo);

            //6.specific download, two kinds of education
            //  re-download and resume downloads
            Request request;
            File file = new File(downloadInfo.path);
            if (!file.exists() || file.length() != downloadInfo.currentLength) {
                //file is not exists, or file size in downloadInfo saved inconsistent
                //the file is invalid
                file.delete();
                downloadInfo.currentLength = 0;

                //need to re-download
                String url = String.format(Url.APP_DOWNLOAD, downloadInfo.downloadUrl);
                request = new Request.Builder().url(url).build();
            } else {
                //need to resume download
                String url = String.format(Url.APP_BREAK_DOWNLOAD, downloadInfo.downloadUrl, downloadInfo.currentLength);
                request = new Request.Builder().url(url).build();
            }

            Response response = null;
            InputStream is = null;
            FileOutputStream fos = null;

            try {
                response = okHttpClient.newCall(request).execute();
                is = response.body().byteStream();

                if (response != null && is != null) {
                    fos = new FileOutputStream(file, true);

                    byte[] buffer = new byte[1024 * 8];
                    int len = -1;
                    while ((len = is.read(buffer)) != -1 && downloadInfo.state == STATE_DOWNLOADING) {
                        fos.write(buffer, 0, len);
                        fos.flush();

                        downloadInfo.currentLength = downloadInfo.currentLength + len;
                        notifyProgressChange(downloadInfo);
                    }
                } else {
                    //the server return an error
                    downloadInfo.state = STATE_ERROR;
                    downloadInfo.currentLength = 0;
                    file.delete();
                    notifyStateChange(downloadInfo);
                }


            } catch (IOException e) {
                e.printStackTrace();

                downloadInfo.state = STATE_ERROR;
                downloadInfo.currentLength = 0;
                file.delete();
                notifyStateChange(downloadInfo);
            } finally {
                try {
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //handle the case did not come here,there are has three situation
            //1.download finish  2.download error 3.download pause
            if (file.length() == downloadInfo.currentLength && downloadInfo.state == STATE_DOWNLOADING) {
                //说明下载完成
                downloadInfo.state = STATE_FINISH;
                notifyStateChange(downloadInfo);
            } else if (downloadInfo.state == STATE_PAUSE) {
                notifyStateChange(downloadInfo);
            } else {
                downloadInfo.state = STATE_ERROR;
                downloadInfo.currentLength = 0;
                file.delete();
                notifyStateChange(downloadInfo);
            }

            //requires runnable is removed from the current from downloadTaskMap at the end of the task
            downloadTaskMap.remove(downloadInfo.id);
        }
    }

    public void pause(AppInfo appInfo) {
        DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.id);
        if (downloadInfo != null) {
            DownloadTask downloadTask = downloadTaskMap.get(downloadInfo.id);
            ThreadPoolManager.getInstance().cancel(downloadTask);

            downloadInfo.state = STATE_PAUSE;
            notifyStateChange(downloadInfo);
        }

    }

    public DownloadInfo getDownloadInfo(AppInfo appInfo) {
        return downloadInfoMap.get(appInfo.id);
    }

    public void installApk(AppInfo appInfo) {
        DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.id);
        if (downloadInfo != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + downloadInfo.path), "application/vnd.android.package-archive");
            IGooglePlayApplication.getContext().startActivity(intent);
        }
    }

    /**
     * notify all listeners download progress update
     *
     * @param downloadInfo
     */
    private void notifyStateChange(DownloadInfo downloadInfo) {
        for (DownloadObserver oberver : observers) {
            oberver.onDownloadProgressChnage(downloadInfo);
        }
    }

    private void notifyProgressChange(DownloadInfo downloadInfo) {
        for (DownloadObserver observer : observers) {
            observer.onDownloadProgressChnage(downloadInfo);
        }
    }

    public void registerDownloadObserver(DownloadObserver downloadObserver) {
        if (downloadObserver != null && !observers.contains(downloadObserver)) {
            observers.add(downloadObserver);
        }
    }

    public void unregisterDownloadObserver(DownloadObserver downloadObserver) {
        if (downloadObserver != null && observers.contains(downloadObserver)) {
            observers.remove(downloadObserver);
        }
    }

    public interface DownloadObserver {
        void onDownloadStateChange(DownloadInfo downloadInfo);

        void onDownloadProgressChnage(DownloadInfo downloadInfo);
    }
}

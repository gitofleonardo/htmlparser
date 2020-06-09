package cn.huangchengxi.htmlparser.databean;

import java.io.Serializable;
import java.util.Comparator;

public class DownloadItem implements Serializable {
    public static class ThisComparator implements Comparator<DownloadItem>{
        @Override
        public int compare(DownloadItem item, DownloadItem t1) {
            if (item.time==t1.time) return 0;
            else if (item.time>t1.time) return 1;
            else return -1;
        }
    }
    public static enum STATE{
        DOWNLOADING,
        DOWNLOADED,
        PAUSED,
        CANCELED,
        FAILED
    }

    public Long id;
    public String name;
    public STATE state;
    public String location;
    public String url;
    public long size;
    public long downloadedSize;
    public long time;

    public DownloadItem(){

    }
}

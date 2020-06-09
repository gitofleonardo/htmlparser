package cn.huangchengxi.htmlparser.controller;

import cn.huangchengxi.htmlparser.databean.AnalyzeResult;
import cn.huangchengxi.htmlparser.databean.DirectoryResult;
import cn.huangchengxi.htmlparser.databean.DownloadItem;
import cn.huangchengxi.htmlparser.toolkits.HtmlParser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class MainController {
    private String lastDir="/";
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/analyze")
    @ResponseBody
    public String analyze(@RequestParam(value = "address") String url){
        try{
            AnalyzeResult result= HtmlParser.parseHtml(url);
            return JSON.toJSONString(result);
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }
    @RequestMapping("/directories")
    @ResponseBody
    public String directories(@RequestParam(value = "currentPath") String path){
        Properties pros=System.getProperties();
        String os=pros.getProperty("os.name");
        if (os.equals("Linux")){
            path="/home/huangchengxi"+path;
            File file=new File(path);
            if (!file.exists() || file.isFile()){
                return "{\"result\":\"Directory Not Found\"}";
            }
            File[] files=file.listFiles();
            DirectoryResult result=new DirectoryResult();
            result.result="success";
            if (files==null){
                return JSON.toJSONString(result);
            }
            for (File f:files){
                if (f.isDirectory())
                    result.dirs.add(f.getName());
            }
            return JSON.toJSONString(result);
        }else if (os.equals("Windows")){
            return "";
        }
        return "";
    }
    private static final Object lock=new Object();
    private static Long ids=0L;
    private static Long getAvailableID(){
        synchronized (lock){
            return ids++;
        }
    }
    private ExecutorService threadPool= Executors.newCachedThreadPool();
    private class DownloadRunnableImp implements Runnable{
        private String KEY="DOWNLOAD_ITEM";
        private String dir;
        private String url;
        private String filename;
        public DownloadRunnableImp(String directory,String filename,String url){
            this.dir=directory;
            this.url=url;
            this.filename=filename;
        }
        private boolean insert(DownloadItem item){
            HashOperations<String,String,DownloadItem> hops=redisTemplate.opsForHash();
            hops.put(KEY,item.id.toString(),item);
            return true;
        }
        @Override
        public void run() {
            try{
                int byteSum=0;
                int byteread=0;
                URL u=new URL(url);

                URLConnection connection=u.openConnection();
                InputStream is=connection.getInputStream();
                File file=new File(dir+"/"+filename);
                if (!file.exists()){
                    file.createNewFile();
                }else{
                    //overwrite
                    file.delete();
                    file.createNewFile();
                }
                FileOutputStream fos=new FileOutputStream(file);
                byte[] buffer=new byte[1024];
                long time=System.currentTimeMillis();

                DownloadItem item=new DownloadItem();
                item.location=dir+"/"+filename;
                item.name=filename;
                item.url=url;
                item.size=0;
                item.size=connection.getContentLength();
                item.downloadedSize=0;
                item.id=getAvailableID();
                item.time=System.currentTimeMillis();
                item.state= DownloadItem.STATE.DOWNLOADING;
                insert(item);

                while ((byteread=is.read(buffer))!=-1){
                    byteSum+=byteread;
                    item.downloadedSize=byteSum;
                    fos.write(buffer,0,byteread);
                    if (System.currentTimeMillis()-time>1000){
                        //check and update database
                        insert(item);
                        time=System.currentTimeMillis();
                    }
                }
                item.state= DownloadItem.STATE.DOWNLOADED;
                insert(item);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/download")
    @ResponseBody
    public String download(@RequestBody String files){
        System.out.println(files);
        try{
            JSONObject down=JSONObject.parseObject(files);
            String path="/home/huangchengxi"+down.getString("directory");
            lastDir=path;
            JSONArray fs=down.getJSONArray("files");
            for (int i=0;i<fs.size();i++){
                JSONObject obj=fs.getJSONObject(i);
                String filename=obj.getString("name")+"."+obj.getString("type");
                String url=obj.getString("url");
                DownloadRunnableImp d=new DownloadRunnableImp(path,filename,url);
                threadPool.submit(d);
            }
            return "{\"result\":\"success\"}";
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }
    @RequestMapping(value="/DownloadState",method = RequestMethod.GET)
    @ResponseBody
    public String states(){
        HashOperations<String,String,DownloadItem> ops=redisTemplate.opsForHash();
        Map<String,DownloadItem> map=ops.entries("DOWNLOAD_ITEM");
        List<DownloadItem> list = new ArrayList<>(map.values());
        Comparator<DownloadItem> com=new DownloadItem.ThisComparator();
        list.sort(com);
        return JSON.toJSONString(list);
    }
    @RequestMapping("/openfiles")
    @ResponseBody
    public String openfiles() throws IOException {
        Runtime rt=Runtime.getRuntime();
        Properties pros=System.getProperties();
        String os=pros.getProperty("os.name");
        if (os.equals("Linux")){
            rt.exec(new String[]{"nautilus",lastDir});
        }else if (os.equals("Windows")){
            rt.exec("explorer "+lastDir.replace(" ","\" \"")+"/");
        }
        return "";
    }
}

package cn.huangchengxi.htmlparser.controller;

import cn.huangchengxi.htmlparser.databean.AnalyzeResult;
import cn.huangchengxi.htmlparser.databean.DirectoryResult;
import cn.huangchengxi.htmlparser.toolkits.HtmlParser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.Properties;

@Controller
public class MainController {
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
    @RequestMapping("/download")
    @ResponseBody
    public String download(@RequestBody String files){
        try{
            JSONObject down=JSONObject.parseObject(files);
            JSONArray fs=down.getJSONArray("files");
            return "{\"result\":\"success\"}";
        }catch (Exception e){
            return e.toString();
        }
    }
}

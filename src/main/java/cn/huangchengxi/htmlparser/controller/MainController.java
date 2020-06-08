package cn.huangchengxi.htmlparser.controller;

import cn.huangchengxi.htmlparser.databean.AnalyzeResult;
import cn.huangchengxi.htmlparser.toolkits.HtmlParser;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
}

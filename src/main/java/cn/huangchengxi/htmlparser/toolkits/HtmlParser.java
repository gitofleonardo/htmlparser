package cn.huangchengxi.htmlparser.toolkits;

import cn.huangchengxi.htmlparser.databean.AnalyzeItem;
import cn.huangchengxi.htmlparser.databean.AnalyzeResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
    public static AnalyzeResult parseHtml(String url) throws Exception{
        AnalyzeResult result=new AnalyzeResult();
        Document doc= Jsoup.connect(url).get();
        Elements imgs=doc.select("img");
        for (Element ele:imgs){
            try{
                AnalyzeItem item=new AnalyzeItem();
                item.url=ele.attr("abs:src");
                if (item.url.length()<=0) continue;
                item.name=item.url.substring(item.url.lastIndexOf("/")+1,item.url.lastIndexOf("."));
                item.type=item.url.substring(item.url.lastIndexOf(".")+1);
                result.items.add(item);
            }catch (Exception ignored){
            }
        }
        Elements as=doc.select("a");
        for (Element ele:as){
            try{
                String href=ele.attr("abs:href");
                if (href.length()<=0) continue;
                AnalyzeItem item=new AnalyzeItem();
                item.url=href;
                item.name=item.url.substring(item.url.lastIndexOf("/")+1,item.url.lastIndexOf("."));
                item.type=item.url.substring(item.url.lastIndexOf(".")+1);

                result.items.add(item);
            }catch (Exception ignored){}
        }
        Elements scripts=doc.select("script");
        for (Element ele:scripts){
            try{
                String href=ele.attr("abs:src");
                if (href.length()<=0) continue;
                AnalyzeItem item=new AnalyzeItem();
                item.url=href;
                item.name=item.url.substring(item.url.lastIndexOf("/")+1,item.url.lastIndexOf("."));
                item.type=item.url.substring(item.url.lastIndexOf(".")+1);
                result.items.add(item);
            }catch (Exception e){}
        }
        Elements links=doc.select("link");
        for (Element ele:links){
            try{
                String href=ele.attr("abs:href");
                if (href.length()<=0) continue;
                AnalyzeItem item=new AnalyzeItem();
                item.url=href;
                item.name=item.url.substring(item.url.lastIndexOf("/")+1,item.url.lastIndexOf("."));
                item.type=item.url.substring(item.url.lastIndexOf(".")+1);
                result.items.add(item);
            }catch (Exception e){}
        }
        result.result="success";
        return result;
    }
}

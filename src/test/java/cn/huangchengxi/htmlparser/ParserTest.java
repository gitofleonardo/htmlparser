package cn.huangchengxi.htmlparser;

import org.junit.jupiter.api.Test;

public class ParserTest {
    @Test
    public void testSubString(){
        String url="http://www.huangchengxi.cn/static/imgs/logo1.png";
        String name=url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));
        System.out.println(name);
    }
}

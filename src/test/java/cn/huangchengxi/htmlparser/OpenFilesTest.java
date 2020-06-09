package cn.huangchengxi.htmlparser;

import org.junit.jupiter.api.Test;

import java.io.File;

public class OpenFilesTest {
    @Test
    public void testOpen() throws Exception{
        Runtime rt=Runtime.getRuntime();
        //rt.exec("nautilus /home/huangchengxi/Documents/parser\\ test");
        rt.exec("nautilus",new String[]{},new File("/home/huangchengxi/Documents/parser test/"));
        rt.exec(new String[]{"nautilus","/home/huangchengxi/Documents/parser test"});
    }
}

package cn.huangchengxi.htmlparser.conf;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Properties;

@Configuration
public class IndexConfig {
    @EventListener(ApplicationReadyEvent.class)
    public void applicationReady(){
        String url="http://127.0.0.1:8899";
        Runtime runtime=Runtime.getRuntime();
        Properties pros=System.getProperties();
        String os=pros.getProperty("os.name");
        try {
            System.out.println(os);
            if (os.equals("Linux")){
                runtime.exec("x-www-browser "+url);
            }else if (os.equals("Windows")){
                runtime.exec("cmd /c "+url);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

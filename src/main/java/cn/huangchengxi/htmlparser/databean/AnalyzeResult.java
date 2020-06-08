package cn.huangchengxi.htmlparser.databean;

import java.io.Serializable;
import java.util.ArrayList;

public class AnalyzeResult implements Serializable {
    public String result;
    public String message;
    public ArrayList<AnalyzeItem> items=new ArrayList<>();
}

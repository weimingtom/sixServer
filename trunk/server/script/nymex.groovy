import java.util.HashMap;
import org.ngame.util.HttpUtils;
  
  
  String u="http://hq.sinajs.cn/?_="+System.currentTimeMillis()+"/&list=hf_CL";
    byte[] r=HttpUtils.get(u, new HashMap<String, Object>());
    String c=new String(r);
    int start=c.indexOf('"');
    int end =c.indexOf('"',start+1);
     c=c.substring(start+1, end);
    int index=c.indexOf(',');
    c=c.substring(0, index);
    System.out.println(c);
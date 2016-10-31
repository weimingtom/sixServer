package org.ngame.eventest.test6;

import java.util.ArrayList;
import java.util.Collection;

public class DemoSource {  
    private Collection<Object> repository=new ArrayList<>();//监听自己的监听器队列  
    public DemoSource(){}  
    public void addDemoListener(DemoListener dl) {  
           repository.add(dl);  
    }  
    public void notifyDemoEvent() {//通知所有的监听器  
           for(Object obj : repository){
        	   DemoListener dl = (DemoListener)obj;
        	   dl.handleEvent(new DemoEvent(this));  
           }
    }  
}  
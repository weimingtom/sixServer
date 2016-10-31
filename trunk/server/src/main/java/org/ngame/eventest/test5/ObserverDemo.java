package org.ngame.eventest.test5;
//观察者   
class Watcher implements java.util.Observer {   
  public void update(java.util.Observable obj, Object arg) {   
    System.out.println("Update() called, count is "    
                                + ((Integer) arg).intValue());   
}   
}   
           
//被观察者   
class BeingWatched extends java.util.Observable {   
    void counter(int period) {   
        for(; period>=0; period-- ) {   
                setChanged();   
                notifyObservers(new Integer(period));   
                try {   
                        Thread.sleep(100);   
                } catch( InterruptedException e) {   
                  System.out.println("Sleep interrupeted" );   
                }   
        }   
}   
};   
  
//演示   
public class ObserverDemo {   
    public static void main(String[] args) {   
        BeingWatched beingWatched = new BeingWatched();//受查者   
        Watcher watcher = new Watcher();//观察者   
        beingWatched.addObserver(watcher);   
        beingWatched.counter(10);   
    }   
}  
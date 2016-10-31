package protocol;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThread {
	public static void main(String[] args) {
		System.out.println("begin");
		final ThreadT2 tt2=new ThreadT2();
		ThreadT tt=new ThreadT();
//		new Thread(tt,"threadt").start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				tt2.execute();
			}
		}).start();;
		System.out.println("end");
		ExecutorService exec=Executors.newCachedThreadPool();  
//		exec.execute(command);
	}
}

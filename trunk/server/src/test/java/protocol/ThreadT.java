package protocol;

public class ThreadT implements Runnable {

	@Override
	public void run() {
		try {
			System.out.println("threadt start");
			Thread.sleep(10000);
			System.out.println("threadt end");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

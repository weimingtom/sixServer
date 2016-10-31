package protocol;

public class ThreadT2 {

	public void execute() {
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

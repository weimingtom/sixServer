package org.ngame.eventest.test4;
/**
 * 当一事件发生时，需要通知实现IObserver接口的对象，并调用interesingEvent()方法
 * @author yongxing.shao
 *
 */
class EventNotifier {
	private IObserver observer; //观察者
	private boolean somethingHappened; //标志事件是否发生
	
	public EventNotifier(IObserver observer) {
		this.observer = observer;
		this.somethingHappened = false;
	}
	
	public void doWork() {
		if (somethingHappened) {
			//事件发生时，通过调用接口的这个方法来通知
			observer.processInterestedEvent();
		}
	}
	
	public void setSomethingHappened(boolean f){
		this.somethingHappened=f;
		doWork();
	}
}
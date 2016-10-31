package org.ngame.eventest.test4;
/**
 * 观察者实现
 * @author yongxing.shao
 *
 */
class Observer implements IObserver {
	private EventNotifier en;
	
	public Observer() {
		//新建一个事件通知者对象，并把自己传递给它
		this.en = new EventNotifier(this);
	}
	
	//实现事件发生时，实际处理事件的方法
	public void processInterestedEvent() {
		System.out.println("Observer: event happened");
	}
}
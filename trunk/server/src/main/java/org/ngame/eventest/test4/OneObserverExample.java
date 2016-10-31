package org.ngame.eventest.test4;
/**
 * 对某个事件只有一个订阅者的例子
 * @author yongxing.shao
 *
 */
public class OneObserverExample {
	public static void main(String[] args) {
		IObserver observer = new Observer();
		EventNotifier notifier = new EventNotifier(observer);
		notifier.setSomethingHappened(true);
		notifier.setSomethingHappened(false);
		notifier.setSomethingHappened(true);
		notifier.setSomethingHappened(true);
	}
}
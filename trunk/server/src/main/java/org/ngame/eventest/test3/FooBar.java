package org.ngame.eventest.test3;
class FooBar {
	private ICallback callback;
	public void setCallback(ICallback callback) {
		this.callback = callback;
		doSth();
	}
	
	public void doSth() {
		callback.process();
	}
}
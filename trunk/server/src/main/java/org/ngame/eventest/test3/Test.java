package org.ngame.eventest.test3;

public class Test {
	public static void main(String[] args) {
		FooBar fb=new FooBar();
		fb.setCallback(new ICallback() {
			@Override
			public void process() {
				System.out.println("真的回调我了哦");
			}
		});
	}
}

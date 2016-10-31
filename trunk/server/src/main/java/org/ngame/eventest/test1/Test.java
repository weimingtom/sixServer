package org.ngame.eventest.test1;

public class Test {
	public static void main(String[] args) {
		StateHolder sh=new StateHolder();
		sh.addStateListener(new StateListener() {
			@Override
			public void stateChanged(StateEvent event) {
				System.out.println("监听到状态改变,我改几次状态就打印几次你信么");
			}
		});
		sh.setState(1);
		sh.setState(2);
		
	}
}

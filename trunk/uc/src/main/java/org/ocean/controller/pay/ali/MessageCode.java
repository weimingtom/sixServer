package org.ocean.controller.pay.ali; 
/**
 * @ClassName: MessageCode.java
 * @author chenpeng
 * @date 2014-10-27 下午1:49:57
 * @Description: 
 *
 */
public class MessageCode {
	
	private boolean successFlag;
	
	private String message;
	
	private Object object;
	
	public MessageCode(){
		
	}
	
	public MessageCode(boolean successFlag,String message,Object object){
		this.successFlag=successFlag;
		this.message=message;
		this.object=object;
	}

	public boolean isSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(boolean successFlag) {
		this.successFlag = successFlag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	

}
 
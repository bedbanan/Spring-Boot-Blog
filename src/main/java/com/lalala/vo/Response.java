package com.lalala.vo;
/*
 * 用来封装我们所有的返回对象
 * 
 */
public class Response {
    private boolean success;   //是否成功
    private String message;    //返回信息
    private Object body;      //返回响应数据
    
	public Response(boolean success, String message) {
		this.success = success;
		this.message = message;
	}
	public Response(boolean success, String message, Object body) {
		
		this.success = success;
		this.message = message;
		this.body = body;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
    
}

package com.fplusf.message.exception;

import java.util.List;
import java.util.Map;

public abstract class BaseException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2358639437884248430L;
	
	private String title;
	private String messageCode;
	private transient Object[] args;
	private transient List<Map<String, Object>> errors;

	Exception exception;
	
	private static final String DEFAULT_TITLE = "General Error";

	public BaseException(String errorCode, Object[] args) {
		this.title = DEFAULT_TITLE;
		this.messageCode = errorCode;
		this.args = args;
	}

	public BaseException(String title, String messageCode, Object[] args) {
		this.title = title;
		this.messageCode = messageCode;
		this.args = args;
	}
	public BaseException(String title, String messageCode, List<Map<String, Object>> errors) {
		this.title = title;
		this.messageCode = messageCode;
		this.setErrors(errors);
	}

	public BaseException(String messageCode, Object[] args, Throwable cause) {
		super(cause);
		this.title = DEFAULT_TITLE;
		this.messageCode = messageCode;
		this.args = args;
	}

	public BaseException(String title, String messageCode, Object[] args, Throwable cause) {
		super(cause);
		this.title = title;
		this.messageCode = messageCode;
		this.args = args;
	}
	
	

	public String getTitle() {
		return title;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public Object[] getArgs() {
		return args;
	}

	protected void setTitle(String title) {
		this.title = title;
	}

	protected void setCodeMessage(String messageCode) {
		this.messageCode = messageCode;
	}

	protected void setArgs(Object[] args) {
		this.args = args;
	}

	private String auxMessage;
	private String message;

	public BaseException(String messageCode, String message, String auxMessage) {
		this.title = DEFAULT_TITLE;
		this.messageCode = messageCode;
		this.auxMessage = auxMessage;
		this.message = message;
	}
	
	public BaseException(String messageCode, String message) {
		this.title = DEFAULT_TITLE;
		this.messageCode = messageCode;
		this.message = message;
	}


	public String getAuxMessage() {
		return auxMessage;
	}

	public void setAuxMessage(String auxMessage) {
		this.auxMessage = auxMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Map<String, Object>> getErrors() {
		return errors;
	}

	public void setErrors(List<Map<String, Object>> errors) {
		this.errors = errors;
	}

}

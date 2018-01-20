package com.flea.common.exception;

import org.apache.shiro.authc.AuthenticationException;

public class ValidCodeException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8152793074406141288L;
	
	public ValidCodeException(String msg){  
        super(msg);  
    }  
	
}

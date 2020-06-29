package com.lalala.util;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;


/*
 * 工具类，专门处理异常的处理器
 */
public class ConstraintViolationExceptionHandler {
     /*
             * 批量获取异常信息
     */
	public static String getMessage(ConstraintViolationException e) {  //参数是异常对象数组e
		List<String> msgList=new ArrayList<>();
		for(ConstraintViolation<?> constrainViolation : e.getConstraintViolations()) {
			msgList.add(constrainViolation.getMessage());  //用foreach将数组导入list中然后转换成字符串
		}
		String messages=StringUtils.join(msgList.toArray(),";");
		return messages;
	}
}

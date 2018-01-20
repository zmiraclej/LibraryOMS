package com.flea.common.tag;

import java.lang.reflect.Field;
import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
/**
 * FreeMarker自定义方法
 * 获取静态字段
 *
 */
public class AchieveStaticFieldMethod implements TemplateMethodModelEx {

	@Override
	public Object exec(List arguments) throws TemplateModelException {
		String className = arguments.get(0).toString();
		className = className.trim();
		int index = className.lastIndexOf('.');
		String fieldName = className.substring(index+1,className.length());//获取字段名
		className = className.substring(0, index);//获取完整类名
		try {
			Class<?> targetClass = Class.forName(className);
			Field field = targetClass.getField(fieldName);
			return field.get(null);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}

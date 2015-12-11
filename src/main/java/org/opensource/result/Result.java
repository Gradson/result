package org.opensource.result;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

public class Result {
	
	private Set<String> includeProperties;
	private Set<String> excludeProperties;

	public Result() {
		includeProperties = new HashSet<String>();
		excludeProperties = new HashSet<String>();
	}
	
	public <T> T getResult(Object objectSource, Class<T> clazz) {
		try {
			String path = "";
			return filter(objectSource, clazz, path);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	private <T> T filter(Object objectSource, Class<T> clazz, String path) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		T objectTarget = clazz.newInstance();
		Field[] declaredFields = objectTarget.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			copyProperty(objectSource, objectTarget, field, path);
		}
		
		return objectTarget;
	}

	private <T> void copyProperty(Object objectSource, T objectTarget, Field field, String path) throws IllegalAccessException, InvocationTargetException, InstantiationException, IllegalArgumentException {
		if(isValidField(field)) {
			BeanUtils.copyProperty(objectTarget, field.getName(), field.get(objectSource));
		
		} else if (isIncludedProperty(path, field)) {
			Object objectIncluded = copyClass(objectSource, field, path);
			BeanUtils.copyProperty(objectTarget, field.getName(), objectIncluded);
		}
	}

	private Object copyClass(Object objectSource, Field field, String path)	throws InstantiationException, IllegalAccessException, InvocationTargetException {
		String parentProperty = path + field.getName();
		return filter(field.get(objectSource), field.getType(), parentProperty);
	}


	private boolean isValidField(Field field) {
		return (!excludeProperties.contains(field.getName()) &&	isJavaNative(field.getType()));
	}

	private boolean isIncludedProperty(String path, Field field) {
		return includeProperties.contains(field.getName()) || includeProperties.contains(path+"."+field.getName());
	}
	
	private boolean isJavaNative(Class<?> propertyClass) {
		  return propertyClass.isPrimitive()
		    || String.class.equals(propertyClass)
		    || Number.class.isAssignableFrom(propertyClass)
		    || Boolean.class.equals(propertyClass)
		    || Date.class.isAssignableFrom(propertyClass)
		    || Calendar.class.isAssignableFrom(propertyClass)
		    || Character.class.equals(propertyClass)
		    || propertyClass.isEnum();
		 }

	public Result includePropety(String includeProperty) {
		this.includeProperties.add(includeProperty);
		return this;
	}
	
	public Result excludePropety(String excludeProperty) {
		this.excludeProperties.add(excludeProperty);
		return this;
	}
}

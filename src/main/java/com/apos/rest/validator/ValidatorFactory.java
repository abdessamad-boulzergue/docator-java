package com.apos.rest.validator;

import com.apos.models.Resource;

public class ValidatorFactory {

	public AposValidator<?> getValidator(Class<?> cls) {
		
		if(cls ==null)
			throw new IllegalArgumentException("class can not be null");
		
		if(cls.equals(Resource.class)) {
			return new ResourceValidator();
		}
		
		throw new IllegalArgumentException("no validator for " + cls);
		
		
	}
}

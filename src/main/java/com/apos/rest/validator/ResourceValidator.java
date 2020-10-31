package com.apos.rest.validator;

import com.apos.models.Resource;
import com.apos.rest.exceptions.AposValidationException;

public class ResourceValidator implements AposValidator<Resource>{

	@Override
	public void validate(Resource resource) throws AposValidationException {
		
		if(resource == null)
			throw new AposValidationException(Resource.class,"instance null");
		if(resource.getName() == null || resource.getName().trim().isEmpty())
			throw new AposValidationException(Resource.class , "invalide value for name : " +resource.getName());
		if(resource.getType() == null || resource.getType().getName().trim().isEmpty())
			throw new AposValidationException(Resource.class , "invalide value for type : " +resource.getType());

		
	}
	
	


}

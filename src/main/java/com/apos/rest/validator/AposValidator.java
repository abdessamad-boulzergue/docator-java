package com.apos.rest.validator;

import com.apos.rest.exceptions.AposValidationException;

public interface AposValidator<T> {
	public void validate(T obj) throws AposValidationException;
}

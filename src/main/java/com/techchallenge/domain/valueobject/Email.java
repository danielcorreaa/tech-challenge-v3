package com.techchallenge.domain.valueobject;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Email {

	private String value;
	private static final String regex = "^(.+)@(.+)$";
	public Email(String value) {
		
		this.value = validate(value);
	}

	private String validate(String value) {
	    if(Optional.ofNullable(value).orElse("").trim().isEmpty()) {
	    	return value;
	    }
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(value);
	    if(matcher.matches()) {
	    	return value;
	    }
	    throw new IllegalArgumentException("Invalid Email!");
	}

	public String getValue() {
		return value;
	}

}

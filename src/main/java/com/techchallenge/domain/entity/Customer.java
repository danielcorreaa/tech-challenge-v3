package com.techchallenge.domain.entity;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.techchallenge.domain.valueobject.Cpf;
import com.techchallenge.domain.valueobject.Email;

public class Customer {

	private Cpf cpf;
	private String name;	
	private Email email;

	public Customer(String cpf, String name, String email) {
		super();
		this.cpf = new Cpf(cpf);
		this.name = name;	
		this.email = new Email(email);
	}

	public Cpf getCpf() {
		return cpf;
	}
	
	public Optional<String> getCpfValue() {
		return Optional.ofNullable(cpf).map( cpf -> cpf.getValue());
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return Optional.ofNullable(email).map( email -> email.getValue()).orElse(null);
	}
	
	public String getFormatCpf() {
		Pattern pattern = Pattern.compile("(\\d{3})(\\d{3})(\\d{3})(\\d{2})");
		Matcher matcher = pattern.matcher(cpf.getValue());		
		if (matcher.matches()) 
			return  matcher.replaceAll("$1.$2.$3-$4");
		return cpf.getValue();
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpf);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return Objects.equals(cpf, other.cpf);
	}
	
	

}

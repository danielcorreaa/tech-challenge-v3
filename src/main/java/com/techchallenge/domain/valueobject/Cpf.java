package com.techchallenge.domain.valueobject;

import java.util.Optional;

public class Cpf {

	private String value;
	private static final int[] pesoCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

	public Cpf(String value) {		
		if (!isValid(Optional.ofNullable(value).orElse(""))) {
			throw new IllegalArgumentException("Invalid Cpf!");
		}
		this.value = removeEspecialCaracter(value);
	}

	private boolean isValid(String cpf) {
		cpf = removeEspecialCaracter(cpf);
		if ((cpf == null) || (cpf.length() != 11))
			return false;

		for (int j = 0; j < 10; j++)
			if (padLeft(Integer.toString(j), Character.forDigit(j, 10)).equals(cpf))
				return false;

		Integer digit1 = calculate(cpf.substring(0, 9), pesoCPF);
		Integer digit2 = calculate(cpf.substring(0, 9) + digit1, pesoCPF);
		return cpf.equals(cpf.substring(0, 9) + digit1.toString() + digit2.toString());
	}

	private String removeEspecialCaracter(String cpf) {
		cpf = cpf.trim().replace(".", "").replace("-", "");
		return cpf;
	}

	private static int calculate(String str, int[] weight) {
		int sum = 0;
		for (int indice = str.length() - 1, digit; indice >= 0; indice--) {
			digit = Integer.parseInt(str.substring(indice, indice + 1));
			sum += digit * weight[weight.length - str.length() + indice];
		}
		sum = 11 - sum % 11;
		return sum > 9 ? 0 : sum;
	}

	private static String padLeft(String text, char character) {
		return String.format("%11s", text).replace(' ', character);
	}

	public String getValue() {
		return value;
	}

}

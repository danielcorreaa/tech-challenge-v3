package com.techchallenge.config.infra;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class Result<T> {

	private int code;
	private String message;
	private T result;
	private List<String> errors;

	public Result(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public Result(int code, T result) {
		this.code = code;
		this.result = result;
	}

	public Result(int code, List<String> errors) {		
		this.code = code;
		this.errors = errors;
	}
	public Result() {	
	}

	public static <T> Result<T> ok(T result) {
		return new Result<>(HttpStatus.OK.value(), result);
	}

	public static <T> Result<T> create(T result) {
		return new Result<>(HttpStatus.CREATED.value(), result);
	}

	public static <T> Result<T> badRequest(List<String> errors) {
		return new Result<>(HttpStatus.BAD_REQUEST.value(), errors);
	}

	public static <T> Result<T> notFound(List<String> errors) {	
		return new Result<>(HttpStatus.NOT_FOUND.value(), errors);
	}

}

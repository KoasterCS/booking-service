package com.booking.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.booking.model.Response;

public class ResponseBuilder {
	
	private ResponseBuilder() {}

	public static<T> Response<T> getSuccessResponse(T data) {
		return getSuccessResponse(data, null);
	}

	public static<T> Response<T> getSuccessResponse(String message) {
		return getSuccessResponse(null, message);
	}

	public static<T> Response<T> getSuccessResponse(T data, String message) {
		Response<T> response = new Response<>();
		response.setData(data);

		if (message != null) {
			response.addMessage(message);
		}
		response.setStatus(HttpStatus.OK);
		return response;
	}
	
	public static<T> Response<T> getBadRequestResponse(String message) {
		return getBadRequestResponse(null, message);
	}

	public static<T> Response<T> getBadRequestResponse(T data, String message) {
		return getBadRequestResponse(data, Arrays.asList(message));
	}

	public static<T> Response<T> getBadRequestResponse(T data, List<String> messages) {
		return getResponse(data, messages, HttpStatus.BAD_REQUEST);
	}

	public static<T> Response<T> getErrorResponse(String message) {
		return getErrorResponse(null, message);
	}

	public static<T> Response<T> getErrorResponse(T data, String message) {
		return getErrorResponse(data, Arrays.asList(message));
	}

	public static<T> Response<T> getErrorResponse(T data, List<String> messages) {
		return getResponse(data, messages, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private static <T> Response<T> getResponse(T data, List<String> messages, HttpStatus status) {
		Response<T> response = new Response<>();
		if (data != null) {
			response.setData(data);
		}
		response.setMessages(messages);
		response.setIsSuccess(false);
		response.setStatus(status);
		return response;
	}
	
}


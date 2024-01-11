package com.booking.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Response<T> {

	private T data;
	private List<String> messages;
	private Boolean isSuccess = Boolean.TRUE;
	@JsonIgnore
	private HttpStatus status;

	public Response() {
	}

	public Response(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public void addMessage(String message) {
		if (messages == null) {
			messages = new ArrayList<>();
		}
		messages.add(message);
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public boolean hasMessages() {
		return messages != null && !CollectionUtils.isEmpty(messages);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}

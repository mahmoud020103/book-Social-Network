package com.mahmoud.book.email;

public enum EmailTemplateName {
	ACTIVATE_ACCOUNT("activate account");
	
	private final String name;
	private EmailTemplateName(String name) {
		// TODO Auto-generated constructor stub
		this.name=name;
	}
}

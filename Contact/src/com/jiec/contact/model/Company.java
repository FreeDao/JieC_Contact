package com.jiec.contact.model;

import java.util.List;

public class Company {
	int id;
	int name;
	List<Contact> contacts;
	
	public Company(int id, int name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Company(int id, int name, List<Contact> contacts) {
		super();
		this.id = id;
		this.name = name;
		this.contacts = contacts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	
	
	
}


package com.jiec.contact.model;

import java.util.ArrayList;
import java.util.List;

public class Company {
    String id;

    String name;

    List<Contact> contacts;

    public Company() {

    }

    public Company(String id, String name) {
        super();
        this.id = id;
        this.name = name;
        contacts = new ArrayList<Contact>();
    }

    public Company(String id, String name, List<Contact> contacts) {
        super();
        this.id = id;
        this.name = name;
        this.contacts = contacts;
        contacts = new ArrayList<Contact>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

}

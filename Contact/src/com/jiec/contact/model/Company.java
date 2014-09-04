
package com.jiec.contact.model;

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
    }

    public Company(String id, String name, List<Contact> contacts) {
        super();
        this.id = id;
        this.name = name;
        this.contacts = contacts;
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

package com.plivo.contactbook.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.plivo.contactbook.model.Contact;

@Service
public interface ContactService {
	
	Contact insertContact(Contact contact);
	
	List<Contact> getContacts();
	
	Contact updateContact(Long contactId, Contact contactDetails);

	ResponseEntity<?> findByIdAndDelete(Long contactId);

	List<Contact> getContactsByName(String name);

	List<Contact> getContactsByEmail(String email);

	List<Contact> getContactsByNameAndEmail(String name, String email);

}

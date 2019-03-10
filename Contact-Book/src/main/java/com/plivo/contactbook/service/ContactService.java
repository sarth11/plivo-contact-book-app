package com.plivo.contactbook.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.plivo.contactbook.model.Contact;

/**
 * Main Service(layer) interface that provide declarations for all the methods
 * @author sarthak
 *
 */
@Service
public interface ContactService {
	
	Contact insertContact(Contact contact);
	
	Page<Contact> getContacts(Pageable pageable);
	
	Contact updateContact(Long contactId, Contact contactDetails);

	ResponseEntity<?> findByIdAndDelete(Long contactId);

	Page<Contact> getContactsByName(String name, Pageable pageable);

	Page<Contact> getContactsByEmail(String email, Pageable pageable);

	Page<Contact> getContactsByNameAndEmail(String name, String email, Pageable pageable);

}

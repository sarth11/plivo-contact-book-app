package com.plivo.contactbook.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plivo.contactbook.model.Contact;
import com.plivo.contactbook.service.ContactService;

@RestController
@RequestMapping(path = "/api/contact-book")
public class ContactController {
	
	@Autowired
	private ContactService contactService;

	@RequestMapping(value = "/v1/contacts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Contact> getContacts() {
		return contactService.getContacts();
	}
	
	@RequestMapping(value = "/v1/getContactsByName", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Contact> getContactsByName(@Valid @RequestParam String name) {
		return contactService.getContactsByName(name);
	}
	
	@Cacheable(value = "contacts-email", key = "#email")
	@RequestMapping(value = "/v1/getContactsByEmail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Contact> getContactsByEmail(@Valid @RequestParam String email) {
		return contactService.getContactsByEmail(email);
	}
	
	@Cacheable(value = "contacts-NameAndEmail", key = "#email")
	@RequestMapping(value = "/v1/getContactsByNameAndEmail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Contact> getContactsByNameAndEmail(@Valid @RequestParam String name,@Valid @RequestParam String email) {
		return contactService.getContactsByNameAndEmail(name, email);
	}

	@RequestMapping(value = "/v1/insert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Contact insertContact(@Valid @RequestBody Contact contact) {
		return contactService.insertContact(contact);
	}

	//@CachePut(value = "contacts", key = "#id")
	@PutMapping("v1/contacts/{id}")
	public Contact updateContact(@PathVariable(value = "id") Long contactId,
			@Valid @RequestBody Contact contactDetails) {
		return contactService.updateContact(contactId, contactDetails);
	}
	
	@CacheEvict(value = "contacts", key = "#id")
	@DeleteMapping("/v1/contacts/{id}")
	public ResponseEntity<?> deleteContact(@PathVariable(value = "id") Long id) {
		return contactService.findByIdAndDelete(id);
	}

}

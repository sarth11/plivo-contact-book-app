package com.plivo.contactbook.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.plivo.contactbook.exceptions.EmailExistsException;
import com.plivo.contactbook.exceptions.FieldNotFoundException;
import com.plivo.contactbook.exceptions.ResourceNotFoundException;
import com.plivo.contactbook.model.Contact;
import com.plivo.contactbook.repository.ContactRepository;
import com.plivo.contactbook.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactRepository contactRepository;

	public ContactServiceImpl() {
		
	}

	@Override
	public Contact insertContact(Contact contact) {
		validateFields(contact);
		checkIfEmailExists(contact.getEmailId());
		return contactRepository.save(contact);
	}

	private void validateFields(Contact contact) {
		if (Objects.nonNull(contact.getName()) && Objects.nonNull(contact.getPhoneNumber())
				&& Objects.nonNull(contact.getEmailId()) && Objects.nonNull(contact.getCity())) {
			return;
		} else {
			throw new FieldNotFoundException("Contact");
		}

	}

	@Override
	public List<Contact> getContacts() {
		return contactRepository.findAll();
	}

	@Override
	public ResponseEntity<?> findByIdAndDelete(Long id) {
		Contact contact = getContact(id);
		contactRepository.delete(contact);
		return new ResponseEntity<>("Deletion Successfull!", HttpStatus.OK);
	}

	@Override
	public Contact updateContact(Long contactId, Contact contactDetails) {
		Contact contact = getContact(contactId);
		if (Objects.nonNull(contactDetails.getName())) {
			contact.setName(contactDetails.getName());
		}
		if (Objects.nonNull(contactDetails.getPhoneNumber())) {
			contact.setPhoneNumber(contactDetails.getPhoneNumber());
		}
		if (Objects.nonNull(contactDetails.getEmailId())) {
			checkIfEmailExists(contactDetails.getEmailId());
			contact.setEmailId(contactDetails.getEmailId());
		}
		if (Objects.nonNull(contactDetails.getCity())) {
			contact.setCity(contactDetails.getCity());
		}
		Contact updatedContact = contactRepository.save(contact);
		return updatedContact;
	}

	private Contact getContact(Long id) {
		Contact contact = contactRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));
		return contact;
	}

	@Override
	@Cacheable(cacheNames = {"contactname"}, key = "#name", unless = "#result == null || #result.isEmpty()")
	public List<Contact> getContactsByName(String name) {
		return contactRepository.findByName(name);
	}

	@Override
	public List<Contact> getContactsByEmail(String email) {
		return contactRepository.findByEmail(email);
	}

	@Override
	public List<Contact> getContactsByNameAndEmail(String name, String email) {
		return contactRepository.findByNameAndEmail(name, email);
	}

	private void checkIfEmailExists(String emailId) {
		List<Contact> contactList = contactRepository.findByEmail(emailId);
		if (contactList.size() != 0) {
			throw new EmailExistsException("Contact", "emailId", emailId);
		}
	}

}

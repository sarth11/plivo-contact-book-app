package com.plivo.contactbook.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.plivo.contactbook.constants.ContactBookConstants;
import com.plivo.contactbook.exceptions.EmailExistsException;
import com.plivo.contactbook.exceptions.FieldNotFoundException;
import com.plivo.contactbook.exceptions.PaginationException;
import com.plivo.contactbook.exceptions.ResourceNotFoundException;
import com.plivo.contactbook.model.Contact;
import com.plivo.contactbook.repository.ContactRepository;
import com.plivo.contactbook.response.PagingErrorResponse;
import com.plivo.contactbook.service.ContactService;

/**
 * Main ServiceImpl(layer) that holds all the business logic for the API's
 * @author sarthak
 *
 */
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
			throw new FieldNotFoundException(ContactBookConstants.CONTACT);
		}

	}

	@Override
	public Page<Contact> getContacts(Pageable pageable) {
		validatePageSize(pageable, 0, null, null);
		return contactRepository.findAll(pageable);
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
				.orElseThrow(() -> new ResourceNotFoundException(ContactBookConstants.CONTACT, "id", id));
		return contact;
	}

	@Override
	// @Cacheable(cacheNames = {"contactname"}, key = "#name", unless = "#result
	// == null || #result.isEmpty()")
	public Page<Contact> getContactsByName(String name, Pageable pageable) {
		validatePageSize(pageable, 1, name, null);
		return contactRepository.findByNamePageable(name, pageable);
	}

	@Override
	public Page<Contact> getContactsByEmail(String email, Pageable pageable) {
		validatePageSize(pageable, 2, null, email);
		return contactRepository.findByEmailPageable(email, pageable);
	}

	@Override
	public Page<Contact> getContactsByNameAndEmail(String name, String email, Pageable pageable) {
		validatePageSize(pageable, 3, name, email);
		return contactRepository.findByNameAndEmailPageable(name, email, pageable);
	}

	private void checkIfEmailExists(String emailId) {
		List<Contact> contactList = contactRepository.findByEmail(emailId);
		if (contactList.size() != 0) {
			throw new EmailExistsException(ContactBookConstants.CONTACT, "emailId", emailId);
		}
	}

	private void validatePageSize(Pageable pageable, int flag, String name, String email) {
		int pageSize = pageable.getPageSize();
		if (pageSize != 20) {
			if (pageSize > 10) {
				exceptionHandler(new Exception());
			}
		} else if (pageSize == 20) {
			if (flag == 0 && contactRepository.findAll().size() > 10) {
				exceptionHandler(new Exception());
			} else if (flag == 1 && contactRepository.findByName(name).size() > 10) {
				exceptionHandler(new Exception());
			} else if (flag == 2 && contactRepository.findByEmail(email).size() > 10) {
				exceptionHandler(new Exception());
			} else if (flag == 2 && contactRepository.findByNameAndEmail(name, email).size() > 10) {
				exceptionHandler(new Exception());
			}
		}
	}

	private void exceptionHandler(Exception ex) {
		PagingErrorResponse pagingErrorResponse = new PagingErrorResponse();
		pagingErrorResponse.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
		pagingErrorResponse.setMessage(ContactBookConstants.AT_A_TIME_MAX_10_CONTACTS_CAN_BE_SEEN);
		throw new PaginationException(pagingErrorResponse);
	}
}

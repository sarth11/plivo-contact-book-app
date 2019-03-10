package com.plivo.contactbook;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plivo.contactbook.constants.ContactBookConstants;
import com.plivo.contactbook.controller.ContactController;
import com.plivo.contactbook.model.Contact;
import com.plivo.contactbook.repository.ContactRepository;
import com.plivo.contactbook.service.ContactService;


/**
 * Test Controller class that handles the unit tests and integration test for the application
 * @author sarthak
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactBookApplicationTests {

	@Before
	public void setUp() {
	      mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	   }

	@Autowired
	WebApplicationContext webApplicationContext;
	
	@Autowired
	private ContactController contactController;
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private ContactRepository contactRepository;
	
	protected MockMvc mvc;
	
	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}
	
	/**
	 * Unit-test for getting contact list when it is empty
	 * @throws Exception
	 */
	
	@Test
	public void getEmptyContactList() throws Exception {
		String uri = ContactBookConstants.API_CONTACT_BOOK_V1_CONTACTS;
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Contact[] contactlist = mapFromJson(content, Contact[].class);
		assertTrue(contactlist.length == 0);
	}

	/**
	 * Unit-test for getting contact list when it is not empty(some record's exist in DB)
	 * @throws Exception
	 */
	
	@Test
	public void getContactList() throws Exception {
		String uri = ContactBookConstants.API_CONTACT_BOOK_V1_CONTACTS;
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Contact[] contactlist = mapFromJson(content, Contact[].class);
		assertTrue(contactlist.length > 0);
	}
	/**
	 * Unit-test for creating a contact with all valid fields(gives 200 status)
	 * @throws Exception
	 */
	@Test
	public void createContact() throws Exception {
		String uri = ContactBookConstants.API_CONTACT_BOOK_V1_INSERT;
		Contact contact = new Contact();
		contact.setName("Salman");
		contact.setPhoneNumber("9928041292");
		contact.setEmailId("unique987@hotmail.com");
		contact.setCity("Madras");
		String inputJson = mapToJson(contact);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	/**
	 * Unit-test for creating a contact with missing fields(gives 404 status)
	 * @throws Exception
	 */
	@Test
	public void createContactMissingFields() throws Exception {
		String uri = ContactBookConstants.API_CONTACT_BOOK_V1_INSERT;
		Contact contact = new Contact();
		contact.setName("Sahil");
		contact.setCity("Chennai");
		String inputJson = mapToJson(contact);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(404, status);
	}
	/**
	 * Unit-test for updating a contact thats exists with the given id in the DB(gives 200 status)
	 * @throws Exception
	 */
	@Test
	public void updateContact() throws Exception {
		String uri = ContactBookConstants.API_CONTACT_BOOK_V1_CONTACTS+"/7";
		Contact contact = new Contact();
		contact.setName("mirza");
		String inputJson = mapToJson(contact);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	/**
	 * Unit-test for updating a contact thats does not exist with the given id in the DB(gives 404 status)
	 * @throws Exception
	 */
	@Test
	public void updateContactIdNotExists() throws Exception {
		String uri = ContactBookConstants.API_CONTACT_BOOK_V1_CONTACTS+"/1";
		Contact contact = new Contact();
		contact.setName("mirza");
		String inputJson = mapToJson(contact);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(404, status);
	}
	/**
	 * Unit-test for deleting a contact that exists in the DB(gives 200 status) plus message is asserted as well
	 * @throws Exception
	 */
	@Test
	public void deleteContact() throws Exception {
		String uri = ContactBookConstants.API_CONTACT_BOOK_V1_CONTACTS+"/37";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, "Deletion Successfull!");
	}
	
	/**
	 * Unit-test for deleting a contact that does not exist in the DB(gives 404 status)
	 * @throws Exception
	 */
	@Test
	public void deleteContactIdNotExists() throws Exception {
		String uri = ContactBookConstants.API_CONTACT_BOOK_V1_CONTACTS+"/1";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(404, status);
	}
	/**
	 * Unit-test for searching a contact by name and email that exists in the DB(gives 200 status)
	 * @throws Exception
	 */
	@Test
	public void searchByNameAndEmail() throws Exception {
		String uri = ContactBookConstants.API_CONTACT_BOOK_V1_GET_CONTACTS_BY_NAME_AND_EMAIL+"?name=bishoo&email=s@gmail.com";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Contact[] contactlist = mapFromJson(content, Contact[].class);
		assertTrue(contactlist.length > 0);

	}
	
	/**
	 * Unit-test for searching a contact by name and email that does not exist in DB(gives 404 status)
	 * @throws Exception
	 */
	@Test
	public void searchByNameAndEmailNotExists() throws Exception {
		String uri = ContactBookConstants.API_CONTACT_BOOK_V1_GET_CONTACTS_BY_NAME_AND_EMAIL+"?name=bish&email=1@gmail.com";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(404, status);
		String content = mvcResult.getResponse().getContentAsString();
		Contact[] contactlist = mapFromJson(content, Contact[].class);
		assertTrue(contactlist.length == 0);

	}
	
	/**
	 * Unit-test for searching a contact by name that exists in the DB(gives 200 status)
	 * @throws Exception
	 */
	@Test
	public void searchByName() throws Exception {
		String uri = ContactBookConstants.API_CONTACT_BOOK_V1_GET_CONTACTS_BY_NAME+"?name=bmrah";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Contact[] contactlist = mapFromJson(content, Contact[].class);
		assertTrue(contactlist.length > 0);

	}
	

	/**
	 * Unit-test for searching a contact by name that does not exists in the DB(gives 404 status)
	 * @throws Exception
	 */
	@Test
	public void searchByNameNotExists() throws Exception {
		String uri = ContactBookConstants.API_CONTACT_BOOK_V1_GET_CONTACTS_BY_NAME+"?name=holebas";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(404, status);
		String content = mvcResult.getResponse().getContentAsString();
		Contact[] contactlist = mapFromJson(content, Contact[].class);
		assertTrue(contactlist.length == 0);

	}
	
	/**
	 * Unit-test for searching a contact by email that exists in the DB(gives 200 status)
	 * @throws Exception
	 */
	@Test
	public void searchByEmail() throws Exception {
		String uri = ContactBookConstants.API_CONTACT_BOOK_V1_GET_CONTACTS_BY_EMAIL+"?email=bumrah@gmail.com";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Contact[] contactlist = mapFromJson(content, Contact[].class);
		assertTrue(contactlist.length > 0);

	}
	
	/**
	 * Unit-test for searching a contact by email that does not exists in the DB(gives 404 status)
	 * @throws Exception
	 */
	@Test
	public void searchByEmailNotExists() throws Exception {
		String uri = ContactBookConstants.API_CONTACT_BOOK_V1_GET_CONTACTS_BY_EMAIL+"?email=holebas@yahoo.com";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(404, status);
		String content = mvcResult.getResponse().getContentAsString();
		Contact[] contactlist = mapFromJson(content, Contact[].class);
		assertTrue(contactlist.length == 0);

	}
	
	/**
	 * Integration test for all the layers of the application
	 */
	
	@Test
	  public void layersIntegrationTest() {
	    assertThat(contactController).isNotNull();
	    assertThat(contactService).isNotNull();
	    assertThat(contactRepository).isNotNull();
	  }

}

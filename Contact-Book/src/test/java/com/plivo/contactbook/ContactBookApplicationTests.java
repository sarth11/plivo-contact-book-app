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
import com.plivo.contactbook.controller.ContactController;
import com.plivo.contactbook.model.Contact;
import com.plivo.contactbook.repository.ContactRepository;
import com.plivo.contactbook.service.ContactService;

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
	 * Unit-test for getting contact list
	 * @throws Exception
	 */
	
	@Test
	public void getContactList() throws Exception {
		String uri = "/api/contact-book/v1/contacts";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Contact[] contactlist = mapFromJson(content, Contact[].class);
		assertTrue(contactlist.length > 0);
	}
	/**
	 * Unit-test for creating a contact
	 * @throws Exception
	 */
	@Test
	public void createContact() throws Exception {
		String uri = "/api/contact-book/v1/insert";
		Contact contact = new Contact();
		contact.setName("Salman");
		contact.setPhoneNumber("9928041292");
		contact.setEmailId("unique987@yahoo.com");
		contact.setCity("Madras");
		String inputJson = mapToJson(contact);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	/**
	 * Unit-test for updating a contact
	 * @throws Exception
	 */
	@Test
	public void updateContact() throws Exception {
		String uri = "/api/contact-book/v1/contacts/13";
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
	 * Unit-test for deleting a contact
	 * @throws Exception
	 */
	@Test
	public void deleteContact() throws Exception {
		String uri = "/api/contact-book/v1/contacts/14";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, "Deletion Successfull!");
	}
	/**
	 * Unit-test for searching a contact by name and email
	 * @throws Exception
	 */
	@Test
	public void searchByNameAndEmail() throws Exception {
		String uri = "/api/contact-book/v1/getContactsByNameAndEmail?name=shetty&email=anuj987@gmail.com";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Contact[] contactlist = mapFromJson(content, Contact[].class);
		assertTrue(contactlist.length > 0);

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

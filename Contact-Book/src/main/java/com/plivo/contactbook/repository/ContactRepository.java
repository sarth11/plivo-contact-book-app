package com.plivo.contactbook.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.plivo.contactbook.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
	
	@Query(value="SELECT * from contacts where name = ?",nativeQuery=true) 
	public List<Contact> findByName(String name);
	
	@Query(value="SELECT * from contacts where email_id = ?",nativeQuery=true) 
	public List<Contact> findByEmail(String email);
	
	@Query(value="SELECT * from contacts where name = ? AND email_id = ?",nativeQuery=true) 
	public List<Contact> findByNameAndEmail(String name,String email);

}

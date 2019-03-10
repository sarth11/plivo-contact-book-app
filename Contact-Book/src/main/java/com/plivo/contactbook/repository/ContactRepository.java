package com.plivo.contactbook.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.plivo.contactbook.model.Contact;

/**
 * Main repository class to fetch out data from the DB
 * @author sarthak
 *
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
	
	@Query(value="SELECT * from contacts where name = ?",nativeQuery=true) 
	public Page<Contact> findByNamePageable(String name, Pageable page);
	
	@Query(value="SELECT * from contacts where name = ? AND email_id = ?",nativeQuery=true) 
	public Page<Contact> findByNameAndEmailPageable(String name,String email, Pageable page);
	
	@Query(value="SELECT * from contacts where email_id = ?",nativeQuery=true) 
	public Page<Contact> findByEmailPageable(String email, Pageable page);
	
	@Query(value="SELECT * from contacts where name = ?",nativeQuery=true) 
	public List<Contact> findByName(String name);
	
	@Query(value="SELECT * from contacts where name = ? AND email_id = ?",nativeQuery=true) 
	public List<Contact> findByNameAndEmail(String name,String email);
	
	@Query(value="SELECT * from contacts where email_id = ?",nativeQuery=true) 
	public List<Contact> findByEmail(String email);

}

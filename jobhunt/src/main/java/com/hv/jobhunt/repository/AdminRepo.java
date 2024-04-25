package com.hv.jobhunt.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hv.jobhunt.Models.Admin;
import com.hv.jobhunt.Models.Employeer;

public interface AdminRepo extends JpaRepository<Admin, Integer> {
	
	
	Admin findByEmailIdAndPassword(String emailId, String password);

	
	
	

}

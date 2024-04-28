package com.hv.jobhunt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hv.jobhunt.Models.AppliedJobs;
import com.hv.jobhunt.Models.Employeer;

public interface EmployeerRepo extends JpaRepository<Employeer,Integer>{
	
	 Employeer findByEmailIdAndPassword(String emailId, String password);
	 
	 Employeer findByEmployeeId(int EmployeeId);
	 
	 String deleteByEmployeeId(int emp_id);
	 
	 Employeer findByEmailId(String status);
	 
	 Employeer findBySubscriptionType(String employeerMail);
	 
	 boolean existsByEmailId(String emailId);
	 
	 
	 

}

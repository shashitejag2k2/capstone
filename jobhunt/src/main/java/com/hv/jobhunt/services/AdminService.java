package com.hv.jobhunt.services;

import java.util.List;

import com.hv.jobhunt.Models.Admin;
import com.hv.jobhunt.Models.Employeer;
import com.hv.jobhunt.Models.Subscription;

public interface AdminService {
	String login(Admin admin);
	List<Employeer> getEmployeers();
	Employeer getIndividualEmployee(int emp_id);
	String deleteEmployee(int emp_id);
	String updateStatus(String status,String email);
	
	List<Subscription> getSubscription();
	
	String subscriptionUpdate(Subscription subscription);
	
	String deleteSubscription(Long id);
	
	String createSubscription(Subscription subscription);

}

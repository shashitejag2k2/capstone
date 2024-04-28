package com.hv.jobhunt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hv.jobhunt.Models.Subscription;

public interface SubscriptionRepo extends JpaRepository<Subscription, Integer> {
	
	Subscription findById(Long id);

	void deleteById(Long id);
	
	Subscription findNoOfJobsBySubscriptionType(String Subscription);

	Subscription findBySubscriptionType(String subscriptionType);
	
	

}

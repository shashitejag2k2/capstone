package com.hv.jobhunt.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.hv.jobhunt.Models.Admin;
import com.hv.jobhunt.Models.Employeer;
import com.hv.jobhunt.Models.Subscription;
import com.hv.jobhunt.controllers.AdminController;
import com.hv.jobhunt.repository.AdminRepo;
import com.hv.jobhunt.repository.EmployeerRepo;
import com.hv.jobhunt.repository.SubscriptionRepo;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

	@InjectMocks
	private AdminServiceImpl adminServiceImpl;

	@Mock
	private EmployeerRepo employeerRepo;

	@Mock
	private AdminRepo adminRepo;

	@Mock
	private SubscriptionRepo subscriptionRepo;

	@Test
	public void login_Success() {

		String email = "admin@example.com";
		String password = "admin123";
		Admin admin = new Admin();
		admin.setEmailId(email);
		admin.setPassword(password);

		Mockito.when(adminRepo.findByEmailIdAndPassword(email, password)).thenReturn(admin);

		String result = adminServiceImpl.login(admin);

		assertEquals("Login successful", result);
	}

	@Test
	public void login_InvalidCredentials() {

		String email = "admin@example.com";
		String password = "admin123";
		Admin admin = new Admin();
		admin.setEmailId(email);
		admin.setPassword(password);

		Mockito.when(adminRepo.findByEmailIdAndPassword(email, password)).thenReturn(null);

		String result = adminServiceImpl.login(admin);

		assertEquals("Invalid credentials", result);
	}

	@Test
	public void login_Exception() {

		String email = "admin@example.com";
		String password = "admin123";
		Admin admin = new Admin();
		admin.setEmailId(email);
		admin.setPassword(password);

		Mockito.when(adminRepo.findByEmailIdAndPassword(email, password))
				.thenThrow(new RuntimeException("Database connection failed"));

		assertThrows(RuntimeException.class, () -> adminServiceImpl.login(admin));
	}

	@Test
	public void testGetEmployeersSuccess() {

		List<Employeer> employees = new ArrayList<>();
		Employeer employee1 = new Employeer();
		employee1.setEmployeeId(1);
		employee1.setName("shashi teja");
		employee1.setEmailId("shashi@email.com");
		employee1.setPassword("password123");
		employee1.setCompanyName("Hitachi Company");
		employee1.setSubscriptionType("Premium");
		employee1.setSubscriptionExprirationDate(new Date());
		employee1.setStatus("Active");
		employees.add(employee1);

		Employeer employee2 = new Employeer();
		employee2.setEmployeeId(2);
		employee2.setName("Jane");
		employee2.setEmailId("jane@email.com");
		employee2.setPassword("password456");
		employee2.setCompanyName("Indian Corporation");
		employee2.setSubscriptionType("Basic");
		employee2.setSubscriptionExprirationDate(new Date());
		employee2.setStatus("Inactive");
		employees.add(employee2);
		when(employeerRepo.findAll()).thenReturn(employees);

		List<Employeer> result = adminServiceImpl.getEmployeers();

		assertEquals(employees, result);
	}

	@Test
	public void testGetEmployeers_NoEmployees_ThrowsRuntimeException() {
	   
	    when(employeerRepo.findAll()).thenReturn(Collections.emptyList());

	    assertThrows(RuntimeException.class, () -> adminServiceImpl.getEmployeers());
	}

	@Test
	public void testGetEmployeers_DatabaseError_ThrowsRuntimeException() {
	    
	    when(employeerRepo.findAll()).thenThrow(new RuntimeException("Database error"));

	   
	    assertThrows(RuntimeException.class, () -> adminServiceImpl.getEmployeers());
	}

	@Test
	public void testGetIndividualEmployeeNotFound() {

		int employeeId = 123;
		Employeer employee = new Employeer();
		employee.setEmployeeId(employeeId);
		employee.setName("shashi teja");
		employee.setEmailId("shashi@email.com");
		employee.setPassword("password123");
		employee.setCompanyName("Hiatchi Company");
		employee.setSubscriptionType("Premium");
		employee.setSubscriptionExprirationDate(new Date());
		employee.setStatus("Active");

		when(employeerRepo.findByEmployeeId(employeeId)).thenReturn(employee);

		Employeer actualEmployeer = adminServiceImpl.getIndividualEmployee(employeeId);

		assertEquals(employee, actualEmployeer);
	}

	@Test
	public void testGetIndividualEmployeeFailure() {

		int employeeId = 123;

		when(employeerRepo.findByEmployeeId(employeeId)).thenThrow(new RuntimeException("Error fetching employee"));

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			adminServiceImpl.getIndividualEmployee(employeeId);
		});
		assertEquals("Failed to retrieve individual employee: Error fetching employee", exception.getMessage());
	}

	@Test
	public void testDeleteEmployeeSuccess() {

		Employeer employee = new Employeer();
		employee.setEmployeeId(1);
		employee.setName("Test Employee");
		employee.setEmailId("test@example.com");
		employee.setPassword("testpassword");
		employee.setCompanyName("Test Company");
		employee.setSubscriptionType("Test Subscription");
		employee.setSubscriptionExprirationDate(new Date());
		employee.setStatus("active");
		employeerRepo.save(employee);

		String result = adminServiceImpl.deleteEmployee(employee.getEmployeeId());

		Employeer deletedEmployee = employeerRepo.findByEmployeeId(employee.getEmployeeId());
		assertNull(deletedEmployee);

		assertEquals("Deleted employeer", result);
	}

	@Test
	public void testDeleteEmployeeNotFound() {

		String result = adminServiceImpl.deleteEmployee(999);

		assertEquals("Failed to delete employeer: Employee not found", result);
	}

	@Test
	public void testDeleteEmployeeException() {

		Employeer employee = new Employeer();
		employee.setName("Test Employee");
		employee.setEmailId("test@example.com");
		employee.setPassword("testpassword");
		employee.setCompanyName("Test Company");
		employee.setSubscriptionType("Test Subscription");
		employee.setSubscriptionExprirationDate(new Date());
		employee.setStatus("active");

		try {
			adminServiceImpl.deleteEmployee(employee.getEmployeeId());
			fail("Expected an exception to be thrown");
		} catch (RuntimeException e) {

			assertEquals("Failed to delete employeer: ", e.getMessage());
		}
	}

	@Test
	public void testUpdateStatusSuccess() {

		Employeer employee = new Employeer();
		employee.setEmailId("test@example.com");
		employee.setStatus("approve");
		when(employeerRepo.findByEmailId(employee.getEmailId())).thenReturn(employee);

		String result = adminServiceImpl.updateStatus("reject", employee.getEmailId());

		Employeer updatedEmployee = employeerRepo.findByEmailId(employee.getEmailId());
		assertNotNull(updatedEmployee);
		assertEquals("reject", updatedEmployee.getStatus());

		assertEquals("Status Updated", result);
	}

	@Test
    public void testUpdateStatusNotFound() {
    	 
        when(employeerRepo.findByEmailId("nonexistent@example.com")).thenReturn(null);

        
        String result = null;
        try {
            result = adminServiceImpl.updateStatus("inactive", "nonexistent@example.com");
        } catch (RuntimeException e) {
            
            assertEquals("Employeer not found with email: nonexistent@example.com", e.getMessage());
        }

        
        assertNull(result);
    }

	@Test
	public void testUpdateStatusException() {

		Employeer employee = new Employeer();
		employee.setEmailId(null);
		employee.setStatus("active");

		try {
			adminServiceImpl.updateStatus(employee.getStatus(), employee.getEmailId());
			fail("Expected an exception to be thrown");
		} catch (RuntimeException e) {

			assertEquals("Failed to update status: Email ID cannot be null or empty", e.getMessage());
		}
	}

	@Test
	public void testGetSubscriptionSuccess() {

		Subscription subscription1 = new Subscription();
		subscription1.setSubscriptionType("Basic");
		subscription1.setNumberOfJobs(10);

		subscription1.setPrice(9.99);
		subscriptionRepo.save(subscription1);

		Subscription subscription2 = new Subscription();
		subscription2.setSubscriptionType("Premium");
		subscription2.setNumberOfJobs(50);

		subscription2.setPrice(29.99);
		subscriptionRepo.save(subscription2);

		List<Subscription> subscriptions = adminServiceImpl.getSubscription();

		assertEquals(2, subscriptions.size());
		assertTrue(subscriptions.contains(subscription1));
		assertTrue(subscriptions.contains(subscription2));
	}

	@Test
	public void testGetSubscriptionEmpty() {

		List<Subscription> subscriptions = adminServiceImpl.getSubscription();

		assertTrue(subscriptions.isEmpty());
	}

	@Test
	public void testGetSubscriptionException() {

		doThrow(new RuntimeException("Test exception")).when(subscriptionRepo).findAll();

		try {
			adminServiceImpl.getSubscription();
			fail("Expected an exception to be thrown");
		} catch (RuntimeException e) {

			assertEquals("Test exception", e.getMessage());
		}

	}

	@Test
	public void testSubscriptionUpdateSuccess() {

		Subscription subscription = new Subscription();
		subscription.setId(1L);
		subscription.setSubscriptionType("Premium");
		subscription.setNumberOfJobs(10);
		subscription.setPrice(100.0);

		Subscription existingSubscription = new Subscription();
		existingSubscription.setId(1L);
		existingSubscription.setSubscriptionType("Basic");
		existingSubscription.setNumberOfJobs(5);
		existingSubscription.setPrice(50.0);
		when(subscriptionRepo.findById(1L)).thenReturn(existingSubscription);

		String result = adminServiceImpl.subscriptionUpdate(subscription);

		verify(subscriptionRepo).save(existingSubscription);
		assertEquals("Saved successfully", result);
	}

	@Test
	public void testSubscriptionUpdateNotFound() {

		Subscription subscription = new Subscription();
		subscription.setId(-1L);
		subscription.setSubscriptionType("Basic");
		subscription.setNumberOfJobs(10);

		subscription.setPrice(9.99);

		String result = adminServiceImpl.subscriptionUpdate(subscription);

		assertEquals("Not saved", result);
	}

	@Test
	public void testSubscriptionUpdateNotFoundInDatabase() {

		Subscription subscription = new Subscription();
		subscription.setId(1L);
		subscription.setSubscriptionType("Basic");
		subscription.setNumberOfJobs(10);

		subscription.setPrice(9.99);

		String result = adminServiceImpl.subscriptionUpdate(subscription);

		assertEquals("Not saved", result);
	}

	@Test
	public void testSubscriptionUpdateException() {

		Subscription subscription = new Subscription();
		subscription.setId(null);
		subscription.setSubscriptionType("Basic");
		subscription.setNumberOfJobs(10);

		subscription.setPrice(9.99);

		try {
			adminServiceImpl.subscriptionUpdate(subscription);
			fail("Expected an exception to be thrown");
		} catch (RuntimeException e) {

			assertEquals("Failed to update subscription: ", e.getMessage());
		}
	}

	@Test
	public void testDeleteSubscriptionSuccess() {

		Subscription subscription = new Subscription();
		subscription.setSubscriptionType("Basic");
		subscription.setNumberOfJobs(10);

		subscription.setPrice(9.99);
		subscriptionRepo.save(subscription);

		String result = adminServiceImpl.deleteSubscription(subscription.getId());

		assertNull(subscriptionRepo.findById(subscription.getId()));

		assertEquals("Deleted Successfully", result);
	}

	@Test
	public void testDeleteSubscriptionNotFound() {

		String result = adminServiceImpl.deleteSubscription(-1L);

		assertEquals("Subscription not found", result);
	}

	@Test
	public void testDeleteSubscriptionException() {

		Subscription subscription = new Subscription();
		subscription.setId(null);
		subscription.setSubscriptionType("Basic");
		subscription.setNumberOfJobs(10);

		subscription.setPrice(9.99);

		try {
			adminServiceImpl.deleteSubscription(subscription.getId());
			fail("Expected an exception to be thrown");
		} catch (RuntimeException e) {

			assertEquals("Failed to delete subscription: ", e.getMessage());
		}
	}

	@Test
	public void testCreateSubscriptionSuccess() {

		Subscription subscription = new Subscription();
		subscription.setSubscriptionType("Basic");
		subscription.setNumberOfJobs(10);

		subscription.setPrice(9.99);

		when(subscriptionRepo.save(subscription)).thenReturn(subscription);

		String result = adminServiceImpl.createSubscription(subscription);

		assertEquals("Subscription Created", result);
	}

	@Test
	public void testCreateSubscriptionException() {

		Subscription subscription = new Subscription();
		subscription.setSubscriptionType(null);
		subscription.setNumberOfJobs(10);

		subscription.setPrice(9.99);

		try {
			adminServiceImpl.createSubscription(subscription);
			fail("Expected an exception to be thrown");
		} catch (RuntimeException e) {

			assertEquals("Failed to create subscription: Subscription object is null", e.getMessage());
		}
	}

}

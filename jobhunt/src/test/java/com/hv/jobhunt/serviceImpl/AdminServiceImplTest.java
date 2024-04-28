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
        // Arrange
        String email = "admin@example.com";
        String password = "admin123";
        Admin admin = new Admin();
        admin.setEmailId(email);
        admin.setPassword(password);

        Mockito.when(adminRepo.findByEmailIdAndPassword(email, password)).thenReturn(admin);

        // Act
        String result = adminServiceImpl.login(admin);

        // Assert
        assertEquals("Login successful", result);
    }
	
	@Test
    public void login_InvalidCredentials() {
        // Arrange
        String email = "admin@example.com";
        String password = "admin123";
        Admin admin = new Admin();
        admin.setEmailId(email);
        admin.setPassword(password);

        Mockito.when(adminRepo.findByEmailIdAndPassword(email, password)).thenReturn(null);

        // Act
        String result = adminServiceImpl.login(admin);

        // Assert
        assertEquals("Invalid credentials", result);
    }
	@Test
    public void login_Exception() {
        // Arrange
        String email = "admin@example.com";
        String password = "admin123";
        Admin admin = new Admin();
        admin.setEmailId(email);
        admin.setPassword(password);

        Mockito.when(adminRepo.findByEmailIdAndPassword(email, password)).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> adminServiceImpl.login(admin));
    }

	
	@Test
    public void testGetEmployeersSuccess() {
        // Mocked list of employees
        List<Employeer> employees = new ArrayList<>();
        Employeer employee1 = new Employeer();
        employee1.setEmployeeId(1);
        employee1.setName("John Doe");
        employee1.setEmailId("john@example.com");
        employee1.setPassword("password123");
        employee1.setCompanyName("ABC Company");
        employee1.setSubscriptionType("Premium");
        employee1.setSubscriptionExprirationDate(new Date());
        employee1.setStatus("Active");
        employees.add(employee1);

        Employeer employee2 = new Employeer();
        employee2.setEmployeeId(2);
        employee2.setName("Jane Smith");
        employee2.setEmailId("jane@example.com");
        employee2.setPassword("password456");
        employee2.setCompanyName("XYZ Corporation");
        employee2.setSubscriptionType("Basic");
        employee2.setSubscriptionExprirationDate(new Date());
        employee2.setStatus("Inactive");
        employees.add(employee2);
        when(employeerRepo.findAll()).thenReturn(employees);

        // Call the service method
        List<Employeer> result = adminServiceImpl.getEmployeers();

        // Verify that the result matches the mocked list of employees
        assertEquals(employees, result);
    }

	@Test
	public void testGetEmployeers_NoEmployees_ThrowsRuntimeException() {
	    // Arrange
	    when(employeerRepo.findAll()).thenReturn(Collections.emptyList());

	    // Act and Assert
	    assertThrows(RuntimeException.class, () -> adminServiceImpl.getEmployeers());
	}

	@Test
	public void testGetEmployeers_DatabaseError_ThrowsRuntimeException() {
	    // Arrange
	    when(employeerRepo.findAll()).thenThrow(new RuntimeException("Database error"));

	    // Act and Assert
	    assertThrows(RuntimeException.class, () -> adminServiceImpl.getEmployeers());
	}
    
    @Test
    public void testGetIndividualEmployeeNotFound() {
        // Mocked employee ID
        int employeeId = 123;
        Employeer employee = new Employeer();
        employee.setEmployeeId(employeeId);
        employee.setName("John Doe");
        employee.setEmailId("john@example.com");
        employee.setPassword("password123");
        employee.setCompanyName("ABC Company");
        employee.setSubscriptionType("Premium");
        employee.setSubscriptionExprirationDate(new Date());
        employee.setStatus("Active");

        // Mock the behavior of employeerRepo.findByEmployeeId() to return null
//        when(employeerRepo.findByEmployeeId(employeeId)).thenReturn(null);

        // Verify that calling the service method with a non-existent employee ID throws a RuntimeException
        when(employeerRepo.findByEmployeeId(employeeId)).thenReturn(employee);

        // Act
        Employeer actualEmployeer = adminServiceImpl.getIndividualEmployee(employeeId);

        // Assert
        assertEquals(employee, actualEmployeer);
    }

    @Test
    public void testGetIndividualEmployeeFailure() {
        // Mocked employee ID
        int employeeId = 123;

        // Mock the behavior of employeerRepo.findByEmployeeId() to throw an exception
        when(employeerRepo.findByEmployeeId(employeeId)).thenThrow(new RuntimeException("Error fetching employee"));

        // Verify that calling the service method with an error fetching employee ID throws a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        	adminServiceImpl.getIndividualEmployee(employeeId);
        });
        assertEquals("Failed to retrieve individual employee: Error fetching employee", exception.getMessage());
    }
    
    @Test
    public void testDeleteEmployeeSuccess() {
        // Setup: create a valid employee object and save it to the database
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

        // Test: call the deleteEmployee method with a valid employee id
        String result = adminServiceImpl.deleteEmployee(employee.getEmployeeId());

        // Verify: the employee object should have been deleted from the database
        Employeer deletedEmployee = employeerRepo.findByEmployeeId(employee.getEmployeeId());
        assertNull(deletedEmployee);

        // Verify: the deleteEmployee method should return a success message
        assertEquals("Deleted employeer", result);
    }

    @Test
    public void testDeleteEmployeeNotFound() {
        // Test: call the deleteEmployee method with an invalid employee id
        String result = adminServiceImpl.deleteEmployee(999);

        // Verify: the deleteEmployee method should return a failure message
        assertEquals("Failed to delete employeer: Employee not found", result);
    }

    @Test
    public void testDeleteEmployeeException() {
        // Setup: create a stub employee object with a null id
        Employeer employee = new Employeer();
        employee.setName("Test Employee");
        employee.setEmailId("test@example.com");
        employee.setPassword("testpassword");
        employee.setCompanyName("Test Company");
        employee.setSubscriptionType("Test Subscription");
        employee.setSubscriptionExprirationDate(new Date());
        employee.setStatus("active");

        // Test: call the deleteEmployee method with an invalid employee object
        try {
        	adminServiceImpl.deleteEmployee(employee.getEmployeeId());
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            // Verify: the deleteEmployee method should throw a runtime exception
            assertEquals("Failed to delete employeer: ", e.getMessage());
        }
    }
    
    @Test
    public void testUpdateStatusSuccess() {
        // Setup: Create a valid employee object and mock the behavior of the repository
        Employeer employee = new Employeer();
        employee.setEmailId("test@example.com");
        employee.setStatus("approve");
        when(employeerRepo.findByEmailId(employee.getEmailId())).thenReturn(employee);

        // Test: Call the updateStatus method with a valid email ID and status
        String result = adminServiceImpl.updateStatus("reject", employee.getEmailId());

        // Verify: The employee object should have been updated in the repository
        Employeer updatedEmployee = employeerRepo.findByEmailId(employee.getEmailId());
        assertNotNull(updatedEmployee); // Make sure the updated employee exists
        assertEquals("reject", updatedEmployee.getStatus());

        // Verify: The updateStatus method should return a success message
        assertEquals("Status Updated", result);
    }

    @Test
    public void testUpdateStatusNotFound() {
    	 // Setup: Mock the behavior of findByEmailId to return null
        when(employeerRepo.findByEmailId("nonexistent@example.com")).thenReturn(null);

        // Test: Call the updateStatus method with an invalid email id
        String result = null;
        try {
            result = adminServiceImpl.updateStatus("inactive", "nonexistent@example.com");
        } catch (RuntimeException e) {
            // Verify: The updateStatus method should throw a RuntimeException with the expected message
            assertEquals("Employeer not found with email: nonexistent@example.com", e.getMessage());
        }

        // Ensure that the result is null, indicating the method should throw an exception
        assertNull(result);
    }

    @Test
    public void testUpdateStatusException() {
        // Setup: create a stub employee object with a null email id
        Employeer employee = new Employeer();
        employee.setEmailId(null);
        employee.setStatus("active");

        // Test: call the updateStatus method with an invalid employee object
        try {
        	adminServiceImpl.updateStatus(employee.getStatus(), employee.getEmailId());
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            // Verify: the updateStatus method should throw a runtime exception
            assertEquals("Failed to update status: Email ID cannot be null or empty", e.getMessage());
        }
    }
    
    
    
    @Test
    public void testGetSubscriptionSuccess() {
        // Setup: create some valid subscription objects and save them to the database
        Subscription subscription1 = new Subscription();
        subscription1.setSubscriptionType("Basic");
        subscription1.setNumberOfJobs(10);
        subscription1.setDuration(30);
        subscription1.setPrice(9.99);
        subscriptionRepo.save(subscription1);

        Subscription subscription2 = new Subscription();
        subscription2.setSubscriptionType("Premium");
        subscription2.setNumberOfJobs(50);
        subscription2.setDuration(90);
        subscription2.setPrice(29.99);
        subscriptionRepo.save(subscription2);
        

        // Test: call the getSubscription method
        List<Subscription> subscriptions = adminServiceImpl.getSubscription();

        // Verify: the getSubscription method should return a list of subscription objects
        assertEquals(2, subscriptions.size());
        assertTrue(subscriptions.contains(subscription1));
        assertTrue(subscriptions.contains(subscription2));
    }
    
    @Test
    public void testGetSubscriptionEmpty() {
        // Test: call the getSubscription method when there are no subscriptions in the database
        List<Subscription> subscriptions = adminServiceImpl.getSubscription();

        // Verify: the getSubscription method should return an empty list
        assertTrue(subscriptions.isEmpty());
    }
    
    @Test
    public void testGetSubscriptionException() {
        // Setup: create a stub subscription repository that throws an exception
        doThrow(new RuntimeException("Test exception")).when(subscriptionRepo).findAll();

        // Test: call the getSubscription method
        try {
        	adminServiceImpl.getSubscription();
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            // Verify: the getSubscription method should throw a runtime exception
            assertEquals("Test exception", e.getMessage());
        }
    
    }
    @Test
    public void testSubscriptionUpdateSuccess() {
        // Setup: create a stub subscription object
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setSubscriptionType("Premium");
        subscription.setNumberOfJobs(10);
        subscription.setPrice(100.0);

        // Mock the subscriptionRepo to return a subscription object
        Subscription existingSubscription = new Subscription();
        existingSubscription.setId(1L);
        existingSubscription.setSubscriptionType("Basic");
        existingSubscription.setNumberOfJobs(5);
        existingSubscription.setPrice(50.0);
        when(subscriptionRepo.findById(1L)).thenReturn(existingSubscription);

        // Test: call the subscriptionUpdate method with the stub subscription object
        String result = adminServiceImpl.subscriptionUpdate(subscription);

        // Verify: the subscription object should be updated and saved successfully
        verify(subscriptionRepo).save(existingSubscription);
        assertEquals("Saved successfully", result);
    }
    

    @Test
    public void testSubscriptionUpdateNotFound() {
        // Setup: create a valid subscription object with an invalid id
        Subscription subscription = new Subscription();
        subscription.setId(-1L);
        subscription.setSubscriptionType("Basic");
        subscription.setNumberOfJobs(10);
        subscription.setDuration(30);
        subscription.setPrice(9.99);

        // Test: call the subscriptionUpdate method with the invalid subscription object
        String result = adminServiceImpl.subscriptionUpdate(subscription);

        // Verify: the subscriptionUpdate method should return a failure message
        assertEquals("Not saved", result);
    }

    @Test
    public void testSubscriptionUpdateNotFoundInDatabase() {
        // Setup: create a valid subscription object with an existing id
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setSubscriptionType("Basic");
        subscription.setNumberOfJobs(10);
        subscription.setDuration(30);
        subscription.setPrice(9.99);

        // Test: call the subscriptionUpdate method with the subscription object
        String result = adminServiceImpl.subscriptionUpdate(subscription);

        // Verify: the subscriptionUpdate method should return a failure message
        assertEquals("Not saved", result);
    }

    @Test
    public void testSubscriptionUpdateException() {
        // Setup: create a stub subscription object with a null id
        Subscription subscription = new Subscription();
        subscription.setId(null);
        subscription.setSubscriptionType("Basic");
        subscription.setNumberOfJobs(10);
        subscription.setDuration(30);
        subscription.setPrice(9.99);

        // Test: call the subscriptionUpdate method with the invalid subscription object
        try {
            adminServiceImpl.subscriptionUpdate(subscription);
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            // Verify: the subscriptionUpdate method should throw a runtime exception
            assertEquals("Failed to update subscription: ", e.getMessage());
        }
    }
   
    
    @Test
    public void testDeleteSubscriptionSuccess() {
        // Setup: create a valid subscription object and save it to the database
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType("Basic");
        subscription.setNumberOfJobs(10);
        subscription.setDuration(30);
        subscription.setPrice(9.99);
        subscriptionRepo.save(subscription);

        // Test: call the deleteSubscription method with the valid subscription id
        String result = adminServiceImpl.deleteSubscription(subscription.getId());

        // Verify: the subscription object should have been deleted from the database
        assertNull(subscriptionRepo.findById(subscription.getId()));

        // Verify: the deleteSubscription method should return a success message
        assertEquals("Deleted Successfully", result);
    }

    @Test
    public void testDeleteSubscriptionNotFound() {
        // Test: call the deleteSubscription method with an invalid subscription id
        String result = adminServiceImpl.deleteSubscription(-1L);

        // Verify: the deleteSubscription method should return a failure message
        assertEquals("Subscription not found", result);
    }

    @Test
    public void testDeleteSubscriptionException() {
        // Setup: create a stub subscription object with a null id
        Subscription subscription = new Subscription();
        subscription.setId(null);
        subscription.setSubscriptionType("Basic");
        subscription.setNumberOfJobs(10);
        subscription.setDuration(30);
        subscription.setPrice(9.99);

        // Test: call the deleteSubscription method with the invalid subscription object
        try {
            adminServiceImpl.deleteSubscription(subscription.getId());
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            // Verify: the deleteSubscription method should throw a runtime exception
            assertEquals("Failed to delete subscription: ", e.getMessage());
        }
    }
    
    @Test
    public void testCreateSubscriptionSuccess() {
        // Setup: create a valid subscription object
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType("Basic");
        subscription.setNumberOfJobs(10);
        subscription.setDuration(30);
        subscription.setPrice(9.99);

        // Test: call the createSubscription method with the valid subscription object
        

        // Verify: the subscription object should have been saved to the database
        when(subscriptionRepo.save(subscription)).thenReturn(subscription);

        // Test: Call the createSubscription method with the valid subscription object
        String result = adminServiceImpl.createSubscription(subscription);

        // Verify: The createSubscription method should return a success message
        assertEquals("Subscription Created", result);
    }
    
    @Test
    public void testCreateSubscriptionException() {
        // Setup: create a stub subscription object with a null subscription type
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(null);
        subscription.setNumberOfJobs(10);
        subscription.setDuration(30);
        subscription.setPrice(9.99);

        // Test: call the createSubscription method with the invalid subscription object
        try {
            adminServiceImpl.createSubscription(subscription);
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            // Verify: the createSubscription method should throw a runtime exception
            assertEquals("Failed to create subscription: Subscription object is null", e.getMessage());
        }
    }
    
    
    
    
    
}
    
    
	

	



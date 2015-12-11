package org.opensource.result;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.opensource.result.model.Address;
import org.opensource.result.model.City;
import org.opensource.result.model.User;

public class ResulTest {

	private Result result;
	
	@Before
	public void setUp() {
		result = new Result();
	}
	
	@Test
	public void shouldBringAllSimpleProperties() {
		User user = createNewUser();
		
		User userExpect = result.getResult(user, User.class);
		
		assertEquals(1L, userExpect.getId(), 0.001);
		assertEquals("User Test", userExpect.getName());
		assertEquals(1, userExpect.getAge(), 0.001);
	}
	
	@Test
	public void shouldBringAllSimplePropertiesExceptAge() {
		User user = createNewUser();
		
		User userExpect = result.excludePropety("age").getResult(user, User.class);
		
		assertEquals(1L, userExpect.getId(), 0.001);
		assertEquals("User Test", userExpect.getName());
		assertNull(userExpect.getAge());
	}
	
	@Test
	public void shouldReturnOnlySimpleProperties() {
		User user = createNewUser();
		user.setAddress(createAddress());
		
		User userExpect = result.getResult(user, User.class);
		
		assertEquals("User Test", userExpect.getName());
		assertEquals(1, userExpect.getAge(), 0.001);
		assertNull(userExpect.getAddress());
	}
	
	@Test
	public void shouldReturnAddressIncluded() {
		User user = createNewUser();
		user.setAddress(createAddress());
		
		User userExpect = result.includePropety("address").getResult(user, User.class);
		
		assertEquals("User Test", userExpect.getName());
		assertEquals(1, userExpect.getAge(), 0.001);
		assertNotNull(userExpect.getAddress().getId());
	}
	
	@Test
	public void shouldReturnOnlySimplePropertiesOfAddressIncluded() {
		User user = createNewUser();
		user.setAddress(createAddress());
		user.getAddress().setCity(createCity());
		
		User userExpect = result.includePropety("address").getResult(user, User.class);
		
		assertEquals("User Test", userExpect.getName());
		assertEquals(1, userExpect.getAge(), 0.001);
		assertNotNull(userExpect.getAddress().getId());
		assertNull(userExpect.getAddress().getCity());
	}
	
	@Test
	public void shouldReturnAddressAndCityIncluded() {
		User user = createNewUser();
		user.setAddress(createAddress());
		user.getAddress().setCity(createCity());
		
		User userExpect = result.includePropety("address")
								.includePropety("address.city")
								.getResult(user, User.class);
		
		assertEquals("User Test", userExpect.getName());
		assertEquals(1, userExpect.getAge(), 0.001);
		assertNotNull(userExpect.getAddress().getId());
		assertNotNull(userExpect.getAddress().getCity().getId());
	}


	
	private City createCity() {
		City city = new City();
		city.setId(1L);
		city.setName("city test");
		return city;
	}
	
	private Address createAddress() {
		Address address = new Address();
		address.setId(1L);
		address.setStreet("street Test");
		return address;
	}

	private User createNewUser() {
		User user = new User();
		user.setAge(1);
		user.setId(1L);
		user.setName("User Test");
		
		return user;
	}
}

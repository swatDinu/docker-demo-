package com.staxrt.tutorial.service;


import com.staxrt.tutorial.exception.ResourceNotFoundException;
import com.staxrt.tutorial.model.User;
import com.staxrt.tutorial.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@PrepareForTest(UserService.class)
public class UserServiceTest {

	@MockBean
	private UserRepository userRepository;
	@InjectMocks
	private UserService service;

	private User user;
	@Before
	public void setUp() {
		service = new UserService(userRepository);
		user = new User("John", "Abram", "john@gmail.com", "admin", "admin");
		user.setId(new Long(1));
	}

	@Test
	public void testUpdateUser() throws ResourceNotFoundException {
		User updatedUser = new User("Joans", "Nick", "Jaons@gmail.com", "admin", "admin");
		Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
		Mockito.when(userRepository.save(updatedUser)).thenReturn(updatedUser);

		ResponseEntity<User> result = service.updateUser(user.getId(),updatedUser);
		assertEquals(result.getStatusCode().value(), 200);
	}

	@Test
	public void testDeleteUSer() throws ResourceNotFoundException {
		Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
		String result = service.deleteUser(user.getId());
		assertEquals("deleted", result);
	}

	@Test
	public void testGetAllUserMailIds() throws Exception {
		List<User> users = new ArrayList<>();
		users.add(user);
		Mockito.when(userRepository.findAll()).thenReturn(users);
		List<String> mailIds = service.getAllUserMailIds();
		assertEquals(mailIds.get(0), "john@gmail.com");

	}

}

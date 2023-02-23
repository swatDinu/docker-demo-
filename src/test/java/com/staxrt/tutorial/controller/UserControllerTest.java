package com.staxrt.tutorial.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staxrt.tutorial.exception.ResourceNotFoundException;
import com.staxrt.tutorial.model.User;
import com.staxrt.tutorial.repository.UserRepository;
import com.staxrt.tutorial.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
    }

    @Test
    public void testAllUsers() throws Exception{
        List<User> userList = new ArrayList<>();
        User mockedUser = new User("John", "Abram", "john@gmail.com", "admin", "admin");
        userList.add(mockedUser);
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<User> result = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<User>>(){});
        assertEquals(userList.size(), result.size());
    }

    @Test
    public void testGetUsersById() throws Exception{
        User mockedUser = new User("John", "Abram", "john@gmail.com", "admin", "admin");
        mockedUser.setId(new Long(1));
        Mockito.when(userRepository.findById(mockedUser.getId())).thenReturn(Optional.of(mockedUser));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1//users/{id}", new Long(1))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        User resultUser = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), User.class);
        assertEquals(resultUser.getFirstName(), mockedUser.getFirstName());
        assertEquals(resultUser.getLastName(), mockedUser.getLastName());
    }

    @Test
    public void testCreateUser() throws Exception {
        User mockedUser = new User("John", "Abram", "john@gmail.com", "admin", "admin");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockedUser)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User mockedUser = new User("John", "Abram", "john@gmail.com", "admin", "admin");
        mockedUser.setId(new Long(1));
        User modifiedUser = new User("Joans", "Abram", "Joans@gmail.com", "admin", "admin");
        Mockito.when(userRepository.findById(mockedUser.getId())).thenReturn(Optional.of(mockedUser));
        Mockito.when(userService.updateUser(mockedUser.getId(), mockedUser)).thenReturn(ResponseEntity.of(Optional.of(modifiedUser)));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/{id}", mockedUser.getId())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mockedUser)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteUser() throws Exception {
        User mockedUser = new User("John", "Abram", "john@gmail.com", "admin", "admin");
        mockedUser.setId(new Long(1));
        Mockito.when(userRepository.findById(mockedUser.getId())).thenReturn(Optional.of(mockedUser));
        Mockito.when(userService.deleteUser(mockedUser.getId())).thenReturn("deleted");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/deleteuser/{id}", mockedUser.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAllUserMailIds() throws Exception {
        List<User> userList = new ArrayList<>();
        User mockedUser = new User("John", "Abram", "john@gmail.com", "admin", "admin");
        userList.add(mockedUser);
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        Mockito.when(userService.getAllUserMailIds()).thenReturn(new ArrayList<>(Arrays.asList("john@gmail.com")));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/userDetails"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<String> result = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<String>>(){});
        assertEquals("john@gmail.com",result.get(0));
    }
}
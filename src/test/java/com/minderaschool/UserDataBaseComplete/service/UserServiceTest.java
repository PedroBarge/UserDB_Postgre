package com.minderaschool.UserDataBaseComplete.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.minderaschool.UserDataBaseComplete.dto.UserDtoCreateUser;
import com.minderaschool.UserDataBaseComplete.dto.UserDtoGetAll;
import com.minderaschool.UserDataBaseComplete.dto.UserDtoGetOneUser;
import com.minderaschool.UserDataBaseComplete.dto.UserDtoUpdate;
import com.minderaschool.UserDataBaseComplete.entity.UserEntity;
import com.minderaschool.UserDataBaseComplete.repositoy.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserService(repository);
    }

    @Test
    public void testAddUser() {
        UserDtoCreateUser userDto = new UserDtoCreateUser("username", "password", "email");
        UserEntity savedEntity = new UserEntity();
        savedEntity.setUsername(userDto.getUsername());
        savedEntity.setPassword(userDto.getPassword());
        savedEntity.setEmail(userDto.getEmail());

        when(repository.save(any(UserEntity.class))).thenReturn(savedEntity);

        UserDtoCreateUser result = userService.add(userDto);

        assertEquals(userDto, result);
        verify(repository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testGetUser() {
        UserEntity userEntity = new UserEntity(1, "username", "email", "password");
        when(repository.findById(1)).thenReturn(Optional.of(userEntity));

        UserDtoGetOneUser result = userService.getUser(1);

        assertEquals(1, (int) result.getId());
        assertEquals("username", result.getUsername());
        assertEquals("email", result.getEmail());
        assertEquals("password", result.getPassword());
    }

    @Test
    public void testGetAllUsers() {
        UserEntity user1 = new UserEntity(1, "user1", "email1", "password1");
        UserEntity user2 = new UserEntity(2, "user2", "email2", "password2");
        List<UserEntity> userList = Arrays.asList(user1, user2);
        when(repository.findAll()).thenReturn(userList);

        List<UserDtoGetAll> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals(2, result.get(1).getId());
        assertEquals("user2", result.get(1).getUsername());
    }

    @Test
    public void testUpdate() {
        UserDtoUpdate updatedUser = new UserDtoUpdate("username", "password");
        UserEntity userEntity = new UserEntity(1, "username", "email", "password");
        when(repository.findById(1)).thenReturn(Optional.of(userEntity));

        userService.update(1, updatedUser);

        assertEquals("username", userEntity.getUsername());
        assertEquals("password", userEntity.getPassword());
    }

    @Test
    public void testUpdatePatch() {
        UserDtoUpdate updatePatch = new UserDtoUpdate("newUsername", null);
        UserEntity userEntity = new UserEntity(1, "username", "email", "password");
        when(repository.findById(1)).thenReturn(Optional.of(userEntity));

        userService.updatePatch(1, updatePatch);

        assertEquals("newUsername", userEntity.getUsername());
        assertEquals("password", userEntity.getPassword());
        verify(repository, times(1)).save(userEntity);
    }

    @Test
    public void testDeleteUser() {
        when(repository.findById(1)).thenReturn(Optional.of(new UserEntity()));

        userService.deleteUser(1);

        verify(repository, times(1)).deleteById(1);
    }
}

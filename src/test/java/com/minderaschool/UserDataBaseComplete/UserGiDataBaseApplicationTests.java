package com.minderaschool.UserDataBaseComplete;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minderaschool.UserDataBaseComplete.entity.UserEntity;
import com.minderaschool.UserDataBaseComplete.repositoy.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserGiDataBaseApplicationTests {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserRepository userRepository;

    //-----Variables Area-----\\
    private final ObjectMapper mapper = new ObjectMapper();

    UserEntity user1 = new UserEntity(1, "User1","user1@email.com","Password1");
    UserEntity user2 = new UserEntity(2, "User2", "user2@email.com","Password2");
    UserEntity user3 = new UserEntity(3, "User3", "user3@email.com","Password3");

    //-----TEST AREA-----\\

    /**
     * Test to add user success
     */
    @Test
    void testAddUserOkShouldExpectStatusIsOk() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .username("User4")
                .email("user4@email.com")
                .password("Password4")
                .build();

        Mockito.when(userRepository.save(userEntity)).thenReturn(userEntity);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(userEntity));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        Mockito.verify(userRepository, times(1)).save(userEntity);
    }
    //----------\\

    /**
     * Test to add user don't have the body complete
     */
    static Stream <Arguments> testAddUserNotOkShouldExpectStatusIsBadRequestArgs(){
        return Stream.of(
                Arguments.of(null,null,null),
                Arguments.of("Username",null,null),
                Arguments.of(null,"Email",null),
                Arguments.of(null,null,"Password"),
                Arguments.of("Username","Email",null),
                Arguments.of("Username",null,"Password"),
                Arguments.of(null,"Email","Password")
                );
    }

    @ParameterizedTest
    @MethodSource("testAddUserNotOkShouldExpectStatusIsBadRequestArgs")
    void testAddUserNotOkShouldExpectStatusIsBadRequest(String username, String email, String password) throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .id(4)
                .username(username)
                .email(email)
                .password(password)
                .build();

        //Mockito.when(userRepository.save(userEntity)).thenReturn(userEntity);
        Mockito.verify(userRepository, times(0)).save(userEntity);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(userEntity));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }
    //----------\\

    /**
     * Test to get all the users OK
     */
    @Test
    void testGetAllUsersOkShouldExpectStatusIsOk() throws Exception {
        List<UserEntity> listUser = new ArrayList<>(Arrays.asList(user1, user2, user3));
        Mockito.when(userRepository.findAll()).thenReturn(listUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].username", is("User1")))
                .andExpect(jsonPath("$[1].username", is("User2")))
                .andExpect(jsonPath("$[2].username", is("User3")));
    }
    //----------\\

    /**
     * Test to get one user
     */
    @Test
    void testGetUserOkShouldExpectStatusIsOk() throws Exception {
        int id = 1;
        List<UserEntity> listUser = new ArrayList<>(Arrays.asList(user1, user2, user3));

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.ofNullable(listUser.get(id)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("User2")))
                .andExpect(jsonPath("$.password", is("Password2")));
    }
    //----------\\

    /**
     * Test to get one user but not ok
     */
    @Test
    void testGetUserNotOkShouldExpectStatusIsBadRequest() throws Exception {
        int idToSearchTest = 4;

        Mockito.when(userRepository.getReferenceById(idToSearchTest)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/{id}", idToSearchTest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    //----------\\

    /**
     * Test to DELETE user success
     */
    @Test
    void testDeleteUserOkShouldExpectStatusIsOk() throws Exception {
        int userIdToDelete = 1;
        List<UserEntity> listUser = new ArrayList<>(Arrays.asList(user1, user2, user3));
        Mockito.when(userRepository.findById(userIdToDelete)).thenReturn(Optional.of(listUser.get(userIdToDelete)));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/user/{id}", userIdToDelete)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
        Mockito.verify(userRepository, times(1)).deleteById(userIdToDelete);
    }
    //----------\\

    /**
     * Test to DELETE user not success
     */
    @Test
    void testDeleteUserShouldExpectStatusIsBadRequest() throws Exception {
        int userIdToDelete = 1;
        Mockito.when(userRepository.findById(userIdToDelete)).thenReturn(Optional.empty());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/user/{id}", userIdToDelete)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
        Mockito.verify(userRepository, times(0)).deleteById(userIdToDelete);
    }
    //----------\\

    /**
     * Test to UPDATE user success
     */
    @Test
    void testUpdateUserOkShouldReturnOkStatus() throws Exception {
        int id = 1;
        List<UserEntity> listUser = new ArrayList<>(Arrays.asList(user1, user2, user3));

        UserEntity userEntity = new UserEntity(1, "UPDATE", "userupdate@email.com","UPDATEPASSWORD");


        Mockito.when(userRepository.findById(id)).thenReturn(Optional.ofNullable((listUser.get(id))));
        Mockito.when(userRepository.save(userEntity)).thenReturn(userEntity);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(userEntity));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }
    //----------\\

    /**
     * Test to UPDATE user with bad request
     */
    @Test
    void testUpdateUserNotOkShouldReturnBadRequest() throws Exception {
        int id = 1;
        UserEntity userEntity = UserEntity.builder()
                .id(1)
                .username("UPDATE")
                .password(null)
                .build();

        Mockito.verify(userRepository, times(0)).findById(id);
        Mockito.verify(userRepository, times(0)).save(userEntity);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(userEntity));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }
    //----------\\

    /**
     * Test to UPDATE user with bad request id NULL
     */
    @Test
    void testUpdateUserNotOkShouldReturnBadRequestIdNull() throws Exception {
        int id = 1;
        UserEntity userEntity = UserEntity.builder()
                .id(null)
                .username("UPDATE")
                .password("password")
                .build();

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(userEntity)).thenReturn(userEntity);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(userEntity));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }
    //----------\\

    /**
     * Test to UPDATE user with bad request Body NULL
     */
    @Test
    void testUpdateUserNotOkShouldReturnBadRequestBodyNull() throws Exception {
        int id = 1;
        UserEntity userEntity = UserEntity.builder()
                .id(1)
                .username(null)
                .password(null)
                .build();

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(userEntity)).thenReturn(userEntity);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(userEntity));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }
    //----------\\

    /**
     * Test to UPDATE Patch user success
     */
    private static Stream<Arguments> testUpdatePatchUserShouldReturnIsOkARGS() {
        return Stream.of(
                Arguments.of("UserPatch", null),
                Arguments.of(null, "PasswordPatch"),
                Arguments.of("UserPatch", "PasswordPatch"));
    }

    @ParameterizedTest
    @MethodSource("testUpdatePatchUserShouldReturnIsOkARGS")
    void testUpdatePatchUserShouldReturnIsOk(String name, String pass) throws Exception {
        int id = 1;
        UserEntity user = new UserEntity();
        user.setUsername(name);
        user.setPassword(pass);

        List<UserEntity> listUser = new ArrayList<>(Arrays.asList(user1, user2, user3, user));

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(listUser.get(id)));
        Mockito.when(userRepository.save(listUser.get(id))).thenReturn(user);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .patch("/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePatchUserShouldReturnBadRequest() throws Exception {
        int idToPatchUserWithBadR = 3;
        UserEntity user = new UserEntity();
        user.setUsername(null);
        user.setPassword(null);

        List<UserEntity> listUser = new ArrayList<>(Arrays.asList(user1, user2, user3, user));

        Mockito.when(userRepository.findById(idToPatchUserWithBadR)).thenReturn(Optional.of(listUser.get(3)));
        Mockito.when(userRepository.save(listUser.get(3))).thenReturn(user);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .patch("/user/{idToPatchUserWithBadR}", idToPatchUserWithBadR)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }
}

package com.example.demo.controller.authentication;

import com.example.demo.Config.JwtService;
import com.example.demo.controllers.auth.AuthenticationController;
import com.example.demo.controllers.auth.AuthenticationResponse;
import com.example.demo.controllers.auth.RegisterRequest;
import com.example.demo.controllers.auth.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtService jwtService;


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

 //   @Test
//    public void testRegisterEndpoint() throws Exception {
//        // Create a RegisterRequest object with sample data
//        RegisterRequest registerRequest = new RegisterRequest();
//        registerRequest.setUsername("testUser");
//        registerRequest.setPassword("testPassword");
//        registerRequest.setEmail("test@example.com");
//
//        // Create an AuthenticationResponse object with sample data
//        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
//        authenticationResponse.setToken("sampleToken");
//        authenticationResponse.setEmail("test@example.com");
//        authenticationResponse.setUsername("testUser");
//        authenticationResponse.setId(1);
//
//        // Mock the behavior of AuthenticationService's register method
//        when(authenticationService.register(any(RegisterRequest.class)))
//                .thenReturn(authenticationResponse);
//
//        // Perform a POST request to the register endpoint
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8084/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(registerRequest)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//         //Verify the response content
//        String responseContent = mvcResult.getResponse().getContentAsString();
//        AuthenticationResponse response = objectMapper.readValue(responseContent, AuthenticationResponse.class);
//        assert response != null;
//        assert response.getToken().equals("sampleToken");
//        assert response.getEmail().equals("test@example.com");
//        assert response.getUsername().equals("testUser");
//        assert response.getId() == 1;
//    }
}

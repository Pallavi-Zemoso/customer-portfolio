package com.zemoso.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetLoginPage() throws Exception {
        mockMvc.perform(get("/login/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(forwardedUrl("/WEB-INF/view/login.jsp"));

        mockMvc.perform(get("/login/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(forwardedUrl("/WEB-INF/view/login.jsp"));
    }

    @Test
    public void testShowAccessDenied() throws Exception {
        mockMvc.perform(get("/access-denied/"))
                .andExpect(status().isOk())
                .andExpect(view().name("access-denied"))
                .andExpect(forwardedUrl("/WEB-INF/view/access-denied.jsp"));
    }
}

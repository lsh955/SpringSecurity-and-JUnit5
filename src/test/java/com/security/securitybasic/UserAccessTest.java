package com.security.securitybasic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author 이승환
 * @since 2020-11-26
 */
@WebMvcTest
public class UserAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    UserDetails user1() {
        return User.builder()
                .username("user1")
                .password(passwordEncoder.encode("1234"))
                .roles("USER")
                .build();
    }

    UserDetails admin() {
        return User.builder()
                .username("admin")
                .password(passwordEncoder.encode("1234"))
                .roles("ADMIN")
                .build();
    }

    @Test
    @DisplayName("1. user 로 user 페이지를 접근할 수 있다.")
        // @WithMockUser(username = "user1", roles = {"USER"}) // 누가 접근할것인가.
    void test_user_access_user_page() throws Exception {
        String resp = mockMvc.perform(get("/user").with(user(user1())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        SecurityMessage message = mapper.readValue(resp, SecurityMessage.class);
        assertEquals("user page", message.getMessage());
    }

    @Test
    @DisplayName("2. user로 admin 페이지를 접근할 수 없다.")
    @WithMockUser(username = "user1", roles = {"USER"})
        // 누가 접근할것인가.
    void test_user_cannot_access_admin_page() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("3. admin 이 user 페이지와 admin 페이지를 접근할 수 있다.")
        // @WithMockUser(username = "admin", roles = {"ADMIN"}) // 누가 접근할것인가.
    void test_admin_can_access_user_and_admin_page() throws Exception {
        SecurityMessage
                message = mapper.readValue(mockMvc.perform(get("/user").with(user(admin())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), SecurityMessage.class);
        assertEquals("user page", message.getMessage());

        message = mapper.readValue(mockMvc.perform(get("/admin").with(user(admin())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), SecurityMessage.class);
        assertEquals("admin page", message.getMessage());
    }

    @Test
    @DisplayName("4. login 페이지는 아무나 접근할 수 있어야 한다.")
    @WithAnonymousUser
    void test_login_page_can_accessed_anonymous() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("5. '/' 홈페이지는 로그인 하지 않은 사람은 접근할 수 없다.")
    void test_need_login() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());    // 302 redirect to /login
        mockMvc.perform(get("/user"))
                .andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection());
    }
}

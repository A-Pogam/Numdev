package com.openclassrooms.starterjwt.unit.security.jwt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterUnitTest {
    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @Tag("AuthTokenFilter.doFilterInternal()")
    @DisplayName("Valid token")
    void doFilterInternal_validToken_shouldSetAuthentication() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    @Tag("AuthTokenFilter.doFilterInternal()")
    @DisplayName("Invalid token")
    void doFilterInternal_invalidToken_shouldNotSetAuthentication() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertEquals(SecurityContextHolder.getContext().getAuthentication(), null);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @Tag("AuthTokenFilter.parseJwt()")
    @DisplayName(" Valid JWT Token from header")
    void parseJwt_validHeader_shouldReturnToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testToken");

        String result = authTokenFilter.parseJwt(request);

        assertEquals("testToken", result);
    }

    @Test
    @Tag("AuthTokenFilter.parseJwt()")
    @DisplayName("Invalid JWT Token from header")
    void parseJwt_invalidHeader_shouldReturnNull() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        String result = authTokenFilter.parseJwt(request);

        assertEquals(null, result);
    }
}

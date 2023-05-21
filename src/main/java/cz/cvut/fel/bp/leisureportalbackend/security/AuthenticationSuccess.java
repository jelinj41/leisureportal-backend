package cz.cvut.fel.bp.leisureportalbackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.bp.leisureportalbackend.security.model.LoginStatus;
import cz.cvut.fel.bp.leisureportalbackend.security.model.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Writes basic login/logout information into the response.
 */
@Service
public class AuthenticationSuccess implements AuthenticationSuccessHandler, LogoutSuccessHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationSuccess.class);

    private final ObjectMapper mapper;

    @Autowired
    public AuthenticationSuccess(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Handles successful authentication.
     *
     * @param httpServletRequest  The HTTP request.
     * @param httpServletResponse The HTTP response.
     * @param authentication      The authentication object.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        final String username = getUsername(authentication);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Successfully authenticated user {}", username);
        }

        httpServletResponse.setStatus(HttpStatus.ACCEPTED.value());
        final LoginStatus loginStatus = new LoginStatus(true, authentication.isAuthenticated(), username, null);
        mapper.writeValue(httpServletResponse.getOutputStream(), loginStatus);
    }

    /**
     * Retrieves the username from the authentication object.
     *
     * @param authentication The authentication object.
     * @return The username.
     */
    private String getUsername(Authentication authentication) {
        if (authentication == null) {
            return "";
        }
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    /**
     * Handles successful logout.
     *
     * @param httpServletRequest  The HTTP request.
     * @param httpServletResponse The HTTP response.
     * @param authentication      The authentication object.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Authentication authentication) throws IOException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Successfully logged out user {}", getUsername(authentication));
        }
        final LoginStatus loginStatus = new LoginStatus(false, true, null, null);
        mapper.writeValue(httpServletResponse.getOutputStream(), loginStatus);
    }
}

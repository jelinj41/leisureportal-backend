package cz.cvut.fel.bp.leisureportalbackend.security;

import cz.cvut.fel.bp.leisureportalbackend.security.model.AuthenticationToken;
import cz.cvut.fel.bp.leisureportalbackend.security.model.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuthenticationProvider.class);

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Performs authentication for the provided authentication object.
     *
     * @param authentication The authentication object.
     * @return An authenticated authentication object if successful.
     * @throws AuthenticationException If authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getPrincipal().toString();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Authenticating user {}", username);
        }
        final UserDetails userDetails = (UserDetails) userDetailsService.loadUserByUsername(username);
        final String password = (String) authentication.getCredentials();
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Provided credentials don't match.");
        }
        userDetails.eraseCredentials();
        return SecurityUtils.setCurrentUser(userDetails);
    }

    /**
     * Checks if the authentication provider supports the provided authentication class.
     *
     * @param aClass The authentication class to check.
     * @return True if the provider supports the authentication class, false otherwise.
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass) ||
                AuthenticationToken.class.isAssignableFrom(aClass);
    }
}

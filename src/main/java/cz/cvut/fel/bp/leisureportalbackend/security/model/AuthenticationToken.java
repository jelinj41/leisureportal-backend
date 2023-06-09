package cz.cvut.fel.bp.leisureportalbackend.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;

public class AuthenticationToken extends AbstractAuthenticationToken implements Principal {

    private UserDetails userDetails;

    public AuthenticationToken(Collection<? extends GrantedAuthority> authorities, UserDetails userDetails) {
        super(authorities);
        this.userDetails = userDetails;
        super.setAuthenticated(true);
        super.setDetails(userDetails);
    }

    /**
     * Returns the credentials (password) of the authenticated user.
     *
     * @return The user's password.
     */
    @Override
    public String getCredentials() {
        return userDetails.getPassword();
    }

    /**
     * Returns the principal (user details) of the authenticated user.
     *
     * @return The user details.
     */
    @Override
    public UserDetails getPrincipal() {
        return userDetails;
    }
}

package cz.cvut.fel.bp.leisureportalbackend.security;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionTimeoutManager implements HttpSessionListener {

    /**
     * Method invoked when a new session is created.
     * Sets the maximum inactive interval for the session based on the configured timeout value.
     *
     * @param httpSessionEvent The HttpSessionEvent object.
     */
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().setMaxInactiveInterval(SecurityConstants.SESSION_TIMEOUT);
    }

    /**
     * Method invoked when a session is destroyed.
     * Does nothing in this implementation.
     *
     * @param httpSessionEvent The HttpSessionEvent object.
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        // do nothing
    }
}

package cz.cvut.fel.bp.leisureportalbackend.config;

import cz.cvut.fel.bp.leisureportalbackend.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Configuration class for the security part of the application.
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "cz.cvut.fel.bp.leisureportalbackend.security")
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)// Allow methods to be secured using annotation
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] COOKIES_TO_DESTROY = {
            SecurityConstants.SESSION_COOKIE_NAME,
            SecurityConstants.REMEMBER_ME_COOKIE_NAME
    };

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final LogoutSuccessHandler logoutSuccessHandler;

    private final AuthenticationProvider authenticationProvider;

    /**
     * Constructor for SecurityConfig.
     *
     * @param authenticationFailureHandler The authentication failure handler.
     * @param authenticationSuccessHandler The authentication success handler.
     * @param logoutSuccessHandler         The logout success handler.
     * @param authenticationProvider       The authentication provider.
     */
    @Autowired
    public SecurityConfig(AuthenticationFailureHandler authenticationFailureHandler,
                          AuthenticationSuccessHandler authenticationSuccessHandler,
                          LogoutSuccessHandler logoutSuccessHandler,
                          AuthenticationProvider authenticationProvider) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * Configures the authentication manager builder with the specified authentication provider.
     *
     * @param auth The authentication manager builder.
     * @throws Exception If an exception occurs during configuration.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * Creates a bean for the authentication manager.
     *
     * @return The authentication manager bean.
     * @throws Exception If an exception occurs while creating the bean.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Configures the HTTP security for the application.
     *
     * @param http The HTTP security object.
     * @throws Exception If an exception occurs during configuration.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests().anyRequest().permitAll().and()
            .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and().headers().frameOptions().sameOrigin()
            .and().authenticationProvider(authenticationProvider)
            .formLogin().successHandler(authenticationSuccessHandler)
            .failureHandler(authenticationFailureHandler)
            .loginProcessingUrl(SecurityConstants.SECURITY_CHECK_URI)
            .usernameParameter(SecurityConstants.USERNAME_PARAM).passwordParameter(SecurityConstants.PASSWORD_PARAM)
            .and()
            .logout().invalidateHttpSession(true).deleteCookies(COOKIES_TO_DESTROY)
            .logoutUrl(SecurityConstants.LOGOUT_URI).logoutSuccessHandler(logoutSuccessHandler)
            .and().sessionManagement().maximumSessions(1);
    }

    /**
     * Configures web security by ignoring certain paths.
     *
     * @param web The web security object.
     * @throws Exception If an exception occurs during configuration.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/","/login", "/upcomingactivities");
    }

}

package com.eshop.authservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.eshop.authservice.service.security.MongoUserDetailsService;
import com.eshop.authservice.util.Constants;

/**
 * 
 *
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

	private TokenStore tokenStore = new InMemoryTokenStore();

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Autowired
	private MongoUserDetailsService userDetailsService;

	@Autowired
	private Environment env;
	
	//@Autowired
    //private BCryptPasswordEncoder passwordEncoder;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		// TODO persist clients details

		// @formatter:off
        clients.inMemory()
        	   .withClient("john").secret(Constants.NOOP_PASSWORD_ENCODE + "123456")
        	   .authorizedGrantTypes("client_credentials")
        	   .scopes("resource-server-read", "resource-server-write")
        	   .and()
               .withClient("browser")
               .authorizedGrantTypes("refresh_token", "password")
               .scopes("ui")
               .and()
               .withClient("account-service")
               .secret(env.getProperty("ACCOUNT_SERVICE_PASSWORD"))
               .authorizedGrantTypes("client_credentials", "refresh_token")
               .scopes("server")
               .and()
               .withClient("statistics-service")
               .secret(env.getProperty("STATISTICS_SERVICE_PASSWORD"))
               .authorizedGrantTypes("client_credentials", "refresh_token")
               .scopes("server")
               .and()
               .withClient("notification-service")
               .secret(env.getProperty("NOTIFICATION_SERVICE_PASSWORD"))
               .authorizedGrantTypes("client_credentials", "refresh_token")
               .scopes("server");
        // @formatter:on
	}

	/*
	 * @Override public void configure(AuthorizationServerEndpointsConfigurer
	 * endpoints) throws Exception {
	 * endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager)
	 * .userDetailsService(userDetailsService); }
	 */

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		// @formatter:off
		oauthServer.tokenKeyAccess("permitAll()")
				   .checkTokenAccess("isAuthenticated()");
				   //.passwordEncoder(passwordEncoder);
		// @formatter:on
	}

}

package com.example.demo.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .oauth2Login()
            .userInfoEndpoint()
            .oidcUserService(this.oidcUserServiceImpl());
    }
    
    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserServiceImpl() {
		return (userRequest) -> {
			// Delegate to the default implementation for loading a user
            OidcUser oidcUser = this.oidcUserService.loadUser(userRequest);

            // Add custom roles
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            String[] roles = new String[2];
            String upn = oidcUser.getAttributes().get("upn").toString();
            if (upn.equals("usertest1@luisjtest.onmicrosoft.com")) {
                roles[0] = "ROLE_Group1";
            } else {
                roles[0] = "ROLE_Group2";
            }
            roles[1] = "ROLE_USER";
            mappedAuthorities.addAll(AuthorityUtils.createAuthorityList(roles));

			oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
			return oidcUser;
		};
	}
}
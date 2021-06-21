package dev.iagorodrigues.dscatalog.config;

import dev.iagorodrigues.dscatalog.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired private JwtTokenStore tokenStore;
    @Autowired private Environment env;

    private static final String[] PUBLIC_ROUTES = { "/oauth/token", "/h2-console/**" };
    private static final String[] PRIVATE_ROUTES = { "/products/**", "/categories/**" };
    private static final String[] ADMIN_ROUTES = { "/users/**" };

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            authorizeH2Database(http);
        }

        http.authorizeRequests()
            .antMatchers(PUBLIC_ROUTES).permitAll()
            .antMatchers(HttpMethod.GET, PRIVATE_ROUTES).permitAll()
            .antMatchers(PRIVATE_ROUTES).hasAnyRole(Roles.ADMIN.toString(), Roles.PRODUTOR.toString())
            .antMatchers(ADMIN_ROUTES).hasRole(Roles.ADMIN.toString())
            .anyRequest().authenticated();
    }

    private void authorizeH2Database(HttpSecurity http) throws Exception {
        disableFrameOptions(http);
    }

    private void disableFrameOptions(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
    }
}

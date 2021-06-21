package dev.iagorodrigues.dscatalog.components;

import dev.iagorodrigues.dscatalog.entities.User;
import dev.iagorodrigues.dscatalog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtTokenEnhancer implements TokenEnhancer {

    @Autowired private UserRepository userRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).get();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userFirstName", user.getFirstName());
        userMap.put("userId", user.getId());

        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        token.setAdditionalInformation(userMap);

        return accessToken;
    }
}

package com.veloMTL.veloMTL.Service.Auth;

import com.veloMTL.veloMTL.DTO.auth.LoginResponseDTO;
import com.veloMTL.veloMTL.Model.Users.Rider;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleRegistrationService extends DefaultOAuth2UserService {

     private final AuthService authService;

     public GoogleRegistrationService(AuthService authService){
         this.authService = authService;
     }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // extract info from Google
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // This method should create the user if needed and return token+user info
        // Change your AuthService to return LoginResponseDTO (token, name, email, role)
        LoginResponseDTO loginResponse = authService.registerGoogleUser(name, email);

        // copy existing attributes and add our token + fields
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("token", loginResponse.getToken());
        attributes.put("name", loginResponse.getName());
        attributes.put("email", loginResponse.getEmail());
        attributes.put("role", loginResponse.getRole());

        // Use provider's nameAttributeKey as the key for principal name (commonly "sub" or "email")
        String nameAttributeKey = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // return a DefaultOAuth2User with the augmented attributes map
        return new DefaultOAuth2User(oAuth2User.getAuthorities(), attributes, nameAttributeKey);
    }

    }

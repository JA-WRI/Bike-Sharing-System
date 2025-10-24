package com.veloMTL.veloMTL.Service.Auth;

import com.veloMTL.veloMTL.Model.Users.Rider;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class GoogleRegistrationService extends DefaultOAuth2UserService {

     private final RegistrationService registrationService;

     public GoogleRegistrationService(RegistrationService registrationService){
         this.registrationService = registrationService;
     }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // extract info from Google
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Rider rider = registrationService.registerGoogleUser(name, email);
        return oAuth2User;
    }

    }

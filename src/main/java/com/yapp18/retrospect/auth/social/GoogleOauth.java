package com.yapp18.retrospect.auth.social;

import org.springframework.stereotype.Component;

@Component
public class GoogleOauth implements SocialOauth{
    @Override
    public String getOauthRedirectURL() {
        return "";
    }
}

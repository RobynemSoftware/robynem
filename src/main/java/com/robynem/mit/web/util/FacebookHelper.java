package com.robynem.mit.web.util;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.Reading;
import facebook4j.User;
import facebook4j.auth.AccessToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by roberto on 12/12/2015.
 */
@Service("facebookHelper")
public class FacebookHelper {

    @Value("${facebook.app-id}")
    private String appId;

    @Value("${facebook.appSecret}")
    private String appSecret;

    private Facebook getInstance(String accessToken, String permissions) {
        Facebook instance = new FacebookFactory().getInstance();
        instance.setOAuthAppId(this.appId, this.appSecret);
        instance.setOAuthAccessToken(new AccessToken(accessToken, null));
        instance.setOAuthPermissions(permissions);

        return instance;
    }

    public User getFacebookUser(String accessToken) throws Exception {
        Facebook instance = this.getInstance(accessToken, "public_profile,email");
        User fbUser = instance.getMe();
        Reading reading = new Reading().fields("id", "name", "first_name", "last_name", "email", "gender");

        fbUser = instance.getUser(fbUser.getId(), reading);

        return fbUser;
    }
}

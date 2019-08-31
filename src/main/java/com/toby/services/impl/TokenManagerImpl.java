package com.toby.services.impl;


import com.toby.conf.Constants;
import com.toby.model.TokenModel;
import com.toby.services.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TokenManagerImpl implements TokenManager {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public TokenModel createToken(String userId) {

        String token = UUID.randomUUID().toString().replace("-", "");

        TokenModel model = new TokenModel(userId, token);

        //存储到redis并设置过期时间
        redisTemplate.boundValueOps(token).set(userId.toString(), Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return model;
    }

    @Override
    public boolean checkToken(TokenModel model) {

        if (model == null) {
            return false;
        }
        String id = redisTemplate.boundValueOps(model.getToken()).get();
        if (id == null || id.length() == 0) {
            return false;
        }
        model.setUserId(id);
        redisTemplate.boundValueOps(model.getToken()).expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return true;
    }

    @Override
    public TokenModel getToken(String authentication) {

        if (authentication == null || authentication.length() == 0) {
            return null;
        }
        return new TokenModel(authentication);
    }

    @Override
    public void deleteToken(String userId) {
        redisTemplate.delete(userId.toString());
    }
}

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
    public TokenModel createToken(String userId, int auth) {

        String token = UUID.randomUUID().toString().replace("-", "");

        TokenModel model = new TokenModel(userId, token, auth);

        //存储到redis并设置过期时间
        redisTemplate.boundValueOps(userId.toString()).set(token, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return model;
    }

    @Override
    public boolean checkToken(TokenModel model) {

        if (model == null) {
            return false;
        }
        String token = redisTemplate.boundValueOps(model.getUserId().toString()).get();
        if (token == null || !token.equals(model.getToken())) {
            return false;
        }
        redisTemplate.boundValueOps(model.getUserId().toString()).expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return true;
    }

    @Override
    public TokenModel getToken(String authentication) {

        if (authentication == null || authentication.length() == 0) {
            return null;
        }
        String[] param = authentication.split("_");
        if (param.length != 3) {
            return null;
        }
        String userId = param[0];
        String token = param[1];
        int auth = Integer.valueOf(param[2]);

        return new TokenModel(userId, token, auth);
    }

    @Override
    public void deleteToken(String userId) {
        redisTemplate.delete(userId.toString());
    }
}

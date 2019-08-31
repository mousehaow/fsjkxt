package com.toby.services;


import com.toby.model.TokenModel;

public interface TokenManager {

    TokenModel createToken(String userId);

    boolean checkToken(TokenModel model);

    TokenModel getToken(String authentication);

    void deleteToken(String token);
}

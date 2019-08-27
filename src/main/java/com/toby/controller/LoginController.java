package com.toby.controller;

import com.toby.annotation.Authorization;
import com.toby.annotation.CurrentUser;
import com.toby.conf.ResultStatus;
import com.toby.model.ResultModel;
import com.toby.model.TokenModel;
import com.toby.model.User;
import com.toby.services.TokenManager;
import com.toby.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenManager;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestParam(name = "account") String account,
                                @RequestParam(name = "password") String password) {
        User userInfo = userService.getByName(account);

        if (userInfo == null) {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.USER_NOT_FOUND), HttpStatus.NOT_FOUND);
        } else if (!userInfo.getPassword().equals(password)) {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.USERNAME_OR_PASSWORD_ERROR), HttpStatus.NOT_FOUND);
        }
        TokenModel model = tokenManager.createToken(userInfo.getId(), userInfo.getAuthority());
        userInfo.setPassword(null);
        model.setUser(userInfo);
        return new ResponseEntity<>(ResultModel.ok(model), HttpStatus.OK);
    }



    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    @Authorization
    public ResponseEntity logout(@CurrentUser User user) {
        tokenManager.deleteToken(user.getId());
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }
}

package com.toby.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.toby.annotation.Authorization;
import com.toby.annotation.CurrentUser;
import com.toby.conf.Constants;
import com.toby.conf.ResultStatus;
import com.toby.model.RecordModel;
import com.toby.model.ResultModel;
import com.toby.model.TokenModel;
import com.toby.model.User;
import com.toby.services.TokenManager;
import com.toby.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping(value = "/api/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenManager;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        if (jsonObject == null) {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR),  HttpStatus.OK);
        } else {
            if (jsonObject.containsKey("account") &&
                    jsonObject.containsKey("password")) {
                String account = jsonObject.getString("account");
                String password = jsonObject.getString("password");
                User userInfo = userService.getByName(account);
                if (userInfo == null) {
                    return new ResponseEntity<>(ResultModel.error(ResultStatus.USER_NOT_FOUND), HttpStatus.OK);
                } else if (!userInfo.getPassword().equals(password)) {
                    return new ResponseEntity<>(ResultModel.error(ResultStatus.USERNAME_OR_PASSWORD_ERROR), HttpStatus.OK);
                }
                TokenModel model = tokenManager.createToken(userInfo.getId());
                userInfo.setPassword(null);
                model.setUser(userInfo);
                return new ResponseEntity<>(ResultModel.ok(model), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
            }
        }
    }

    @Authorization
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@CurrentUser User user,
                                   @RequestBody String body) {
        if (user.getAuthority() == 0) {
            // TODO
            // 处理相应注册用户的权限
            return new ResponseEntity<>(ResultModel.ok(null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.NO_AUTHORITY), HttpStatus.OK);
        }
    }

    @Authorization
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity changePassword(@CurrentUser User user,
                                   @RequestBody String body) {
        if (user.getAuthority() == 0) {
            // TODO
            // 修改密码逻辑
            return new ResponseEntity<>(ResultModel.ok(null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.NO_AUTHORITY), HttpStatus.OK);
        }
    }




    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    @Authorization
    public ResponseEntity logout(HttpServletRequest request) {
        String authorization = request.getHeader(Constants.AUTHORIZATION);
        tokenManager.deleteToken(authorization);
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }
}

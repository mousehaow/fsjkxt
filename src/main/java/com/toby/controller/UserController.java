package com.toby.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toby.annotation.Authorization;
import com.toby.annotation.CurrentUser;
import com.toby.conf.Constants;
import com.toby.conf.ResultStatus;
import com.toby.model.RecordModel;
import com.toby.model.ResultModel;
import com.toby.model.User;
import com.toby.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/system")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    @Authorization
    public ResponseEntity logout(@CurrentUser User user, @RequestBody String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        if (jsonObject == null) {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR),  HttpStatus.OK);
        } else {
            int page = 0;
            int size = 50;
            Sort sort = new Sort(Sort.Direction.DESC, "registerTime");
            if (jsonObject.containsKey("page")) {
                page = jsonObject.getIntValue("page");
            }
            if (jsonObject.containsKey("size")) {
                size = jsonObject.getIntValue("size");
            }
            if (jsonObject.containsKey("sort")) {
                sort = new Sort(Sort.Direction.DESC, jsonObject.getString("sort"));
            }
            Page<User> result = userService.getAll(page, size, sort);
            return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
        }
    }

    @Authorization
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity changePassword(@CurrentUser User user,
                                         @RequestBody String body) {
        if (user.getAuthority() == 0) {
            JSONObject jsonObject = JSON.parseObject(body);
            if (jsonObject == null) {
                return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR),  HttpStatus.OK);
            } else {
                String userId = null;
                String newPassword = null;
                if (jsonObject.containsKey("userId")) {
                    userId = jsonObject.getString("userId");
                }
                if (jsonObject.containsKey("newPassword")) {
                    newPassword = jsonObject.getString("newPassword");
                }
                if (userId != null && newPassword != null) {
                    userService.changePassword(userId, newPassword);
                    return new ResponseEntity<>(ResultModel.ok(null), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
                }

            }
        } else {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.NO_AUTHORITY), HttpStatus.OK);
        }
    }
}

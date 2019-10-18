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
import java.util.Date;

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
            int size = 15;
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
            if (jsonObject.containsKey("search")) {
                String search = jsonObject.getString("search");
                Page<User> result = userService.searchAll(page, size, sort, search);
                return new ResponseEntity<>(ResultModel.ok(JSON.toJSONString(result)), HttpStatus.OK);
            } else {
                Page<User> result = userService.getAll(page, size, sort);
                return new ResponseEntity<>(ResultModel.ok(JSON.toJSONString(result)), HttpStatus.OK);
            }
        }
    }

    @Authorization
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity changePassword(@CurrentUser User user,
                                         @RequestBody String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        if (jsonObject == null) {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR),  HttpStatus.OK);
        } else {
            String oldPassword = null;
            String newPassword = null;
            if (jsonObject.containsKey("oldPassword")) {
                oldPassword = jsonObject.getString("oldPassword");
            }
            if (jsonObject.containsKey("newPassword")) {
                newPassword = jsonObject.getString("newPassword");
            }
            if (oldPassword != null && newPassword != null) {
                if (userService.getById(user.getId()).getPassword().equals(oldPassword)) {
                    userService.changePassword(user.getId(), newPassword);
                    return new ResponseEntity<>(ResultModel.ok("新密码设置成功"), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(ResultModel.ok("旧密码匹配失败"), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
            }

        }
    }

    @Authorization
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseEntity resetPassword(@CurrentUser User user,
                                         @RequestBody String body) {
        if (user.getAuthority() == 0) {
            JSONObject jsonObject = JSON.parseObject(body);
            if (jsonObject == null) {
                return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
            } else {
                String userId = null;
                String newPassword = null;
                if (jsonObject.containsKey("userId")) {
                    userId = jsonObject.getString("userId");
                }
                if (jsonObject.containsKey("password")) {
                    newPassword = jsonObject.getString("password");
                }
                if (userId != null && newPassword != null) {
                    User targetUser = userService.getById(userId);
                    if (targetUser.getAuthority() == 0) {
                        return new ResponseEntity<>(ResultModel.ok("超级管理员不能进行该操作"), HttpStatus.OK);
                    } else {
                        userService.changePassword(userId, newPassword);
                        return new ResponseEntity<>(ResultModel.ok("重置密码成功，新密码为123456"), HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
                }

            }
        } else {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.NO_AUTHORITY), HttpStatus.OK);
        }
    }

    @Authorization
    @RequestMapping(value = "/addNewUser", method = RequestMethod.POST)
    public ResponseEntity addNewUser(@CurrentUser User user,
                                        @RequestBody String body) {
        if (user.getAuthority() == 0) {
            JSONObject jsonObject = JSON.parseObject(body);
            if (jsonObject == null) {
                return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
            } else {
                String userName = null;
                String password = null;
                if (jsonObject.containsKey("userName")) {
                    userName = jsonObject.getString("userName");
                }
                if (jsonObject.containsKey("password")) {
                    password = jsonObject.getString("password");
                }
                if (userName != null && password != null) {
                    if (userService.userNameIsExist(userName)) {
                        return new ResponseEntity<>(ResultModel.ok("用户名已经被注册"), HttpStatus.OK);
                    }
                    User newUser = new User();
                    newUser.setUserName(userName);
                    newUser.setPassword(password);
                    newUser.setRegisterTime(new Date());
                    newUser.setLoginCount(0);
                    newUser.setAuthority(jsonObject.getIntValue("authority"));
                    if (jsonObject.containsKey("phoneNumber")) {
                        newUser.setPhoneNumber(jsonObject.getString("phoneNumber"));
                    }
                    if (jsonObject.containsKey("remark")) {
                        newUser.setRemark(jsonObject.getString("remark"));
                    }
                    userService.saveNewUser(newUser);
                    return new ResponseEntity<>(ResultModel.ok("新用户添加成功"), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
                }

            }
        } else {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.NO_AUTHORITY), HttpStatus.OK);
        }
    }

    @Authorization
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public ResponseEntity deleteUser(@CurrentUser User user,
                                     @RequestBody String body) {
        if (user.getAuthority() == 0) {
            JSONObject jsonObject = JSON.parseObject(body);
            if (jsonObject == null) {
                return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
            } else {
                String userId = null;
                if (jsonObject.containsKey("userId")) {
                    userId = jsonObject.getString("userId");
                }
                if (userId != null) {
                    User targetUser = userService.getById(userId);
                    if (targetUser.getAuthority() == 0) {
                        return new ResponseEntity<>(ResultModel.ok("超级管理员不能删除"), HttpStatus.OK);
                    } else {
                        userService.deleteUser(userId);
                        return new ResponseEntity<>(ResultModel.ok("用户删除成功"), HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
                }

            }
        } else {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.NO_AUTHORITY), HttpStatus.OK);
        }
    }
}

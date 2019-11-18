package com.toby.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toby.annotation.Authorization;
import com.toby.annotation.CurrentUser;
import com.toby.conf.ResultStatus;
import com.toby.model.ResultModel;
import com.toby.model.SupportInfoModel;
import com.toby.model.User;
import com.toby.services.SupportInfoService;
import com.toby.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/support")
public class SupportInfoController {


    @Autowired
    private UserService userService;

    @Autowired
    private SupportInfoService supportInfoService;

    @Authorization
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity getInfo() {
        SupportInfoModel result = supportInfoService.getInfo();
        return new ResponseEntity<>(ResultModel.ok(JSON.toJSONString(result)), HttpStatus.OK);
    }

    @Authorization
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity updateInfo(@CurrentUser User user,
                                     @RequestBody String body) {
        if (user.getAuthority() == 0) {
            JSONObject jsonObject = JSON.parseObject(body);
            if (jsonObject == null) {
                return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
            } else {
                SupportInfoModel info = new SupportInfoModel();
                if (jsonObject.containsKey("phoneNumber")) {
                    info.setPhoneNumber(jsonObject.getString("phoneNumber"));
                }
                if (jsonObject.containsKey("address")) {
                    info.setAddress(jsonObject.getString("address"));
                }
                if (jsonObject.containsKey("instructions")) {
                    info.setInstructions(jsonObject.getString("instructions"));
                }
                if (jsonObject.containsKey("briefIntroduction")) {
                    info.setBriefIntroduction(jsonObject.getString("briefIntroduction"));
                }
                SupportInfoModel newInfo = supportInfoService.updateInfo(info);
                Map<String, Object> result = new HashMap<>();
                result.put("result", "信息更新成功");
                result.put("info", newInfo);
                return new ResponseEntity<>(ResultModel.ok(JSON.toJSONString(result)), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.NO_AUTHORITY), HttpStatus.OK);
        }
    }
}

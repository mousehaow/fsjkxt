package com.toby.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toby.conf.ResultStatus;
import com.toby.model.AppLoginModel;
import com.toby.model.ResultModel;
import com.toby.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/app")
public class AppController {


    @Autowired
    private AppService appService;

    @RequestMapping(value = "/online", method = RequestMethod.POST)
    public ResponseEntity getAll(@RequestBody String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        if (jsonObject == null) {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR),  HttpStatus.OK);
        } else {
            AppLoginModel loginModel = JSON.parseObject(body, AppLoginModel.class);

            String result = appService.insertNewAppLoginRecord(loginModel);
            return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
        }
    }
}

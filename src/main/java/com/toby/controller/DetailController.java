package com.toby.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toby.annotation.Authorization;
import com.toby.conf.ResultStatus;
import com.toby.model.DetailModel;
import com.toby.model.ResultModel;
import com.toby.services.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/detail")
public class DetailController {

    @Autowired
    private DetailService detailService;

    @Authorization
    @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
    public ResponseEntity getInfo(@RequestBody String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        if (jsonObject == null) {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR),  HttpStatus.OK);
        } else {
            String recordId;
            if (jsonObject.containsKey("recordId")) {
                recordId = jsonObject.getString("recordId");
                List<DetailModel> result = detailService.getAllDetail(recordId);
                return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
            }
        }
    }
}

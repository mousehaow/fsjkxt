package com.toby.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toby.annotation.Authorization;
import com.toby.conf.ResultStatus;
import com.toby.model.ResultModel;
import com.toby.services.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/statistical")
public class StatisticalController {

    @Autowired
    private StatisticalService statisticalService;

    @Authorization
    @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
    public ResponseEntity getInfo() {
        Map<String, Object> result = statisticalService.getInfo();
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }


    @Authorization
    @RequestMapping(value = "/getRecordNum", method = RequestMethod.POST)
    public ResponseEntity getRecordNum(@RequestBody(required=false) String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        if (jsonObject == null) {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR),  HttpStatus.OK);
        } else {
            String requestType;
            int requestNum;
            if (jsonObject.containsKey("requestType") && jsonObject.containsKey("requestNum")) {
                requestType = jsonObject.getString("requestType");
                requestNum = jsonObject.getIntValue("requestNum");
            } else {
                return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
            }
            Map<String, Object> result = statisticalService.getRecordNumList(requestType, requestNum);

            return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
        }
    }

    @Authorization
    @RequestMapping(value = "/getRecordType", method = RequestMethod.POST)
    public ResponseEntity getRecordType() {
        List<Integer> result = statisticalService.getRecordType();
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    @Authorization
    @RequestMapping(value = "/getRecordZone", method = RequestMethod.POST)
    public ResponseEntity getRecordZone() {
        Map<String, Object> result = statisticalService.getRecordZone();
        return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
    }

    @Authorization
    @RequestMapping(value = "/getAppLoginNum", method = RequestMethod.POST)
    public ResponseEntity getAppLoginNum(@RequestBody(required=false) String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        if (jsonObject == null) {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR),  HttpStatus.OK);
        } else {
            String requestType;
            int requestNum;
            if (jsonObject.containsKey("requestType") && jsonObject.containsKey("requestNum")) {
                requestType = jsonObject.getString("requestType");
                requestNum = jsonObject.getIntValue("requestNum");
            } else {
                return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
            }
            Map<String, Object> result = statisticalService.getAppLoginNumList(requestType, requestNum);

            return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
        }
    }

}

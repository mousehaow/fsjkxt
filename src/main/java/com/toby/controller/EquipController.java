package com.toby.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toby.annotation.Authorization;
import com.toby.conf.ResultStatus;
import com.toby.model.RecordModel;
import com.toby.model.ResultModel;
import com.toby.services.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/equip")
public class EquipController {

    @Autowired
    private RecordService recordService;

    @Authorization
    @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
    public ResponseEntity getInfo(@RequestBody String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        if (jsonObject == null) {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR),  HttpStatus.OK);
        } else {
            String equipAddress;
            if (jsonObject.containsKey("equipAddress")) {
                equipAddress = jsonObject.getString("equipAddress");
            } else {
                return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("total", recordService.getTotalCount(equipAddress, -1));
            result.put("totalAlarm", recordService.getTotalCountOverThreshold(equipAddress, true, -1));
            result.put("professional", recordService.getTotalCount(equipAddress, 1));
            result.put("professionalAlarm", recordService.getTotalCountOverThreshold(equipAddress, true, 1));
            return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
        }
    }

    @Authorization
    @RequestMapping(value = "/getRecordList", method = RequestMethod.POST)
    public ResponseEntity getRecordList(@RequestBody String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        if (jsonObject == null) {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR),  HttpStatus.OK);
        } else {
            String equipAddress;
            if (jsonObject.containsKey("equipAddress")) {
                equipAddress = jsonObject.getString("equipAddress");
            } else {
                return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR), HttpStatus.OK);
            }
            int page = 0;
            int size = 50;
            Sort sort = new Sort(Sort.Direction.DESC, "startTime");
            if (jsonObject.containsKey("page")) {
                page = jsonObject.getIntValue("page");
            }
            if (jsonObject.containsKey("size")) {
                size = jsonObject.getIntValue("size");
            }
            if (jsonObject.containsKey("sort")) {
                sort = new Sort(Sort.Direction.DESC, jsonObject.getString("sort"));
            }
            Page<RecordModel> result = recordService.getAllRecordByEquipAddress(equipAddress, page, size, sort);
            return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
        }
    }

}

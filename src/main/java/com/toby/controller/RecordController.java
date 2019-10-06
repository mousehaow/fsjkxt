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

import java.util.Date;

@RestController
@RequestMapping(value = "/api/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Authorization
    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        if (jsonObject == null) {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR),  HttpStatus.OK);
        } else {
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
            Page<RecordModel> result;
            if (jsonObject.containsKey("startTime") && jsonObject.containsKey("endTime")) {
                Date startTime = jsonObject.getDate("startTime");
                Date endTime = jsonObject.getDate("endTime");
                result = recordService.getAllRecordByTime(startTime, endTime, page, size, sort);
                return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
            }
            if (jsonObject.containsKey("province")) {
                String province = jsonObject.getString("province");
                result = recordService.getAllRecordByProvince(province, page, size, sort);
                return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);

            }
            if (jsonObject.containsKey("city")) {
                String city = jsonObject.getString("city");
                result = recordService.getAllRecordByCity(city, page, size, sort);
                return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
            }
            if (jsonObject.containsKey("zone")) {
                String zone = jsonObject.getString("zone");
                result = recordService.getAllRecordByZone(zone, page, size, sort);
                return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
            }
            if (jsonObject.containsKey("alarm")) {
                boolean alarm = jsonObject.getBoolean("alarm");
                result = recordService.getAllRecordByAlarm(alarm, page, size, sort);
                return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
            }
            if (jsonObject.containsKey("type")) {
                int type = jsonObject.getIntValue("type");
                result = recordService.getAllRecordByType(type, page, size, sort);
                return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
            }
            result = recordService.getAllRecord(page, size, sort);
            return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
        }

    }
}

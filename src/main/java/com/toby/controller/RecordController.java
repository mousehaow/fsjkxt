package com.toby.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toby.annotation.Authorization;
import com.toby.conf.ResultStatus;
import com.toby.model.DetailModel;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Authorization
    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    public ResponseEntity getAll(@RequestBody(required=false) String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        if (jsonObject == null) {
            int page = 0;
            int size = 15;
            Sort sort = new Sort(Sort.Direction.DESC, "startTime");
            Page<RecordModel> result = recordService.getAllRecord(page, size, sort);
            return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
        } else {
            int page = 0;
            int size = 15;
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

    @RequestMapping(value = "/uploadSpecial", method = RequestMethod.POST)
    public ResponseEntity uploadSpecial(@RequestBody(required=false) String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        if (jsonObject == null) {
            return new ResponseEntity<>(ResultModel.error(ResultStatus.PARAM_ERROR),  HttpStatus.OK);
        } else {
            RecordModel record = JSON.parseObject(jsonObject.getString("init"), RecordModel.class);
            record.setRecordType(1);
            record.setCount(0);
            record.setTotalDose(0.0);
            JSONArray array = JSON.parseArray(jsonObject.getString("data"));
            List<DetailModel> details = new ArrayList<>();
            for (Object one : array) {
                DetailModel detail = JSON.parseObject(((JSONObject)one).toJSONString(), DetailModel.class);
                details.add(detail);
            }
            boolean result = recordService.updateSpecialRecord(record, details);
            return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
        }
    }
}

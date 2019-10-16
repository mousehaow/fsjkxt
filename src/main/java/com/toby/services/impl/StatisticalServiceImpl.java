package com.toby.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.toby.model.RecordModel;
import com.toby.repository.AppLoginRepository;
import com.toby.repository.EquipRepository;
import com.toby.repository.RecordRepository;
import com.toby.services.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StatisticalServiceImpl implements StatisticalService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private EquipRepository equipRepository;

    @Autowired
    private AppLoginRepository appLoginRepository;


    @Override
    public Map<String, Object> getInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("total", recordRepository.count());
        result.put("totalAlarm", recordRepository.count(new Specification<RecordModel>() {
            @Override
            public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition = criteriaBuilder.equal(root.get("overThreshold"), true);
                return criteriaQuery.where(condition).getRestriction();
            }
        }));
        result.put("professional", recordRepository.count(new Specification<RecordModel>() {
            @Override
            public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition = criteriaBuilder.equal(root.get("recordType"), 1);
                return criteriaQuery.where(condition).getRestriction();
            }
        }));
        result.put("equipNum", equipRepository.count());

        return result;
    }

    @Override
    public List<Integer> getRecordType() {
        List<Integer> result = new ArrayList<>();
        result.add((int)recordRepository.count(new Specification<RecordModel>() {
            @Override
            public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition = criteriaBuilder.equal(root.get("overThreshold"), false);
                Predicate condition1 = criteriaBuilder.equal(root.get("recordType"), 0);
                return criteriaQuery.where(condition, condition1).getRestriction();
            }
        }));
        result.add((int)recordRepository.count(new Specification<RecordModel>() {
            @Override
            public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition = criteriaBuilder.equal(root.get("overThreshold"), true);
                Predicate condition1 = criteriaBuilder.equal(root.get("recordType"), 0);
                return criteriaQuery.where(condition, condition1).getRestriction();
            }
        }));
        result.add((int)recordRepository.count(new Specification<RecordModel>() {
            @Override
            public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition = criteriaBuilder.equal(root.get("recordType"), 1);
                return criteriaQuery.where(condition).getRestriction();
            }
        }));

        return result;
    }

    @Override
    public Map<String, Object> getRecordNumList(String type, int num) {
        Map<String, Object> result = new HashMap<>();
        if (type.equals("day")) {
            List<Integer> datas = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(new Date());
            calendarStart.set(Calendar.HOUR_OF_DAY,0);
            calendarStart.set(Calendar.MINUTE, 0);
            calendarStart.set(Calendar.SECOND, 0);
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(new Date());
            calendarEnd.set(Calendar.HOUR_OF_DAY,23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            calendarStart.add(Calendar.DATE, -num + 1);
            calendarEnd.add(Calendar.DATE, -num + 1);
            SimpleDateFormat format = new SimpleDateFormat("M月d日");
            for (int i = 0 ; i < num; i++) {
                datas.add(recordRepository.countAllByStartTimeBetween(calendarStart.getTime(), calendarEnd.getTime()));
                labels.add(format.format(calendarStart.getTime()));
                calendarStart.add(Calendar.DATE, 1);
                calendarEnd.add(Calendar.DATE, 1);
            }
            result.put("labels", labels);
            result.put("datas", datas);
        }
        if (type.equals("month")) {
            List<Integer> datas = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(new Date());
            calendarStart.set(Calendar.DAY_OF_MONTH, 1);
            calendarStart.set(Calendar.HOUR_OF_DAY,0);
            calendarStart.set(Calendar.MINUTE, 0);
            calendarStart.set(Calendar.SECOND, 0);
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(calendarStart.getTime());
            calendarEnd.set(Calendar.HOUR_OF_DAY,23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            calendarStart.add(Calendar.MONTH, -num + 1);
            calendarEnd.add(Calendar.MONTH, -num + 1);
            calendarEnd.add(Calendar.MONTH, 1);
            calendarEnd.add(Calendar.DATE, -1);
            SimpleDateFormat format = new SimpleDateFormat("YYYY年M月");
            for (int i = 0 ; i < num; i++) {
                datas.add(recordRepository.countAllByStartTimeBetween(calendarStart.getTime(), calendarEnd.getTime()));
                labels.add(format.format(calendarStart.getTime()));
                calendarStart.add(Calendar.MONTH, 1);
                calendarEnd.setTime(calendarStart.getTime());
                calendarEnd.set(Calendar.HOUR_OF_DAY,23);
                calendarEnd.set(Calendar.MINUTE, 59);
                calendarEnd.set(Calendar.SECOND, 59);
                calendarEnd.add(Calendar.MONTH, 1);
                calendarEnd.add(Calendar.DATE, -1);
            }
            result.put("labels", labels);
            result.put("datas", datas);
        }
        if (type.equals("year")) {
            List<Integer> datas = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(new Date());
            calendarStart.set(Calendar.MONTH, 0);
            calendarStart.set(Calendar.DAY_OF_MONTH, 1);
            calendarStart.set(Calendar.HOUR_OF_DAY,0);
            calendarStart.set(Calendar.MINUTE, 0);
            calendarStart.set(Calendar.SECOND, 0);
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(calendarStart.getTime());
            calendarEnd.set(Calendar.HOUR_OF_DAY,23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            calendarStart.add(Calendar.YEAR, -num + 1);
            calendarEnd.add(Calendar.YEAR, -num + 1);
            calendarEnd.add(Calendar.YEAR, 1);
            calendarEnd.add(Calendar.DATE, -1);
            SimpleDateFormat format = new SimpleDateFormat("YYYY年");
            for (int i = 0 ; i < num; i++) {
                datas.add(recordRepository.countAllByStartTimeBetween(calendarStart.getTime(), calendarEnd.getTime()));
                labels.add(format.format(calendarStart.getTime()));
                calendarStart.add(Calendar.YEAR, 1);
                calendarEnd.setTime(calendarStart.getTime());
                calendarEnd.set(Calendar.HOUR_OF_DAY,23);
                calendarEnd.set(Calendar.MINUTE, 59);
                calendarEnd.set(Calendar.SECOND, 59);
                calendarEnd.add(Calendar.YEAR, 1);
                calendarEnd.add(Calendar.DATE, -1);
            }
            result.put("labels", labels);
            result.put("datas", datas);
        }
        return result;
    }

    @Override
    public Map<String, Object> getAppLoginNumList(String type, int num) {
        Map<String, Object> result = new HashMap<>();
        if (type.equals("day")) {
            List<Integer> datas = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(new Date());
            calendarStart.set(Calendar.HOUR_OF_DAY,0);
            calendarStart.set(Calendar.MINUTE, 0);
            calendarStart.set(Calendar.SECOND, 0);
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(new Date());
            calendarEnd.set(Calendar.HOUR_OF_DAY,23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            calendarStart.add(Calendar.DATE, -num + 1);
            calendarEnd.add(Calendar.DATE, -num + 1);
            SimpleDateFormat format = new SimpleDateFormat("M月d日");
            for (int i = 0 ; i < num; i++) {
                datas.add(appLoginRepository.countAppLoginModelByTimeStampBetween(calendarStart.getTime(), calendarEnd.getTime()));
                labels.add(format.format(calendarStart.getTime()));
                calendarStart.add(Calendar.DATE, 1);
                calendarEnd.add(Calendar.DATE, 1);
            }
            result.put("labels", labels);
            result.put("datas", datas);
        }
        if (type.equals("month")) {
            List<Integer> datas = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(new Date());
            calendarStart.set(Calendar.DAY_OF_MONTH, 1);
            calendarStart.set(Calendar.HOUR_OF_DAY,0);
            calendarStart.set(Calendar.MINUTE, 0);
            calendarStart.set(Calendar.SECOND, 0);
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(calendarStart.getTime());
            calendarEnd.set(Calendar.HOUR_OF_DAY,23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            calendarStart.add(Calendar.MONTH, -num + 1);
            calendarEnd.add(Calendar.MONTH, -num + 1);
            calendarEnd.add(Calendar.MONTH, 1);
            calendarEnd.add(Calendar.DATE, -1);
            SimpleDateFormat format = new SimpleDateFormat("YYYY年M月");
            for (int i = 0 ; i < num; i++) {
                datas.add(appLoginRepository.countAppLoginModelByTimeStampBetween(calendarStart.getTime(), calendarEnd.getTime()));
                labels.add(format.format(calendarStart.getTime()));
                calendarStart.add(Calendar.MONTH, 1);
                calendarEnd.setTime(calendarStart.getTime());
                calendarEnd.set(Calendar.HOUR_OF_DAY,23);
                calendarEnd.set(Calendar.MINUTE, 59);
                calendarEnd.set(Calendar.SECOND, 59);
                calendarEnd.add(Calendar.MONTH, 1);
                calendarEnd.add(Calendar.DATE, -1);
            }
            result.put("labels", labels);
            result.put("datas", datas);
        }
        if (type.equals("year")) {
            List<Integer> datas = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(new Date());
            calendarStart.set(Calendar.MONTH, 0);
            calendarStart.set(Calendar.DAY_OF_MONTH, 1);
            calendarStart.set(Calendar.HOUR_OF_DAY,0);
            calendarStart.set(Calendar.MINUTE, 0);
            calendarStart.set(Calendar.SECOND, 0);
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(calendarStart.getTime());
            calendarEnd.set(Calendar.HOUR_OF_DAY,23);
            calendarEnd.set(Calendar.MINUTE, 59);
            calendarEnd.set(Calendar.SECOND, 59);
            calendarStart.add(Calendar.YEAR, -num + 1);
            calendarEnd.add(Calendar.YEAR, -num + 1);
            calendarEnd.add(Calendar.YEAR, 1);
            calendarEnd.add(Calendar.DATE, -1);
            SimpleDateFormat format = new SimpleDateFormat("YYYY年");
            for (int i = 0 ; i < num; i++) {
                datas.add(appLoginRepository.countAppLoginModelByTimeStampBetween(calendarStart.getTime(), calendarEnd.getTime()));
                labels.add(format.format(calendarStart.getTime()));
                calendarStart.add(Calendar.YEAR, 1);
                calendarEnd.setTime(calendarStart.getTime());
                calendarEnd.set(Calendar.HOUR_OF_DAY,23);
                calendarEnd.set(Calendar.MINUTE, 59);
                calendarEnd.set(Calendar.SECOND, 59);
                calendarEnd.add(Calendar.YEAR, 1);
                calendarEnd.add(Calendar.DATE, -1);
            }
            result.put("labels", labels);
            result.put("datas", datas);
        }
        return result;
    }

    @Override
    public Map<String, Object> getRecordZone() {
        List<Object> findResult = recordRepository.findProvinceByProvinces();
        List<String> labelResult = new ArrayList<>();
        List<Integer> countResult = new ArrayList<>();
        for (Object listData: findResult) {
            String jsonStr = JSON.toJSONString(listData);
            JSONArray data = JSON.parseArray(jsonStr);
            countResult.add((Integer) data.get(0));
            labelResult.add((String) data.get(1));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("labels", labelResult);
        result.put("datas", countResult);
        return result;
    }
}

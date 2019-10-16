package com.toby.services;

import java.util.List;
import java.util.Map;

public interface StatisticalService {

    Map<String, Object> getInfo();

    Map<String, Object> getRecordNumList(String type, int num);

    Map<String, Object> getAppLoginNumList(String type, int num);

    List<Integer> getRecordType();

    Map<String, Object> getRecordZone();
}

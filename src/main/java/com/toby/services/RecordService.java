package com.toby.services;

import com.toby.model.DetailModel;
import com.toby.model.RecordModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface RecordService {

    @Transactional
    String createNewRecord(RecordModel record);

    @Transactional
    RecordModel updateRecord(RecordModel record);

    RecordModel updateRecordLocation(RecordModel record);

    @Transactional
    boolean updateSpecialRecord(RecordModel record, List<DetailModel> details);

    Page<RecordModel> getAllRecord(int page, int size, Sort sort);

    Page<RecordModel> getAllRecordByTime(Date startTime, Date endTime, int page, int size, Sort sort);

    Page<RecordModel> getAllRecordByProvince(String province, int page, int size, Sort sort);

    Page<RecordModel> getAllRecordByCity(String city, int page, int size, Sort sort);

    Page<RecordModel> getAllRecordByZone(String zone, int page, int size, Sort sort);

    Page<RecordModel> getAllRecordByAlarm(boolean alarm, int page, int size, Sort sort);

    Page<RecordModel> getAllRecordByType(int type, int page, int size, Sort sort);

    int getTotalCount(String equipAddress, int type);

    int getTotalCountOverThreshold(String equipAddress, boolean alarm, int type);

    Page<RecordModel> getAllRecordByEquipAddress(String equipAddress, int page, int size, Sort sort);

    void deleteRecord(String id);
}

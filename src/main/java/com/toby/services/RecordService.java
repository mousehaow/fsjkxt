package com.toby.services;

import com.toby.model.RecordModel;
import org.springframework.transaction.annotation.Transactional;

public interface RecordService {

    @Transactional
    String createNewRecord(RecordModel record);

    @Transactional
    RecordModel updateRecord(RecordModel record);

}

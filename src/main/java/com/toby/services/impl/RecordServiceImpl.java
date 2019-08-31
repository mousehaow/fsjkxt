package com.toby.services.impl;

import com.toby.model.RecordModel;
import com.toby.repository.RecordRepository;
import com.toby.services.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Override
    public String createNewRecord(RecordModel record) {
        return recordRepository.saveAndFlush(record).getId();
    }

    @Override
    public RecordModel updateRecord(RecordModel record) {
        return recordRepository.saveAndFlush(record);
    }


}

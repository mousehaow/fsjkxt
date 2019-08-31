package com.toby.services.impl;

import com.toby.model.DetailModel;
import com.toby.model.RecordModel;
import com.toby.repository.DetailRepository;
import com.toby.repository.RecordRepository;
import com.toby.services.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetailServiceImpl implements DetailService {

    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Override
    public String addNewDetail(DetailModel detail) {
        RecordModel record = recordRepository.getOne(detail.getRecordId());
        record.setCount(record.getCount() + 1);
        record.setEndTime(detail.getTimeStamp());
        record.setTotalDose(record.getTotalDose() + detail.getValue() / 3600);
        recordRepository.saveAndFlush(record);
        return detailRepository.saveAndFlush(detail).getId();
    }
}

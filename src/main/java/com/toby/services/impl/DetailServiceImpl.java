package com.toby.services.impl;

import com.toby.model.DetailModel;
import com.toby.model.RecordModel;
import com.toby.repository.DetailRepository;
import com.toby.repository.RecordRepository;
import com.toby.services.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

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
        if (detail.getValue() > record.getThresholdValue()) {
            record.setOverThreshold(true);
        }
        recordRepository.saveAndFlush(record);
        return detailRepository.saveAndFlush(detail).getId();
    }

    @Override
    public List<DetailModel> getAllDetail(String recordId) {
        Sort sort = new Sort(Sort.Direction.DESC, "timeStamp");
        return detailRepository.findAll(new Specification<DetailModel>() {
            @Override
            public Predicate toPredicate(Root<DetailModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition1 = criteriaBuilder.equal(root.get("recordId"), recordId);
                return criteriaQuery.where(condition1).getRestriction();
            }
        }, sort);
    }
}

package com.toby.services.impl;

import com.toby.model.DetailModel;
import com.toby.model.EquipModel;
import com.toby.model.RecordModel;
import com.toby.repository.DetailRepository;
import com.toby.repository.RecordRepository;
import com.toby.services.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private DetailRepository detailRepository;

    @Override
    public String createNewRecord(RecordModel record) {
        return recordRepository.saveAndFlush(record).getId();
    }

    @Override
    public RecordModel updateRecord(RecordModel record) {
        return recordRepository.saveAndFlush(record);
    }

    @Override
    public RecordModel updateRecordLocation(RecordModel record) {
        RecordModel example = new RecordModel();
        example.setId(record.getId());
        Optional<RecordModel> optOld = recordRepository.findOne(Example.of(example));
        if (!optOld.isPresent()) {
            return null;
        }
        RecordModel old = optOld.get();
        old.setCountry(record.getCountry());
        old.setProvince(record.getProvince());
        old.setCity(record.getCity());
        old.setLocalDes(record.getLocalDes());
        old.setLatitude(record.getLatitude());
        old.setLongitude(record.getLongitude());
        return recordRepository.saveAndFlush(old);
    }

    @Override
    public boolean updateSpecialRecord(RecordModel record, List<DetailModel> details) {
        RecordModel newModel = recordRepository.saveAndFlush(record);
        boolean overThresholdValue = false;
        double totalDose = 0;
        for (DetailModel model: details) {
            model.setRecordId(newModel.getId());
            detailRepository.saveAndFlush(model);
            if (model.getValue() > record.getThresholdValue()) {
                overThresholdValue = true;
            }
            totalDose += model.getValue();
        }
        newModel.setCount(details.size());
        newModel.setEndTime(details.get(details.size() - 1).getTimeStamp());
        newModel.setTotalDose(totalDose / 3600);
        newModel.setOverThreshold(overThresholdValue);
        recordRepository.saveAndFlush(newModel);
        return true;
    }

    @Override
    public Page<RecordModel> getAllRecord(int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        return recordRepository.findAll(pageable);
    }



    @Override
    public Page<RecordModel> getAllRecordByTime(Date startTime, Date endTime, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<RecordModel> specification = new Specification<RecordModel>() {
            @Override
            public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition1 = criteriaBuilder.greaterThan(root.get("startTime"), startTime);
                Predicate condition2 = criteriaBuilder.lessThan(root.get("startTime"), endTime);

                return criteriaQuery.where(condition1, condition2).getRestriction();
            }
        };
        return recordRepository.findAll(specification, pageable);
    }

    @Override
    public Page<RecordModel> getAllRecordByProvince(String province, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<RecordModel> specification = new Specification<RecordModel>() {
            @Override
            public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition1 = criteriaBuilder.equal(root.get("province"), province);
                return criteriaQuery.where(condition1).getRestriction();
            }
        };
        return recordRepository.findAll(specification, pageable);
    }

    @Override
    public Page<RecordModel> getAllRecordByCity(String city, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<RecordModel> specification = new Specification<RecordModel>() {
            @Override
            public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition1 = criteriaBuilder.equal(root.get("city"), city);
                return criteriaQuery.where(condition1).getRestriction();
            }
        };
        return recordRepository.findAll(specification, pageable);
    }

    @Override
    public Page<RecordModel> getAllRecordByZone(String zone, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<RecordModel> specification = new Specification<RecordModel>() {
            @Override
            public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition1 = criteriaBuilder.like(root.get("zone"), zone);
                return criteriaQuery.where(condition1).getRestriction();
            }
        };
        return recordRepository.findAll(specification, pageable);
    }

    @Override
    public Page<RecordModel> getAllRecordByAlarm(boolean alarm, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<RecordModel> specification = new Specification<RecordModel>() {
            @Override
            public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition1 = criteriaBuilder.equal(root.get("overThreshold"), alarm);
                return criteriaQuery.where(condition1).getRestriction();
            }
        };
        return recordRepository.findAll(specification, pageable);
    }

    @Override
    public Page<RecordModel> getAllRecordByType(int type, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<RecordModel> specification = new Specification<RecordModel>() {
            @Override
            public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition1 = criteriaBuilder.equal(root.get("recordType"), type);
                return criteriaQuery.where(condition1).getRestriction();
            }
        };
        return recordRepository.findAll(specification, pageable);
    }

    @Override
    public int getTotalCount(String equipAddress, int type) {
        if (type < 0) {
            return (int) recordRepository.count(new Specification<RecordModel>() {
                @Override
                public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Predicate condition1 = criteriaBuilder.equal(root.get("equipAddress"), equipAddress);
                    return criteriaQuery.where(condition1).getRestriction();
                }
            });
        }
        return (int) recordRepository.count(new Specification<RecordModel>() {
            @Override
            public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition1 = criteriaBuilder.equal(root.get("equipAddress"), equipAddress);
                Predicate condition2 = criteriaBuilder.equal(root.get("recordType"), type);
                return criteriaQuery.where(condition1, condition2).getRestriction();
            }
        });
    }

    @Override
    public int getTotalCountOverThreshold(String equipAddress, boolean alarm, int type) {
        if (type < 0) {
            return (int) recordRepository.count(new Specification<RecordModel>() {
                @Override
                public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Predicate condition1 = criteriaBuilder.equal(root.get("equipAddress"), equipAddress);
                    Predicate condition2 = criteriaBuilder.equal(root.get("overThreshold"), alarm);
                    return criteriaQuery.where(condition1, condition2).getRestriction();
                }
            });
        }
        return (int) recordRepository.count(new Specification<RecordModel>() {
            @Override
            public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition1 = criteriaBuilder.equal(root.get("equipAddress"), equipAddress);
                Predicate condition2 = criteriaBuilder.equal(root.get("recordType"), type);
                Predicate condition3 = criteriaBuilder.equal(root.get("overThreshold"), alarm);
                return criteriaQuery.where(condition1, condition2, condition3).getRestriction();
            }
        });
    }

    @Override
    public Page<RecordModel> getAllRecordByEquipAddress(String equipAddress, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        return recordRepository.findAll(new Specification<RecordModel>() {
            @Override
            public Predicate toPredicate(Root<RecordModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition1 = criteriaBuilder.equal(root.get("equipAddress"), equipAddress);
                return criteriaQuery.where(condition1).getRestriction();
            }
        }, pageable);
    }

    @Override
    public void deleteRecord(String id) {
        recordRepository.deleteById(id);
    }
}

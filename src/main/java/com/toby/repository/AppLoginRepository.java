package com.toby.repository;

import com.toby.model.AppLoginModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface AppLoginRepository extends JpaRepository<AppLoginModel, String>, JpaSpecificationExecutor<AppLoginModel> {
    int countAppLoginModelByTimeStampBetween(Date startTime, Date endTime);
}

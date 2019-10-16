package com.toby.repository;

import com.toby.model.RecordModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<RecordModel, String>, JpaSpecificationExecutor<RecordModel> {

   int countAllByStartTimeBetween(Date startTime, Date endTime);

   @Query("select sum (1) as num,t.province as province from RecordModel t where t.recordType = 0 group by t.province")
   List<Object> findProvinceByProvinces();
}

package com.toby.repository;

import com.toby.model.SupportInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportInfoRepository extends JpaRepository<SupportInfoModel, String>, JpaSpecificationExecutor<SupportInfoModel> {
}

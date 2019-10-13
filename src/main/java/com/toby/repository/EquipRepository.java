package com.toby.repository;

import com.toby.model.EquipModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipRepository extends JpaRepository<EquipModel, String> {
    EquipModel getByLastRecordId(String recrodId);
}

package com.toby.repository;


import com.toby.model.DetailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailRepository extends JpaRepository<DetailModel, String> {
}

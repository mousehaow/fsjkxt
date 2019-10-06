package com.toby.services;

import com.toby.model.DetailModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DetailService {

    @Transactional
    String addNewDetail(DetailModel detail);

    List<DetailModel> getAllDetail(String recordId);
}

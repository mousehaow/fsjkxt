package com.toby.services;

import com.toby.model.DetailModel;
import org.springframework.transaction.annotation.Transactional;

public interface DetailService {

    @Transactional
    public String addNewDetail(DetailModel detail);
}

package com.toby.services.impl;


import com.toby.model.AppLoginModel;
import com.toby.repository.AppLoginRepository;
import com.toby.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppLoginRepository appLoginRepository;

    @Override
    public String insertNewAppLoginRecord(AppLoginModel loginModel) {
        return appLoginRepository.saveAndFlush(loginModel).getId();
    }
}

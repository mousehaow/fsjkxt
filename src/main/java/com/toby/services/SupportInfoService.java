package com.toby.services;

import com.toby.model.SupportInfoModel;

public interface SupportInfoService {

    SupportInfoModel getInfo();
    SupportInfoModel updateInfo(SupportInfoModel infoModel);
}

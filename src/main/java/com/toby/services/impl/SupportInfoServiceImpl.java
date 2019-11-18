package com.toby.services.impl;

import com.toby.model.SupportInfoModel;
import com.toby.repository.SupportInfoRepository;
import com.toby.services.SupportInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SupportInfoServiceImpl implements SupportInfoService {

    @Autowired
    private SupportInfoRepository supportInfoRepository;

    @Override
    public SupportInfoModel getInfo() {
        SupportInfoModel example = new SupportInfoModel();
        Optional<SupportInfoModel> optOld = supportInfoRepository.findOne(Example.of(example));
        if (!optOld.isPresent()) {
            SupportInfoModel infoModel = new SupportInfoModel();
            infoModel.setBriefIntroduction("管理员尚未编辑");
            infoModel.setInstructions("管理员尚未编辑");
            infoModel.setAddress("管理员尚未编辑");
            infoModel.setPhoneNumber("管理员尚未编辑");
            return supportInfoRepository.saveAndFlush(infoModel);
        } else {
            return optOld.get();
        }
    }

    @Override
    public SupportInfoModel updateInfo(SupportInfoModel infoModel) {
        SupportInfoModel example = new SupportInfoModel();
        Optional<SupportInfoModel> optOld = supportInfoRepository.findOne(Example.of(example));
        if (!optOld.isPresent()) {
            return supportInfoRepository.saveAndFlush(infoModel);
        } else {
            SupportInfoModel old = optOld.get();
            if (infoModel.getPhoneNumber() != null) {
                old.setPhoneNumber(infoModel.getPhoneNumber());
            }
            if (infoModel.getAddress() != null) {
                old.setAddress(infoModel.getAddress());
            }
            if (infoModel.getBriefIntroduction() != null) {
                old.setBriefIntroduction(infoModel.getBriefIntroduction());
            }
            if (infoModel.getInstructions() != null) {
                old.setInstructions(infoModel.getInstructions());
            }
            return supportInfoRepository.saveAndFlush(old);
        }
    }
}

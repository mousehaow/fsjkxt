package com.toby.services.impl;


import com.toby.model.DetailModel;
import com.toby.model.EquipModel;
import com.toby.repository.EquipRepository;
import com.toby.services.EquipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipServiceImpl implements EquipService {

    @Autowired
    private EquipRepository equipRepository;

    @Override
    public void equipOnline(EquipModel equip) {
        EquipModel example = new EquipModel();
        example.setId(equip.getId());
        Optional<EquipModel> optOld = equipRepository.findOne(Example.of(example));
        if (!optOld.isPresent()) {
            equipRepository.saveAndFlush(equip);
            return;
        }
        EquipModel old = optOld.get();
        old.setLastRecordId(equip.getLastRecordId());
        if (equip.getCountry() != null) {
            old.setCountry(equip.getCountry());
        }
        if (equip.getProvince() != null) {
            old.setProvince(equip.getProvince());
        }
        if (equip.getCity() != null) {
            old.setCity(equip.getCity());
        }
        if (equip.getLocalDes() != null) {
            old.setLocalDes(equip.getLocalDes());
        }
        if (equip.getLatitude() != null) {
            old.setLatitude(equip.getLatitude());
        }
        if (equip.getLongitude() != null) {
            old.setLongitude(equip.getLongitude());
        }
        if (equip.getTimeStamp() != null) {
            old.setTimeStamp(equip.getTimeStamp());
        }
        if (equip.getThresholdValue() != null) {
            old.setThresholdValue(equip.getThresholdValue());
        }
        equipRepository.saveAndFlush(old);
    }

    @Override
    public void updateEquip(DetailModel detailModel) {
        EquipModel example = new EquipModel();
        example.setId(detailModel.getEquipAddress());
        Optional<EquipModel> optOld = equipRepository.findOne(Example.of(example));
        if (!optOld.isPresent()) {
            return;
        }
        EquipModel equipModel = optOld.get();
        equipModel.setTimeStamp(detailModel.getTimeStamp());
        equipModel.setLatitude(detailModel.getLatitude());
        equipModel.setLongitude(detailModel.getLongitude());
        equipModel.setValue(detailModel.getValue());
        equipModel.setElectricQuantity(detailModel.getElectricQuantity());
        equipModel.setOnline(true);
        equipRepository.saveAndFlush(equipModel);
    }

    @Override
    public void equipOffline(String recordId) {
        EquipModel equipModel = equipRepository.getByLastRecordId(recordId);
        if (equipModel != null) {
            equipModel.setOnline(false);
            equipRepository.saveAndFlush(equipModel);
        }
    }

    @Override
    public List<EquipModel> getAllEquips() {
        Sort sort = new Sort(Sort.Direction.DESC, "online");
        return equipRepository.findAll(sort);
    }


}

package com.toby.services;

import com.toby.model.DetailModel;
import com.toby.model.EquipModel;

import java.util.List;

public interface EquipService {

    void equipOnline(EquipModel equip);

    void updateEquipLocation(EquipModel equip);

    void equipOffline(String recordId);

    void updateEquip(DetailModel detailModel);

    List<EquipModel> getAllEquips();

    int getEquipCount();
}

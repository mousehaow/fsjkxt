package com.toby.services;


import com.toby.model.User;

public interface UserService {

    User getById(String id);

    User getByName(String name);

    User saveNewUser(User user);
}

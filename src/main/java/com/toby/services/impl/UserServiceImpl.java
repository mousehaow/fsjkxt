package com.toby.services.impl;


import com.toby.model.User;
import com.toby.repository.UserRepository;
import com.toby.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getById(String id) {
        return userRepository.getUserById(id);
    }

    @Override
    public User getByName(String name) {
        return userRepository.getUserByUserName(name);
    }

    @Override
    public User saveNewUser(User user) {
        return userRepository.saveAndFlush(user);
    }
}

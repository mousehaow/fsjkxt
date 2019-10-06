package com.toby.services.impl;


import com.toby.model.RecordModel;
import com.toby.model.User;
import com.toby.repository.UserRepository;
import com.toby.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public Page<User> getAll(int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userRepository.findAll(pageable);
        for (User user: users.getContent()) {
            user.setPassword(null);
        }
        return users;
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public void changePassword(String id, String newPassword) {
        User user = userRepository.getUserById(id);
        user.setPassword(newPassword);
        userRepository.saveAndFlush(user);
    }
}

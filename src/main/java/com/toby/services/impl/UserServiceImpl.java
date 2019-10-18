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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
    public Page<User> searchAll(int page, int size, Sort sort, String search) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate condition = criteriaBuilder.like(root.get("userName"), "%"+search+"%");
                return criteriaQuery.where(condition).getRestriction();
            }
        };
        return userRepository.findAll(specification, pageable);
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

    @Override
    public boolean userNameIsExist(String userName) {
        int count = userRepository.countByUserNameEquals(userName);
        return count > 0;
    }
}

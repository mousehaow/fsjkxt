package com.toby.services;


import com.toby.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface UserService {

    User getById(String id);

    User getByName(String name);

    User saveNewUser(User user);

    Page<User> getAll(int page, int size, Sort sort);

    Page<User> searchAll(int page, int size, Sort sort, String search);

    void deleteUser(String id);

    void changePassword(String id, String newPassword);

    boolean userNameIsExist(String userName);
}

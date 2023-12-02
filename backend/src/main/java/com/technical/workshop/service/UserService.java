package com.technical.workshop.service;

import com.technical.workshop.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService  {
    List<User> listAll();
    User findById(String id);
    User create(User user);

    void delete(String id);

}

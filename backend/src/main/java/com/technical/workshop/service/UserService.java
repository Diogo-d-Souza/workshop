package com.technical.workshop.service;

import com.technical.workshop.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService  {
    public List<User> listAll();
    public User findById(String id);
    public User create(User user);
    public void delete(String id);
    public void update(User user);

}

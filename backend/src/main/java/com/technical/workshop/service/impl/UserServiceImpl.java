package com.technical.workshop.service.impl;

import com.technical.workshop.model.User;
import com.technical.workshop.repositories.UserRepository;
import com.technical.workshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> listAll() {
        return userRepository.findAll();
    }
}

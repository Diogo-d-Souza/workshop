package com.technical.workshop.service;

import com.technical.workshop.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService  {

    List<User> listAll();

}

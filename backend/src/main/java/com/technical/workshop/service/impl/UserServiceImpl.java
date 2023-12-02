package com.technical.workshop.service.impl;

import com.technical.exceptions.NotFoundException;
import com.technical.workshop.model.User;
import com.technical.workshop.repositories.UserRepository;
import com.technical.workshop.service.UserService;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<User> listAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            return user.get();
        }
        throw new NotFoundException("User not found");
    }

    @Override
    public User create(User user) {
        return userRepository.insert(user);
    }

    @Override
    public void delete(String id){
        userRepository.delete(findById(id));
    }

    public User update(User user){
        Optional<User> editedUser = userRepository.findById(user.getId());
        if(editedUser.isPresent()){
            newData(editedUser.get(), user);
            return userRepository.save(editedUser.get());
        }
        throw new NotFoundException("User not found");
    }

    private void newData(User editedUser, User user) {
        editedUser.setName(user.getName());
        editedUser.setEmail(user.getEmail());
        editedUser.setPassword(user.getPassword());
    }

}

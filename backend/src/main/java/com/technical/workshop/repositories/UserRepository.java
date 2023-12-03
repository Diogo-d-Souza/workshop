package com.technical.workshop.repositories;

import com.technical.workshop.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    UserDetails findByEmail(String email);
    User findByEmailLike(String email);
}

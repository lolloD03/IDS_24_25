package com.filiera.factory;

import com.filiera.model.dto.RegisterRequestUser;
import com.filiera.model.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserFactory {

    public User createUser(RegisterRequestUser request , PasswordEncoder passwordEncoder);

}

package com.filiera.services;

import com.filiera.factory.SellerFactory;
import com.filiera.factory.UserFactory;
import com.filiera.factory.UserSimpleFactory;
import com.filiera.model.dto.RegisterRequestUser;
import com.filiera.model.dto.RegisterRequestVenditore;
import com.filiera.model.users.User;
import com.filiera.repository.InMemoryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final InMemoryUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUserSimple(RegisterRequestUser request) {

        UserFactory factory = new UserSimpleFactory();

        return userRepository.save(factory.createUser(request , passwordEncoder));

    }

    public User createSeller(RegisterRequestVenditore request) {

        UserFactory factory = new SellerFactory();

        return userRepository.save(factory.createUser(request , passwordEncoder));

    }

}

package com.melardev.spring.rest.services;

import com.melardev.spring.rest.entities.Role;
import com.melardev.spring.rest.entities.User;
import com.melardev.spring.rest.models.SecurityUser;
import com.melardev.spring.rest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AppUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User Not Found"))))
                .map(SecurityUser::new);
    }

    public Long countSync() {
        return userRepository.count().block();
    }

    public Mono<User> save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles() == null)
            user.addRole(new Role("ROLE_USER"));

        return userRepository.save(user);
    }

    public Flux<User> saveAll(User... users) {
        if (users.length == 0)
            return Flux.empty();

        Flux<User> savedUsers = Flux.empty();
        for (int i = 0; i < users.length; i++) {
            savedUsers = savedUsers.concatWith(save(users[i]));
        }
        return savedUsers;
    }
}

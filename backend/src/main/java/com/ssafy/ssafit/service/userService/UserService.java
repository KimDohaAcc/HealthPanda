package com.ssafy.ssafit.service.userService;

import com.ssafy.ssafit.domain.User;

import java.util.Optional;

public interface UserService {
    User insertUser(User user);

    User updateUser(User user);

    Optional<User> findUserById(Long id);

    Optional<User> findUserByAccount(String account);

    Optional<User> extractUserFromToken(String sessionToken);

    boolean deleteUserById(Long id);
}

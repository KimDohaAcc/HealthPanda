package com.ssafy.ssafit.service.userService;

import com.ssafy.ssafit.domain.User;
import com.ssafy.ssafit.repository.UserRepository;
import com.ssafy.ssafit.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
    // 카카오로그인시 db에 유저로 저장 메소드
    // userRepository
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public User insertUser(User user) {

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user){
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findUserByAccount(String account) {
        return userRepository.findUserByAccount(account);
    }

    @Override
    public Optional<User> extractUserFromToken(String sessionToken) {
        return Optional.ofNullable(jwtUtil.extractUserIdFromToken(sessionToken))
                .flatMap(this::findUserById);
    }


//    @Override
//    public boolean deleteUserById(Long id) {
//        if (userRepository.existsById(id)) {
//            userRepository.deleteById(id);
//            return true;
//        } else {
//            return false;
//        }
//    }

    @Override
    public boolean deleteUserById(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
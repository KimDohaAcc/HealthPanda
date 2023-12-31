package com.ssafy.ssafit.controller;

import com.ssafy.ssafit.domain.Like;
import com.ssafy.ssafit.domain.User;
import com.ssafy.ssafit.domain.Video;
import com.ssafy.ssafit.service.likeService.LikeService;
import com.ssafy.ssafit.service.userService.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final UserService userService;

    @PostMapping("/auth/like")
    public ResponseEntity<Like> clickLike(@RequestBody Video video, HttpServletRequest request) {
        userService.extractUserFromToken(request.getHeader("Authorization"))
                .ifPresent(user -> {
                    likeService.getLikeByUserAndVideo(user, video)
                            .ifPresentOrElse(likeService::removeLike,
                                    () -> likeService.createLike(new Like(null, video, user))
                            );
                });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/auth/like/{id}")
    public ResponseEntity<List<Like>> getLikeByUser(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        if (user.isPresent()) {
            List<Like> likeList = likeService.getLikeByUser(user.get());

            return likeList.isEmpty() ?
                    new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                    new ResponseEntity<>(likeList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/like/{videoNum}")
    public ResponseEntity<Integer> getLikeCountByVideo(@PathVariable Long videoNum) {
        int likeCount = likeService.getLikeCountByVideo(videoNum);
        return new ResponseEntity<>(likeCount, HttpStatus.OK);
    }
}

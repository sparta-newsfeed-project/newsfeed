package com.example.newsfeed.follow.repository;

import com.example.newsfeed.follow.domain.Follow;
import com.example.newsfeed.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);

    Optional<Follow> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);

    Long countByFollower(User follower);

    Long countByFollowed(User followed);

    @Query(
            value = "SELECT f FROM Follow f JOIN FETCH f.followed fw WHERE f.follower = :user AND fw.deletedAt IS NULL",
            countQuery = "SELECT COUNT(f) FROM Follow f WHERE f.follower = :user"
    )
    Page<Follow> findAllByFollower(@Param("user") User user, Pageable pageable);

    @Query(
            value = "SELECT f FROM Follow f JOIN FETCH f.follower fw WHERE f.followed = :user AND fw.deletedAt IS NULL",
            countQuery = "SELECT COUNT(f) FROM Follow f WHERE f.followed = :user"
    )
    Page<Follow> findAllByFollowed(@Param("user")User user, Pageable pageable);

    @Modifying
    @Query("UPDATE Follow f " +
            "SET f.deletedAt = CURRENT_TIMESTAMP " +
            "WHERE (f.followed.id = :userId OR f.follower.id = :userId) AND f.deletedAt IS NULL")
    void softDeleteFollowsByUserId(@Param("userId") Long userId);
}

package com.panini.album.social;

import com.panini.album.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("""
            select f from Friendship f
            join fetch f.requester
            join fetch f.addressee
            where (f.requester = :a and f.addressee = :b) or (f.requester = :b and f.addressee = :a)
            """)
    Optional<Friendship> findBetween(@Param("a") User a, @Param("b") User b);

    @Query("""
            select f from Friendship f
            join fetch f.requester
            join fetch f.addressee
            where f.status = com.panini.album.social.FriendshipStatus.ACCEPTED
              and (f.requester.id = :userId or f.addressee.id = :userId)
            order by f.acceptedAt desc
            """)
    List<Friendship> findAcceptedForUser(@Param("userId") Long userId);

    @Query("""
            select f from Friendship f
            join fetch f.requester
            join fetch f.addressee
            where f.status = com.panini.album.social.FriendshipStatus.PENDING
              and f.addressee.id = :userId
            order by f.createdAt desc
            """)
    List<Friendship> findIncomingPending(@Param("userId") Long userId);

    @Query("""
            select f from Friendship f
            join fetch f.requester
            join fetch f.addressee
            where f.status = com.panini.album.social.FriendshipStatus.PENDING
              and f.requester.id = :userId
            order by f.createdAt desc
            """)
    List<Friendship> findOutgoingPending(@Param("userId") Long userId);
}

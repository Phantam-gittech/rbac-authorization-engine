package com.example.rbac_engine.repository;

import com.example.rbac_engine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User>  findByUsername(String username);
    @Query("""
            SELECT p.name FROM User u
            JOIN UserRole ur ON ur.user = u
            JOIN Role r ON ur.role = r
            JOIN RolePermission rp ON rp.role = r
            JOIN Permission p ON rp.permission = p
            WHERE u.username = :username
            """)
    List<String> findPermissionNamesByUsername(@Param("username") String username);

//    @Query("""
//            SELECT p.name FROM UserRole ur
//            JOIN RolePermission rp ON rp.role = ur.role
//            JOIN rp.permission p
//            WHERE ur.user.username = :username
//            """)
//    List<String> findPermissionNamesByUsername2(@Param("username") String username);


}

package org.nmng.library.manager.dao;

import org.nmng.library.manager.entity.Role;
import org.nmng.library.manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

//@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN u.roles ur JOIN ur.role r WHERE r = ?1")
    List<User> findByRole(Role role);

    @Query("SELECT u FROM User u JOIN u.roles ur JOIN ur.role r WHERE u.id = ?1 AND r = ?2")
    Optional<User> findByIdAndRole(Long id, Role role);

    Optional<User> findByIdentityNumber(String identityNumber);

    @Query("SELECT u FROM User u JOIN u.roles ur JOIN ur.role r WHERE u.identityNumber = ?1 AND r = ?2")
    Optional<User> findByIdentityNumberAndRole(String identityNumber, Role role);

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u JOIN u.roles ur JOIN ur.role r WHERE u.username = ?1 AND  r = ?2")
    Optional<User> findByUsernameAndRole(String username, Role role);

}

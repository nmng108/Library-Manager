package org.nmng.library.manager.dao;

import org.nmng.library.manager.entity.Role;
import org.nmng.library.manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

//@RepositoryRestResource(path = "roles")
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);

    @Query("SELECT r FROM User u JOIN u.roles ur JOIN ur.role r WHERE u = :user")
    List<Role> findByUser(User user);

    @Query("SELECT EXISTS (SELECT r FROM User u JOIN u.roles ur JOIN ur.role r WHERE u = :user AND r.name = :name)")
    boolean existsRoleByUserAndName(User user, String name);
}

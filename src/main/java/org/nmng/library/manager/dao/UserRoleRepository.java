package org.nmng.library.manager.dao;

import org.nmng.library.manager.entity.User;
import org.nmng.library.manager.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "user-roles")
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}

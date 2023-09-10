package org.nmng.library.manager.dao;

import org.nmng.library.manager.entity.Request;
import org.nmng.library.manager.entity.RequestStatus;
import org.nmng.library.manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByPatron(User patron);
    List<Request> findByStatus(RequestStatus status);
    List<Request> findByPatronAndStatus(User patron, RequestStatus status);
}

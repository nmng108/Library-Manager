package org.nmng.library.manager.dao;

import org.nmng.library.manager.entity.Request;
import org.nmng.library.manager.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestStatusRepository extends JpaRepository<RequestStatus, Integer> {
    public RequestStatus findByNameIgnoreCase(String statusName);
}

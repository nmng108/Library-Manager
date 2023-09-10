package org.nmng.library.manager.dao;

import org.nmng.library.manager.entity.Book;
import org.nmng.library.manager.entity.Request;
import org.nmng.library.manager.entity.RequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestDetailRepository extends JpaRepository<RequestDetail, Long> {
    List<RequestDetail> findByRequest(Request request);
}

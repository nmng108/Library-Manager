package org.nmng.library.manager.service;

import org.nmng.library.manager.dto.request.CreateRequestDto;
import org.nmng.library.manager.entity.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RequestService {
    ResponseEntity<?> getAll();

    ResponseEntity<?> getSpecifiedOne(String identifiable);

    ResponseEntity<?> getByPatronAndStatus(String patronIdentifiable, String status);

    ResponseEntity<?> create(CreateRequestDto dto);

    ResponseEntity<?> completeRequest(String requestIdentifiable);

    ResponseEntity<?> cancelRequest(String requestIdentifiable);

//    void updateExpiredRequest();

    ResponseEntity<?> delete(String identifiable);

    Request find(String identifiable);
}

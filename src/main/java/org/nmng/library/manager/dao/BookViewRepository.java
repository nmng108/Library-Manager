package org.nmng.library.manager.dao;

import org.nmng.library.manager.entity.BookView;
import org.nmng.library.manager.model.BookSearchModel;
import org.springframework.stereotype.Repository;

@Repository
public interface BookViewRepository extends CustomRepository<BookView, Integer, BookSearchModel> {
}

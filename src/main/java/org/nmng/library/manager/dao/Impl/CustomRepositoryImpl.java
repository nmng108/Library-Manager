package org.nmng.library.manager.dao.Impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.nmng.library.manager.dao.CustomRepository;
import org.nmng.library.manager.dto.request.SearchDto;
import org.nmng.library.manager.model.EntitySearchModel;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

import java.util.List;

public class CustomRepositoryImpl<T, ID, SearchModel extends EntitySearchModel<? extends SearchDto>>
        extends SimpleJpaRepository<T, ID>
        implements CustomRepository<T, ID, SearchModel> {

    //    @PersistenceContext
    private final EntityManager entityManager;

    // how can JpaEntityInformation get domain class?
    public CustomRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public CustomRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    protected <S, E extends T> Root<E> getRoot(CriteriaQuery<S> query, Class<E> domainClass) {
        Assert.notNull(query, "CriteriaQuery must not be null");
        Assert.notNull(domainClass, "Domain class must not be null");

        return query.from(domainClass);
    }

    /**
     * Accept query with no predicate. If using the 'and' operator (by default),
     * the result will be identical to 'findAll()'.
     * If we use 'or', the response will not contain any record, because a default predicate '1 != 1' will be added.
     *
     * @param searchModel BookSearchModel
     * @return List<Person>
     */
    @Override
    public List<T> findByCriteria(SearchModel searchModel) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(this.getDomainClass());
        Root<T> root = this.getRoot(query, this.getDomainClass());
        List<Predicate> predicates = searchModel.criteriaToPredicates(builder, root);

        query.select(root).where(searchModel.isUsingOr()
                ? builder.or(predicates.toArray(new Predicate[0])) // ???
                : builder.and(predicates.toArray(new Predicate[0])));

        List<Order> orders = searchModel.sortToCriteriaOrders(builder, root); // usually contains 1 element

        // search without pagination
        if (searchModel.getPagination() == null) {
            return entityManager.createQuery(query.orderBy(orders)).getResultList();
        }

        return entityManager.createQuery(query.orderBy(orders))
                .setFirstResult(searchModel.getPagination().firstRecordNumber())
                .setMaxResults(searchModel.getPagination().getNumberOfRecords())
                .getResultList();
    }

    public Long countByCriteria(SearchModel searchModel) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<T> root = query.from(this.getDomainClass());
        List<Predicate> predicates = searchModel.criteriaToPredicates(builder, root);

        query.select(builder.count(root)).where(searchModel.isUsingOr()
                ? builder.or(predicates.toArray(new Predicate[0])) // ???
                : builder.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getSingleResult();
    }
}

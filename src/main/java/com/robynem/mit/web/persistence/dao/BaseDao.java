package com.robynem.mit.web.persistence.dao;

import com.robynem.mit.web.persistence.entity.BaseEntity;
import com.robynem.mit.web.persistence.entity.PagedEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by roberto on 12/12/2015.
 */
@Service
public abstract class BaseDao {

    @Autowired
    protected SessionFactory sessionFactory;

    @Autowired
    @Qualifier("hibernateTemplate")
    protected HibernateTemplate hibernateTemplate;

    protected <T extends BaseEntity> PagedEntity<T> getPagingInfo(String hql, Map parameters, Integer pageSize, Integer currentPage, Session session) {
        PagedEntity<T> pagedEntity = new PagedEntity<T>();

        if (pageSize != null && currentPage != null) {
            Query countQuery = session.getNamedQuery(hql);

            this.setParameters(countQuery, parameters);

            List<Object[]> result = countQuery.list();

            if (result != null && result.size() > 0) {
                pagedEntity.setTotalRows(Long.valueOf(String.valueOf(result.get(0))));
            }

            if (pagedEntity.getTotalRows() <= pageSize) {
                pagedEntity.setCurrentPage(1);
            } else {
                pagedEntity.setCurrentPage(currentPage);
            }
        }

        return pagedEntity;
    }

    protected Query setParameters(Query query, Map parameters) {
        if (parameters != null) {
            parameters.keySet().stream().forEach(k -> {
                query.setParameter((String)k, parameters.get(k));
            });
        }

        return query;
    }
}

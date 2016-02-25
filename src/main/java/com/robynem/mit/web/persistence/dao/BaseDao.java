package com.robynem.mit.web.persistence.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

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
}

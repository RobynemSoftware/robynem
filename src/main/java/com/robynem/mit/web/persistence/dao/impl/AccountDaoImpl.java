package com.robynem.mit.web.persistence.dao.impl;

import com.robynem.mit.web.persistence.dao.AccountDao;
import com.robynem.mit.web.persistence.dao.BaseDao;
import com.robynem.mit.web.persistence.entity.BandComponentEntity;
import com.robynem.mit.web.persistence.entity.BandEntity;
import com.robynem.mit.web.persistence.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by roberto on 12/12/2015.
 */
@Service("accountDao")
public class AccountDaoImpl extends BaseDao implements AccountDao {

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public UserEntity addUser(UserEntity entity) {
        Long id = (Long)this.hibernateTemplate.save(entity);
        entity.setId(id);
        return entity;
    }

    @Override
    public UserEntity getUserById(Long id) {
        return this.hibernateTemplate.get(UserEntity.class, id);
    }

    @Override
    public UserEntity getUserByIdWithProfileImage(final Long id) {

        UserEntity userEntity = this.hibernateTemplate.execute(new HibernateCallback<UserEntity>() {
            @Override
            public UserEntity doInHibernate(Session session) throws HibernateException, SQLException {
                UserEntity userEntity = (UserEntity) session.get(UserEntity.class, id);

                Hibernate.initialize(userEntity.getProfileImage());

                return userEntity;
            }
        });

        return userEntity;
    }


    @Override
    public UserEntity getUserByEmailAddress(String emailAddress) {
        UserEntity entity = null;

        List<UserEntity> list = this.hibernateTemplate.findByNamedQueryAndNamedParam("@HQL_GET_USER_BY_EMAIL_ADDRESS", new String[]{"emailAddress"}, new Object[]{StringUtils.trim(emailAddress)});

        if (list != null && list.size() > 0) {
            entity = list.get(0);
        }

        return entity;
    }

    @Override
    public UserEntity getUserByFacebookId(Long facebookId) {
        UserEntity entity = null;

        List<UserEntity> list = this.hibernateTemplate.findByNamedQueryAndNamedParam("@HQL_GET_USER_BY_FACEBOOK_ID", new String[] {"facebookId"}, new Object[] {facebookId});

        if (list != null && list.size() > 0) {
            entity = list.get(0);
        }

        return entity;
    }

    @Override
    public UserEntity getUserByEmailAndPassword(String emailAddress, String password) {
        UserEntity entity = null;

        List<UserEntity> list = this.hibernateTemplate.findByNamedQueryAndNamedParam("@HQL_GET_USER_BY_EMAIL_AND_PASSWORD",
                new String[] {
                        "emailAddress",
                        "password"
                },
                new Object[] {
                        emailAddress,
                        password
                });

        if (list != null && list.size() > 0) {
            entity = list.get(0);
        }

        return entity;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void updateUser(UserEntity userEntity) {
        if (userEntity != null) {
            this.hibernateTemplate.update(userEntity);
        }
    }

    @Override
    public List<UserEntity> filterMusiciansByNameForAutocomplete(Long bandId, String name) {

        return this.hibernateTemplate.execute(session -> {
            List<UserEntity> result = null;

            Set<BandComponentEntity> bandComponents = ((BandEntity) session.get(BandEntity.class, bandId)).getComponents();

            Criteria criteria = session.createCriteria(UserEntity.class, "user");
            criteria = criteria.createAlias("user.profileImage", "profileImage", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.add(
                    Restrictions.and(
                        Restrictions.eq("user.musician", true),
                        Restrictions.or(Restrictions.like("user.firstName", name + "%"), Restrictions.like("user.lastName", name + "%"))
            ));
            criteria.add(Restrictions.eq("user.engagementAvailable", true));

            // Excludes actual band components
            if (bandComponents != null && bandComponents.size() > 0) {
                List<Long> compnentsIds = bandComponents.stream().map(c -> c.getUser().getId()).collect(Collectors.toList());

                criteria = criteria.add(Restrictions.not(Restrictions.in("user.id", compnentsIds)));
            }

            result = criteria.list();

            return result;
        });
    }


}

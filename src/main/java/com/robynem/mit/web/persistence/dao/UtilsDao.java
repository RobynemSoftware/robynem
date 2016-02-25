package com.robynem.mit.web.persistence.dao;

import com.robynem.mit.web.persistence.entity.BaseEntity;
import com.robynem.mit.web.persistence.entity.UserEntity;
import com.robynem.mit.web.util.PortalHelper;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.core.GenericTypeResolver;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by robyn_000 on 08/01/2016.
 */
@Service
public class UtilsDao<T extends BaseEntity> extends BaseDao {

    /**
     * Get the entity by id and fetch all relation attributes passed into the <code>attributesToFetch</code> parameter.
     * @param id
     * @param attributesToFetch
     * @return
     */
    public T getByIdWithFetchedObjects(final Class<T> clazz, final Serializable id, final String... attributesToFetch)  {
        T result = this.hibernateTemplate.execute(new HibernateCallback<T>() {
            @Override
            public T doInHibernate(Session session) throws HibernateException, SQLException {
                T entity;
                try {
                    T type = clazz.newInstance();

                    entity = (T) session.get(type.getClass(), id);

                    if (attributesToFetch != null) {
                        Method method = null;
                        for (String attr : attributesToFetch) {
                            method = entity.getClass().getMethod(String.format("get%s", StringUtils.capitalize(attr)));
                            Hibernate.initialize(method.invoke(entity));
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }


                return entity;
            }
        });

        return result;
    }

    /**
     * Deletes videos not belonging to any relation table.
     * @param videoIds
     */
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void deleteOrphanVideos(List<Long> videoIds) {
        this.hibernateTemplate.execute(session -> {

            String queryStr = "delete dest\n" +
                    "from mit_video dest\n" +
                    "\tleft join mit_bandvideo bv on dest.id = bv.videoId\n" +
                    "where bv.videoId is null and dest.id in " + PortalHelper.createInSqlClause(videoIds);

            Query query = session.createSQLQuery(queryStr);

            query.executeUpdate();

            return null;
        });
    }
}

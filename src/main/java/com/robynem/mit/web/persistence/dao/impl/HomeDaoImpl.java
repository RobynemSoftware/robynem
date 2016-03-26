package com.robynem.mit.web.persistence.dao.impl;

import com.robynem.mit.web.persistence.criteria.NewArtistsCriteria;
import com.robynem.mit.web.persistence.dao.BaseDao;
import com.robynem.mit.web.persistence.dao.HomeDao;
import com.robynem.mit.web.persistence.util.ArtistMapResult;
import com.robynem.mit.web.util.ArtistType;
import com.robynem.mit.web.util.EntityStatus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by robyn_000 on 26/03/2016.
 */
@Service
public class HomeDaoImpl extends BaseDao implements HomeDao {

    @Override
    public ArtistMapResult getNewArtists(NewArtistsCriteria criteria) {

        List<Map<String, Object>> list = this.hibernateTemplate.execute(session -> {

            StringBuilder queryBuilder = new StringBuilder();

            queryBuilder.append(String.format("select new map (b.id as %s, b.name as %s, bg.name as %s, bi.id as %s, '%s' as artistType)",
                    ArtistMapResult.ID,
                    ArtistMapResult.NAME,
                    ArtistMapResult.ARTIST_DESCRIPTION,
                    ArtistMapResult.IMAGE_ID,
                    ArtistType.BAND.toString()));
            queryBuilder.append("from BandEntity b\n" +
                    "        left join b.musicGenres bg\n" +
                    "        left join b.bandLogo bi ");
            queryBuilder.append(String.format("where b.status.code = '%s' ", EntityStatus.PUBLISHED.toString()));

            if (criteria != null) {
                if (StringUtils.isNotBlank(criteria.getPlaceId())) {
                    queryBuilder.append("and b.placeId = :placeId ");
                }
            }

            queryBuilder.append("order by b.firstPublishDate desc ");

            Query query = session.createQuery(queryBuilder.toString());

            if (criteria != null) {
                if (StringUtils.isNotBlank(criteria.getPlaceId())) {
                    query.setParameter("placeId", StringUtils.trimToEmpty(criteria.getPlaceId()));
                }
            }

            query.setMaxResults(18);
            query.setFirstResult(0);

            List<Map<String, Object>> result = query.list();

            // Normalizes result joining ARTIST_DESCRIPTION colum with all values returned in several rows by id
            Map<Long, Map<String, Object>> normalizedResult = new HashMap<Long, Map<String, Object>>();

            for (Map<String, Object> row : result) {
                // Retrieves the id column
                Long id = (Long) row.get(ArtistMapResult.ID);

                /*
                * If we don't have already normalized row for this id, we create it. ARTIST_DESCRIPTION colum will contain first value.
                * If we have normalized row for this id, we retrieve current ARTIST_DESCRIPTION colum and we add value into normalized row
                * */
                if (!normalizedResult.containsKey(id)) {
                    normalizedResult.put(id, row);
                } else {
                    Map<String, Object> normalizedRow = normalizedResult.get(id);

                    normalizedRow.put(ArtistMapResult.ARTIST_DESCRIPTION, (String)normalizedRow.get(ArtistMapResult.ARTIST_DESCRIPTION) + ", " + (String)row.get(ArtistMapResult.ARTIST_DESCRIPTION));
                }
            }

            return (List<Map<String, Object>>)normalizedResult.values().stream().collect(Collectors.toList());

        });

        return new ArtistMapResult(list);
    }
}

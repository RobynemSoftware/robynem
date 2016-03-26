package com.robynem.mit.web.persistence.dao;

import com.robynem.mit.web.persistence.criteria.NewArtistsCriteria;
import com.robynem.mit.web.persistence.util.ArtistMapResult;

/**
 * Created by robyn_000 on 26/03/2016.
 */
public interface HomeDao {

    ArtistMapResult getNewArtists(NewArtistsCriteria criteria);
}

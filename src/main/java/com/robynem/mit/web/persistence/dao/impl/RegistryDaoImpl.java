package com.robynem.mit.web.persistence.dao.impl;

import com.robynem.mit.web.persistence.dao.BaseDao;
import com.robynem.mit.web.persistence.dao.RegistryDao;
import com.robynem.mit.web.persistence.entity.ClubGenreEntity;
import com.robynem.mit.web.persistence.entity.MusicGenreEntity;
import com.robynem.mit.web.persistence.entity.MusicalInstrumentEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by robyn_000 on 06/01/2016.
 */
@Service
public class RegistryDaoImpl extends BaseDao implements RegistryDao {
    @Override
    public List<MusicalInstrumentEntity> getAllMusicalInstruments() {
        return this.hibernateTemplate.findByNamedQuery("@HQL_GET_ALL_MUSICAL_INSTRUMNTS");
    }

    @Override
    public List<MusicGenreEntity> getAllMusicGenres() {
        return this.hibernateTemplate.findByNamedQuery("@HQL_GET_ALL_MUSIC_GENRES");
    }

    @Override
    public List<ClubGenreEntity> getAllClubGenres() {
        return this.hibernateTemplate.findByNamedQuery("@HQL_GET_ALL_CLUB_GENRES");
    }
}

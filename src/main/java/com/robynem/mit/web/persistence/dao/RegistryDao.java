package com.robynem.mit.web.persistence.dao;

import com.robynem.mit.web.persistence.entity.MusicGenreEntity;
import com.robynem.mit.web.persistence.entity.MusicalInstrumentEntity;

import java.util.List;

/**
 * Created by robyn_000 on 06/01/2016.
 */
public interface RegistryDao {

    List<MusicalInstrumentEntity> getAllMusicalInstruments();

    List<MusicGenreEntity> getAllMusicGenres();
}

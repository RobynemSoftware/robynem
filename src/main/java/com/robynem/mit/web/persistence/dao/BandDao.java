package com.robynem.mit.web.persistence.dao;

import com.robynem.mit.web.persistence.criteria.BandComponentsCriteria;
import com.robynem.mit.web.persistence.entity.BandEntity;
import com.robynem.mit.web.persistence.entity.UserEntity;
import com.robynem.mit.web.persistence.entity.VideoEntity;
import com.robynem.mit.web.util.OwnerType;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by robyn_000 on 13/01/2016.
 */
public interface BandDao {

    BandEntity createEmtpyBand(Long userId);

    BandEntity createStageVersion(BandEntity parentBand);

    /**
     * Overrides destination attributs with source's ones, setting destination id on copied relations.
     * @param source
     * @param destination
     */
    void copyBandData(BandEntity source, BandEntity destination);

    void copyBandData(Serializable sourceId, BandEntity destination);

    void update(BandEntity bandEntity);

    BandEntity getBandById(Long bandId);

    BandEntity addSelectedComponent(Long bandId, Long userId);

    BandEntity getBandToEdit(Long bandId);

    BandEntity getBandGeneralInfo(Long bandId);

    BandEntity getBandComponents(Long bandId);

    BandEntity getBandMedia(Long bandId);

    void saveComponentInstrument(Long bandId, Long userId, Long instrumentId, boolean selected);

    List<UserEntity> searchBandComponents(BandComponentsCriteria criteria);

    List<BandEntity> getOwnedBands(Long userId, OwnerType ownerType);

    void saveBandComponentSinger(Long bandId, Long bandComponentId, boolean value);

    void saveBandComponentDiscJockey(Long bandId, Long bandComponentId, boolean value);

    void saveBandVideos(Long bandId, Set<VideoEntity> videos);
}

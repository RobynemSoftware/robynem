package com.robynem.mit.web.persistence.dao;

import com.robynem.mit.web.persistence.criteria.AudioCriteria;
import com.robynem.mit.web.persistence.criteria.BandComponentsCriteria;
import com.robynem.mit.web.persistence.criteria.GalleryCriteria;
import com.robynem.mit.web.persistence.criteria.VideosCriteria;
import com.robynem.mit.web.persistence.entity.*;
import com.robynem.mit.web.persistence.util.BandMapResult;
import com.robynem.mit.web.persistence.util.VideoMapResult;
import com.robynem.mit.web.util.OwnerType;
import com.robynem.mit.web.util.PublishBandResult;
import com.robynem.mit.web.util.SmtpHelper;
import org.hibernate.Session;

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

    BandEntity createStageVersion(Long parentBandId);

    /**
     * Overrides destination attributs with source's ones, setting destination id on copied relations.
     * @param source
     * @param destination
     */
    void copyBandData(BandEntity source, BandEntity destination, Session session);

    void copyBandData(Long sourceId, BandEntity destination);

    void update(BandEntity bandEntity);

    BandEntity getBandById(Long bandId);

    BandEntity addSelectedComponent(Long bandId, Long userId, Long operationUserId);

    BandEntity removeComponent(Long bandId, Long userId, Long operationUserId);

    BandEntity getBandToEdit(Long bandId);

    BandEntity getBandGeneralInfo(Long bandId);

    BandEntity getBandComponents(Long bandId);

    BandEntity getBandMedia(Long bandId);

    void saveComponentInstrument(Long bandId, Long userId, Long instrumentId, boolean selected);

    List<UserEntity> searchBandComponents(BandComponentsCriteria criteria);

    List<BandEntity> getOwnedBands(Long userId, OwnerType ownerType);

    BandMapResult getPlayingBands(Long userId);

    void saveBandComponentSinger(Long bandId, Long bandComponentId, boolean value);

    void saveBandComponentDiscJockey(Long bandId, Long bandComponentId, boolean value);

    void saveBandVideos(Long bandId, Set<VideoEntity> videos);

    Long addBandVideo(Long bandId, VideoEntity videoEntity);

    void removeBandVideo(Long bandId, Long videoId);

    PublishBandResult publishBand(Long bandId, Long userId);

    Long getStageVideoId(Long publishedBandId, Long publishedVideoId);

    Long getStageGalleryImageId(Long publishedBandId, Long publishedImageId);

    Long getStageAudioId(Long publishedBandId, Long publishedAudioId);

    Long getStageBandComponentId(Long publishedBandId, Long publishedBandComponentId);

    String getBandStatusCode(Long bandId);

    Long addBandAudio(Long bandId, AudioEntity audioEntity);

    void removeBandAudio(Long bandId, Long audioId);

    PagedEntity<VideoEntity> getBandVideos(VideosCriteria criteria);

    PagedEntity<ImageEntity> getBandGallery(GalleryCriteria criteria);

    PagedEntity<AudioEntity> getBandAudios(AudioCriteria criteria);

    BandComponentEntity getBandComponent(Long bandId, Long userId);

    boolean isBandComponent(Long bandId, Long userId);

    void acceptDeclineBandComponentRequest(Long bandId, Long userId, boolean accept);
}

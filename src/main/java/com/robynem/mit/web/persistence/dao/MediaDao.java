package com.robynem.mit.web.persistence.dao;

import com.robynem.mit.web.persistence.entity.AudioEntity;
import com.robynem.mit.web.persistence.entity.ImageEntity;

import java.io.InputStream;

/**
 * Created by robyn_000 on 03/01/2016.
 */
public interface MediaDao {

    void updateUserProfileImage(Long userId, InputStream imageStream);

    void updateBandLogoImage(Long bandId, InputStream imageStream);

    void updateClubLogoImage(Long clubId, InputStream imageStream);

    Long addBandGalleryImage(Long bandId, InputStream imageStream);

    Long addClubGalleryImage(Long clubId, InputStream imageStream);

    void removeBandGalleryImage(Long bandId, Long imageId);

    void removeClubGalleryImage(Long clubId, Long imageId);

    ImageEntity getImageById(Long id);

    AudioEntity getAudioById(Long id);

    Long addBandAudio(Long bandId, InputStream audioStream, String name);

    //void removeBandAudio(Long bandId, Long audioId);
}

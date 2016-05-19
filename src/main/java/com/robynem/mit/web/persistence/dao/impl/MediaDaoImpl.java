package com.robynem.mit.web.persistence.dao.impl;

import com.robynem.mit.web.persistence.dao.BaseDao;
import com.robynem.mit.web.persistence.dao.MediaDao;
import com.robynem.mit.web.persistence.entity.*;
import com.robynem.mit.web.util.ImageHelper;
import com.robynem.mit.web.util.PortalHelper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.sampled.Port;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

/**
 * Created by robyn_000 on 03/01/2016.
 */
@Service
public class MediaDaoImpl extends BaseDao implements MediaDao {

    static final Logger LOG = LoggerFactory.getLogger(MediaDaoImpl.class);

    @Value("${media.audio.base-path}")
    private String audioBasePath;

    @Value("${media.bands-images.folder}")
    private String bandImagesBasePath;

    @Value("${media.image-small.width}")
    private int imageSmallWidth;

    @Value("${media.image-small.height}")
    private int imageSmallHeight;

    @Value("${media.image-medium.width}")
    private int imageMediumWidth;

    @Value("${media.image-medium.height}")
    private int imageMediumHeight;

    @Value("${media.image-large.width}")
    private int imageLargeWidth;

    @Value("${media.image-large.height}")
    private int imageLargeHeight;

    @Value("${media.image-format}")
    private String imageFormat;

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    /**
     * @deprecated
     */
    public void updateUserProfileImage_(Long userId, InputStream imageStream){

        /*try {
            // Creates unique file name
            String newFileName = String.valueOf(PortalHelper.getUniqueId());

            // Stores old images to delete if any
            List<String> imagesToDelete = new ArrayList<String>();

            // User file path
            String baseUserPath = String.valueOf(userId) + "/";
            String userFilePath = this.userImagesBasePath + baseUserPath;

            // Create new file names with suffix and extension
            String smallImageName = String.format("%s_%sx%s.%s",
                    String.valueOf(newFileName),
                    String.valueOf(this.imageSmallWidth),
                    String.valueOf(this.imageSmallHeight),
                    this.imageFormat);

            String mediumImageName = String.format("%s_%sx%s.%s",
                    String.valueOf(newFileName),
                    String.valueOf(this.imageMediumWidth),
                    String.valueOf(this.imageMediumHeight),
                    this.imageFormat);

            String largeImageName = String.format("%s_%sx%s.%s",
                    String.valueOf(newFileName),
                    String.valueOf(this.imageLargeWidth),
                    String.valueOf(this.imageLargeHeight),
                    this.imageFormat);

            String originalImageName = String.format("%s.%s",
                    String.valueOf(newFileName),
                    this.imageFormat);

            // Retrieves user entity
            UserEntity userEntity = this.hibernateTemplate.get(UserEntity.class, userId);

            // Retrieves user profile image if any
            ImageEntity profileImage = userEntity.getProfileImage();

            if (profileImage == null) {
                profileImage = new ImageEntity();
                profileImage.setCreated(Calendar.getInstance().getTime());
            }


            // Stores old images to delete
            if (StringUtils.isNotBlank(profileImage.getSmallFileName())) {
                imagesToDelete.add(String.format("%s%s%s",
                        this.userImagesBasePath,
                        StringUtils.trimToEmpty(profileImage.getFilePath()),
                        StringUtils.trimToEmpty(profileImage.getSmallFileName())));
            }

            if (StringUtils.isNotBlank(profileImage.getMediumFileName())) {
                imagesToDelete.add(String.format("%s%s%s",
                        this.userImagesBasePath,
                        StringUtils.trimToEmpty(profileImage.getFilePath()),
                        StringUtils.trimToEmpty(profileImage.getMediumFileName())));
            }

            if (StringUtils.isNotBlank(profileImage.getLargeFileName())) {
                imagesToDelete.add(String.format("%s%s%s",
                        this.userImagesBasePath,
                        StringUtils.trimToEmpty(profileImage.getFilePath()),
                        StringUtils.trimToEmpty(profileImage.getLargeFileName())));
            }

            if (StringUtils.isNotBlank(profileImage.getOriginalFileName())) {
                imagesToDelete.add(String.format("%s%s%s",
                        this.userImagesBasePath,
                        StringUtils.trimToEmpty(profileImage.getFilePath()),
                        StringUtils.trimToEmpty(profileImage.getOriginalFileName())));
            }

            // Execs updates
            profileImage.setUpdated(Calendar.getInstance().getTime());
            profileImage.setFilePath(baseUserPath);
            profileImage.setSmallFileName(smallImageName);
            profileImage.setMediumFileName(mediumImageName);
            profileImage.setLargeFileName(largeImageName);
            profileImage.setOriginalFileName(originalImageName);

            this.hibernateTemplate.saveOrUpdate(profileImage);

            userEntity.setProfileImage(profileImage);

            this.hibernateTemplate.update(userEntity);

            // Creates if not exists user file path
            ImageHelper.createDirectory(userFilePath);

            // Creates new files on file system
            String originalDestinationName = userFilePath + originalImageName;

            // Original
            ImageHelper.saveImage(imageStream, originalDestinationName);
            imageStream.close();

            // Small
            ImageHelper.scaleAndSaveImage(originalDestinationName,
                    userFilePath + smallImageName,
                    this.imageSmallWidth,
                    this.imageSmallHeight,
                    this.imageFormat);

            // Medium
            ImageHelper.scaleAndSaveImage(originalDestinationName,
                    userFilePath + mediumImageName,
                    this.imageMediumWidth,
                    this.imageMediumHeight,
                    this.imageFormat);

            // Large
            ImageHelper.scaleAndSaveImage(originalDestinationName,
                    userFilePath + largeImageName,
                    this.imageLargeWidth,
                    this.imageLargeHeight,
                    this.imageFormat);

            // Deletes old images
            for (String imageToDelete : imagesToDelete) {
                ImageHelper.deleteImage(imageToDelete);
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }*/
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void updateUserProfileImage(Long userId, InputStream imageStream){


        try {

            // Retrieves user entity
            UserEntity userEntity = this.hibernateTemplate.get(UserEntity.class, userId);

            // Retrieves user profile image if any
            ImageEntity profileImage = userEntity.getProfileImage();

            if (profileImage == null) {
                profileImage = new ImageEntity();
                profileImage.setCreated(Calendar.getInstance().getTime());
            }

            try (
                 // Small
                 InputStream smallFile = ImageHelper.scaleImage(imageStream,
                         this.imageSmallWidth,
                         this.imageSmallHeight,
                         this.imageFormat);

            // Medium
                 InputStream mediumFile = ImageHelper.scaleImage(imageStream,
                    this.imageMediumWidth,
                    this.imageMediumHeight,
                    this.imageFormat);

            // Large
                 InputStream largeFile = ImageHelper.scaleImage(imageStream,
                    this.imageLargeWidth,
                    this.imageLargeHeight,
                    this.imageFormat)) {

                // Execs updates
                profileImage.setUpdated(Calendar.getInstance().getTime());
                profileImage.setSmallFile(PortalHelper.getBlob(smallFile));
                profileImage.setMediumFile(PortalHelper.getBlob(mediumFile));
                profileImage.setLargeFile(PortalHelper.getBlob(largeFile));
                profileImage.setOriginalFile(PortalHelper.getBlob(imageStream));

                userEntity.setProfileImage(profileImage);
                userEntity.setUpdated(Calendar.getInstance().getTime());

                this.hibernateTemplate.update(userEntity);

            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (imageStream != null) {
                try {
                    imageStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Saves a new logo image for that band id.
     * @param bandId
     * @param imageStream
     */
    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void updateBandLogoImage(Long bandId, InputStream imageStream) {
        try {

            // Retrieves band entity
            BandEntity bandEntity = this.hibernateTemplate.get(BandEntity.class, bandId);

            // Retrieves user profile image if any
            ImageEntity logoImage = bandEntity.getBandLogo();

            if (logoImage == null) {
                logoImage = new ImageEntity();
                logoImage.setCreated(Calendar.getInstance().getTime());
            }

            try (
            // Small
            InputStream smallFile = ImageHelper.scaleImage(imageStream,
                    this.imageSmallWidth,
                    this.imageSmallHeight,
                    this.imageFormat);

            // Medium
            InputStream mediumFile = ImageHelper.scaleImage(imageStream,
                    this.imageMediumWidth,
                    this.imageMediumHeight,
                    this.imageFormat);

            // Large
            InputStream largeFile = ImageHelper.scaleImage(imageStream,
                    this.imageLargeWidth,
                    this.imageLargeHeight,
                    this.imageFormat)) {

                // Execs updates
                logoImage.setUpdated(Calendar.getInstance().getTime());
                logoImage.setUpdated(Calendar.getInstance().getTime());
                logoImage.setSmallFile(PortalHelper.getBlob(smallFile));
                logoImage.setMediumFile(PortalHelper.getBlob(mediumFile));
                logoImage.setLargeFile(PortalHelper.getBlob(largeFile));
                logoImage.setOriginalFile(PortalHelper.getBlob(imageStream));

                bandEntity.setBandLogo(logoImage);
                bandEntity.setUpdated(Calendar.getInstance().getTime());

                this.hibernateTemplate.update(bandEntity);
            }




        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (imageStream != null) {
                try {
                    imageStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void updateClubLogoImage(Long clubId, InputStream imageStream) {
        try {

            // Retrieves club entity
            ClubEntity clubEntity = this.hibernateTemplate.get(ClubEntity.class, clubId);

            // Retrieves user profile image if any
            ImageEntity logoImage = clubEntity.getClubLogo();

            if (logoImage == null) {
                logoImage = new ImageEntity();
                logoImage.setCreated(Calendar.getInstance().getTime());
            }

            try (
                    // Small
                    InputStream smallFile = ImageHelper.scaleImage(imageStream,
                            this.imageSmallWidth,
                            this.imageSmallHeight,
                            this.imageFormat);

                    // Medium
                    InputStream mediumFile = ImageHelper.scaleImage(imageStream,
                            this.imageMediumWidth,
                            this.imageMediumHeight,
                            this.imageFormat);

                    // Large
                    InputStream largeFile = ImageHelper.scaleImage(imageStream,
                            this.imageLargeWidth,
                            this.imageLargeHeight,
                            this.imageFormat)) {

                // Execs updates
                logoImage.setUpdated(Calendar.getInstance().getTime());
                logoImage.setUpdated(Calendar.getInstance().getTime());
                logoImage.setSmallFile(PortalHelper.getBlob(smallFile));
                logoImage.setMediumFile(PortalHelper.getBlob(mediumFile));
                logoImage.setLargeFile(PortalHelper.getBlob(largeFile));
                logoImage.setOriginalFile(PortalHelper.getBlob(imageStream));

                clubEntity.setClubLogo(logoImage);
                clubEntity.setUpdated(Calendar.getInstance().getTime());

                this.hibernateTemplate.update(clubEntity);
            }




        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (imageStream != null) {
                try {
                    imageStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Adds a new gallery image for that band id.
     * @param bandId
     * @param imageStream
     */
    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public Long addBandGalleryImage(Long bandId, InputStream imageStream) {
        Long newImageId = null;

        try {

            // Retrieves band entity
            BandEntity bandEntity = this.hibernateTemplate.get(BandEntity.class, bandId);

            // Creates a new ImageEntity
            ImageEntity image =new ImageEntity();
            image.setCreated(Calendar.getInstance().getTime());
            image.setLinkId(PortalHelper.getUniqueId());



            try (
                    // Small
                    InputStream smallFile = ImageHelper.scaleImage(imageStream,
                            this.imageSmallWidth,
                            this.imageSmallHeight,
                            this.imageFormat);

                    // Medium
                    InputStream mediumFile = ImageHelper.scaleImage(imageStream,
                            this.imageMediumWidth,
                            this.imageMediumHeight,
                            this.imageFormat);

                    // Large
                    InputStream largeFile = ImageHelper.scaleImage(imageStream,
                            this.imageLargeWidth,
                            this.imageLargeHeight,
                            this.imageFormat)) {

                // Execs updates
                image.setSmallFile(PortalHelper.getBlob(smallFile));
                image.setMediumFile(PortalHelper.getBlob(mediumFile));
                image.setLargeFile(PortalHelper.getBlob(largeFile));
                image.setOriginalFile(PortalHelper.getBlob(imageStream));

                newImageId = (Long) this.hibernateTemplate.save(image);

                if (bandEntity.getImages() == null) {
                    bandEntity.setImages(new HashSet<ImageEntity>());
                }

                bandEntity.getImages().add(image);

                bandEntity.setUpdated(Calendar.getInstance().getTime());

                this.hibernateTemplate.update(bandEntity);
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (imageStream != null) {
                try {
                    imageStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return newImageId;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public Long addClubGalleryImage(Long clubId, InputStream imageStream) {
        Long newImageId = null;

        try {

            // Retrieves band entity
            ClubEntity clubEntity = this.hibernateTemplate.get(ClubEntity.class, clubId);

            // Creates a new ImageEntity
            ImageEntity image = new ImageEntity();
            image.setCreated(Calendar.getInstance().getTime());
            image.setLinkId(PortalHelper.getUniqueId());



            try (
                    // Small
                    InputStream smallFile = ImageHelper.scaleImage(imageStream,
                            this.imageSmallWidth,
                            this.imageSmallHeight,
                            this.imageFormat);

                    // Medium
                    InputStream mediumFile = ImageHelper.scaleImage(imageStream,
                            this.imageMediumWidth,
                            this.imageMediumHeight,
                            this.imageFormat);

                    // Large
                    InputStream largeFile = ImageHelper.scaleImage(imageStream,
                            this.imageLargeWidth,
                            this.imageLargeHeight,
                            this.imageFormat)) {

                // Execs updates
                image.setSmallFile(PortalHelper.getBlob(smallFile));
                image.setMediumFile(PortalHelper.getBlob(mediumFile));
                image.setLargeFile(PortalHelper.getBlob(largeFile));
                image.setOriginalFile(PortalHelper.getBlob(imageStream));

                newImageId = (Long) this.hibernateTemplate.save(image);

                if (clubEntity.getImages() == null) {
                    clubEntity.setImages(new HashSet<ImageEntity>());
                }

                clubEntity.getImages().add(image);

                clubEntity.setUpdated(Calendar.getInstance().getTime());

                this.hibernateTemplate.update(clubEntity);
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (imageStream != null) {
                try {
                    imageStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return newImageId;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void removeBandGalleryImage(Long bandId, Long imageId) {
        this.hibernateTemplate.execute(session -> {

            BandEntity bandEntity = (BandEntity) session.get(BandEntity.class, bandId);

            List<ImageEntity> imagesToDelete = new ArrayList<ImageEntity>();

            if (bandEntity.getImages() != null) {
                bandEntity.getImages().stream().filter(i -> i.getId().equals(imageId)).forEach(i -> {
                    imagesToDelete.add(i);
                });
            }

            imagesToDelete.stream().forEach(i -> {
                bandEntity.getImages().remove(i);
            });

            session.update(bandEntity);

            return null;
        });
    }



    @Override
    public ImageEntity getImageById(Long id) {
        return this.hibernateTemplate.get(ImageEntity.class, id);
    }

    @Override
    public AudioEntity getAudioById(Long id) {
        return this.hibernateTemplate.get(AudioEntity.class, id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public Long addBandAudio(Long bandId, InputStream audioStream, String name) {
        Long newAudioId = null;

        try {

            String uniqueName = StringUtils.trimToEmpty(name) + "_" + PortalHelper.getUniqueId() + ".mp3";

            // Retrieves band entity
            BandEntity bandEntity = this.hibernateTemplate.get(BandEntity.class, bandId);

            // Creates a new AudioEntity
            AudioEntity audio = new AudioEntity();
            audio.setCreated(Calendar.getInstance().getTime());

            audio.setName(StringUtils.trimToEmpty(name));
            audio.setFileName(uniqueName);

            newAudioId = (Long) this.hibernateTemplate.save(audio);

            if (bandEntity.getAudios() == null) {
                bandEntity.setAudios(new HashSet<AudioEntity>());
            }

            bandEntity.getAudios().add(audio);

            bandEntity.setUpdated(Calendar.getInstance().getTime());

            this.hibernateTemplate.update(bandEntity);

            PortalHelper.saveFile(audioStream, this.audioBasePath, uniqueName);

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (audioStream != null) {
                try {
                    audioStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return newAudioId;
    }

    /*@Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void removeBandAudio(Long bandId, Long audioId) {
        this.hibernateTemplate.execute(session -> {

            BandEntity bandEntity = (BandEntity) session.get(BandEntity.class, bandId);

            List<AudioEntity> audiosToDelete = new ArrayList<AudioEntity>();

            if (bandEntity.getAudios() != null) {
                bandEntity.getAudios().stream().filter(a -> a.getId().equals(audioId)).forEach(i -> {
                    audiosToDelete.add(i);
                });
            }

            audiosToDelete.stream().forEach(i -> {
                bandEntity.getAudios().remove(i);
            });

            session.update(bandEntity);

            // Removes physical files
            String folder = this.audioBasePath;
            audiosToDelete.stream().forEach(a -> {
                File file = new File(this.audioBasePath + a.getFileName());

                if (file.exists()) {
                    file.delete();
                }
            });

            return null;
        });
    }*/
}

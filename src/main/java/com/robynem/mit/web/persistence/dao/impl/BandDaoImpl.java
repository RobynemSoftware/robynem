package com.robynem.mit.web.persistence.dao.impl;

import com.robynem.mit.web.persistence.criteria.BandComponentsCriteria;
import com.robynem.mit.web.persistence.dao.BandDao;
import com.robynem.mit.web.persistence.dao.BaseDao;
import com.robynem.mit.web.persistence.dao.NotificationDao;
import com.robynem.mit.web.persistence.dao.UtilsDao;
import com.robynem.mit.web.persistence.entity.*;
import com.robynem.mit.web.util.EntityStatus;
import com.robynem.mit.web.util.OwnerType;
import com.robynem.mit.web.util.PublishBandErrorCode;
import com.robynem.mit.web.util.PublishBandResult;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.RequestingUserName;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by robyn_000 on 13/01/2016.
 */
@Service
public class BandDaoImpl extends BaseDao implements BandDao {

    @Autowired
    private UtilsDao<VideoEntity> utilsVideoDao;

    @Autowired
    private NotificationDao notificationDao;

    /**
     * Createa a new band, with NOT_PUBLISHED statu and its stage version.
     *
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public BandEntity createEmtpyBand(final Long userId) {
        BandEntity bandEntity = null;

        if (userId == null) {
            throw new RuntimeException("User id cannot be null.");
        }

        final BandDao thisInstance = this;

        bandEntity = this.hibernateTemplate.execute(new HibernateCallback<BandEntity>() {
            @Override
            public BandEntity doInHibernate(Session session) throws HibernateException, SQLException {
                BandEntity parentBandEntity = null;

                // Gets the owner's owner type
                OwnerTypeEntity ownerOwnerTypeEntity = (OwnerTypeEntity) session.getNamedQuery("@HQL_GET_OWNER_TYPE_BY_CODE")
                        .setParameter("code", OwnerType.OWNER.toString())
                        .list()
                        .get(0);

                // Gets the  NOT PUBLISHED status
                EntityStatusEntity notPublishedStatus = (EntityStatusEntity) session.getNamedQuery("@HQL_GET_ENTITY_STATUS_BY_CODE")
                        .setParameter("code", EntityStatus.NOT_PUBLISHED.toString())
                        .list()
                        .get(0);

                // Gets the  STAGE status
                EntityStatusEntity stageStatus = (EntityStatusEntity) session.getNamedQuery("@HQL_GET_ENTITY_STATUS_BY_CODE")
                        .setParameter("code", EntityStatus.STAGE.toString())
                        .list()
                        .get(0);

                // Gets the owner user
                UserEntity ownerUser = (UserEntity) session.get(UserEntity.class, userId);

                if (ownerUser == null) {
                    throw new RuntimeException("Cannot retrieve ownere user");
                }

                // Creates the parent band (NOT PUBLISHED)
                parentBandEntity = new BandEntity();

                parentBandEntity.setCreatedBy(ownerUser);

                parentBandEntity.setCreated(Calendar.getInstance().getTime());
                parentBandEntity.setStatus(notPublishedStatus);

                // Saves the new parent band
                session.saveOrUpdate(parentBandEntity);

                // Save the band ownership
                BandOwnershipEntity parentBandOwnershipEntity = new BandOwnershipEntity(ownerUser, parentBandEntity, ownerOwnerTypeEntity);
                session.saveOrUpdate(parentBandOwnershipEntity);

                // Creates and save stage version
                BandEntity stageVersion = thisInstance.createStageVersion(parentBandEntity);
                session.saveOrUpdate(stageVersion);

                // Save the band ownership
                BandOwnershipEntity stageBandOwnershipEntity = new BandOwnershipEntity(ownerUser, stageVersion, ownerOwnerTypeEntity);
                session.saveOrUpdate(stageBandOwnershipEntity);

                // Update stege version reference on parent one
                List<BandEntity> stageVersions = new ArrayList<BandEntity>();
                stageVersions.add(stageVersion);

                parentBandEntity.setStageVersions(stageVersions);

                return parentBandEntity;
            }
        });

        return bandEntity;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public BandEntity createStageVersion(BandEntity parentBand) {
        BandEntity stage = new BandEntity();

        // Gets the  STAGE status
        List<EntityStatusEntity> entityStatusEntityList = this.hibernateTemplate.findByNamedQueryAndNamedParam("@HQL_GET_ENTITY_STATUS_BY_CODE", "code", EntityStatus.STAGE.toString());
        EntityStatusEntity stageStatus = (EntityStatusEntity) entityStatusEntityList.get(0);

        this.copyBandData(parentBand, stage);

        stage.setPublishedVersion(parentBand);
        stage.setStatus(stageStatus);

        return stage;
    }

    @Override
    public void copyBandData(BandEntity source, BandEntity destination) {
        destination.setStatus(source.getStatus());
        destination.setCreated(source.getCreated());
        destination.setCreatedBy(source.getCreatedBy());
        destination.setBandLogo(source.getBandLogo());
        destination.setBiography(source.getBiography());
        destination.setModifiedBy(source.getModifiedBy());
        destination.setName(source.getName());
        destination.setPlaceId(source.getPlaceId());
        destination.setTown(source.getTown());
        destination.setWebSite(source.getWebSite());
        destination.setUpdated(source.getUpdated());

        Set<BandOwnershipEntity> owners = new HashSet<BandOwnershipEntity>();

        // Create the owners set by setting null to band attribute so hibernate can associate destintion band id on save
        if (source.getOwners() != null) {
            for (BandOwnershipEntity bandOwnershipEntity : source.getOwners()) {
                owners.add(new BandOwnershipEntity(bandOwnershipEntity.getUser(), null, bandOwnershipEntity.getOwnerType()));
            }
        }

        destination.setMusicGenres(source.getMusicGenres());

        if (source.getComponents() != null) {
            destination.setComponents(new HashSet<BandComponentEntity>());

            source.getComponents().stream().forEach(c -> {
                BandComponentEntity bandComponentEntity = new BandComponentEntity();

                bandComponentEntity.setBand(destination);
                bandComponentEntity.setUser(c.getUser());
                bandComponentEntity.setSinger(c.isSinger());
                bandComponentEntity.setDiscJockey(c.isDiscJockey());

                destination.getComponents().add(bandComponentEntity);
            });
        }

        if (source.getContacts() != null) {
            destination.setContacts(new HashSet<BandContactEntity>());

            source.getContacts().stream().forEach(c -> {
                BandContactEntity bandContactEntity = new BandContactEntity(c.getEmailAddress(), c.getPhoneNumber());

                bandContactEntity.setBand(destination);

                destination.getContacts().add(bandContactEntity);
            });
        }

        if (source.getVideos() != null) {
            destination.setVideos(new HashSet<VideoEntity>());

            source.getVideos().stream().forEach(v -> {
                VideoEntity videoEntity = new VideoEntity();

                videoEntity.setYoutubeUrl(v.getYoutubeUrl());

                destination.getVideos().add(videoEntity);
            });
        }

        if (source.getImages() != null) {
            destination.setImages(new HashSet<ImageEntity>());

            source.getImages().stream().forEach(i -> {
                ImageEntity imageEntity = new ImageEntity();

                imageEntity.setFilePath(i.getFilePath());
                imageEntity.setLargeFile(i.getLargeFile());
                imageEntity.setMediumFile(i.getMediumFile());
                imageEntity.setOriginalFile(i.getOriginalFile());
                imageEntity.setSmallFile(i.getSmallFile());

                destination.getImages().add(imageEntity);
            });
        }

        if (source.getAudios() != null) {
            destination.setAudios(new HashSet<AudioEntity>());

            source.getAudios().stream().forEach(a -> {
                AudioEntity audioEntity = new AudioEntity();

                audioEntity.setName(a.getName());
                audioEntity.setFile(a.getFile());

                destination.getAudios().add(audioEntity);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void copyBandData(Serializable sourceId, final BandEntity destination) {

        this.hibernateTemplate.execute(session -> {

            BandEntity source = (BandEntity) session.get(BandEntity.class, sourceId);

            this.copyBandData(source, destination);

            return destination;
        });
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void update(BandEntity bandEntity) {

        this.hibernateTemplate.execute(session -> {

            /*Deletes all contacts to recreate list*/
            Query query = session.getNamedQuery("@HQL_DELETE_ALL_BAND_CONTACTS");
            query.setParameter("bandId", bandEntity.getId());

            query.executeUpdate();

            /*session.clear();

            *//*Stores new videos before associating them*//*
            if (bandEntity.getVideos() != null) {
                bandEntity.getVideos().stream().forEach(v -> {
                    session.saveOrUpdate(v);
                });
            }*/

            session.update(bandEntity);
            return null;
        });
    }

    @Override
    public BandEntity getBandById(Long bandId) {
        return this.hibernateTemplate.get(BandEntity.class, bandId);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public BandEntity addSelectedComponent(Long bandId, Long userId, Long operationUserId) {

        return this.hibernateTemplate.execute(session -> {
            // Retrieves selected component
            UserEntity userEntity = (UserEntity) session.get(UserEntity.class, userId);

            BandEntity bandEntity = (BandEntity) session.get(BandEntity.class, bandId);

            BandComponentEntity bandComponentEntity = new BandComponentEntity();
            bandComponentEntity.setUser(userEntity);
            bandComponentEntity.setBand(bandEntity);
            bandComponentEntity.setSinger(userEntity.isSinger());
            bandComponentEntity.setDiscJockey(userEntity.isDiscJockey());
            bandComponentEntity.setPlayedInstruments(new HashSet<MusicalInstrumentEntity>());

            if (userEntity.getPlayedMusicInstruments() != null) {
                userEntity.getPlayedMusicInstruments().stream().forEach(i -> {
                    bandComponentEntity.getPlayedInstruments().add((MusicalInstrumentEntity) session.get(MusicalInstrumentEntity.class, i.getId()));
                });
            }

            if (bandEntity.getComponents() == null) {
                bandEntity.setComponents(new HashSet<BandComponentEntity>());
            }

            bandEntity.getComponents().add(bandComponentEntity);

            session.update(bandEntity);

            // Sends notification
            // Attaches alway published band version.
            Long bandNotificationId = bandEntity.getPublishedVersion() != null ? bandEntity.getPublishedVersion().getId() : bandEntity.getId();
            this.notificationDao.sendBandInvitation(operationUserId, userId, bandNotificationId);

            return bandEntity;
        });
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public BandEntity removeComponent(Long bandId, Long userId, Long operationUserId) {
        return this.hibernateTemplate.execute(session -> {

            BandEntity bandEntity = (BandEntity) session.get(BandEntity.class, bandId);

            // Retrieves selected component
            BandComponentEntity bandComponentEntity = bandEntity.getComponents().stream().filter(bc -> bc.getUser().getId().equals(userId)).findFirst().get();

            // Removes component
            //session.delete(bandComponentEntity);
            bandComponentEntity.setBand(null);
            bandComponentEntity.setUser(null);
            bandEntity.getComponents().remove(bandComponentEntity);

            //session.update(bandComponentEntity);
            session.update(bandEntity);

            session.flush();

            // Sends notification
            //this.notificationDao.sendBandComponentRemoval(operationUserId, userId, bandEntity.getId());

            return bandEntity;
        });
    }

    @Override
    public BandEntity getBandToEdit(Long bandId) {
        return this.hibernateTemplate.execute(session -> {
            BandEntity bandEntity = null;

            Criteria criteria = session.createCriteria(BandEntity.class, "band");

            criteria = criteria.add(Restrictions.idEq(bandId));

            criteria = criteria.createAlias("band.owners", "owners", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.bandLogo", "bandLogo", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.images", "images", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.videos", "videos", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.status", "status", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.publishedVersion", "publishedVersion", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.stageVersions", "stageVersions", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.musicGenres", "musicGenres", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.components", "components", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("components.user", "bc", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("bc.profileImage", "bcpi", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("bc.playedMusicInstruments", "bcpinstr", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("components.playedInstruments", "bcpinstr1", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.contacts", "contacts", CriteriaSpecification.LEFT_JOIN);

            bandEntity = (BandEntity) criteria.uniqueResult();

            return bandEntity;
        });
    }

    @Override
    public BandEntity getBandGeneralInfo(Long bandId) {
        return this.hibernateTemplate.execute(session -> {
            BandEntity bandEntity = null;

            Criteria criteria = session.createCriteria(BandEntity.class, "band");

            criteria = criteria.add(Restrictions.idEq(bandId));

            criteria = criteria.createAlias("band.owners", "owners", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.bandLogo", "bandLogo", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.status", "status", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.publishedVersion", "publishedVersion", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.stageVersions", "stageVersions", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.musicGenres", "musicGenres", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.contacts", "contacts", CriteriaSpecification.LEFT_JOIN);

            bandEntity = (BandEntity) criteria.uniqueResult();

            return bandEntity;
        });
    }

    @Override
    public BandEntity getBandComponents(Long bandId) {
        return this.hibernateTemplate.execute(session -> {
            BandEntity bandEntity = null;

            Criteria criteria = session.createCriteria(BandEntity.class, "band");

            criteria = criteria.add(Restrictions.idEq(bandId));

            criteria = criteria.createAlias("band.owners", "owners", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.status", "status", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.publishedVersion", "publishedVersion", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.stageVersions", "stageVersions", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.components", "components", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("components.user", "bc", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("bc.profileImage", "bcpi", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("bc.playedMusicInstruments", "bcpinstr", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("components.playedInstruments", "bcpinstr1", CriteriaSpecification.LEFT_JOIN);

            bandEntity = (BandEntity) criteria.uniqueResult();

            return bandEntity;
        });
    }

    @Override
    public BandEntity getBandMedia(Long bandId) {
        return this.hibernateTemplate.execute(session -> {
            BandEntity bandEntity = null;

            Criteria criteria = session.createCriteria(BandEntity.class, "band");

            criteria = criteria.add(Restrictions.idEq(bandId));

            criteria = criteria.createAlias("band.owners", "owners", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.images", "images", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.videos", "videos", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.audios", "audios", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.status", "status", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.publishedVersion", "publishedVersion", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.stageVersions", "stageVersions", CriteriaSpecification.LEFT_JOIN);

            bandEntity = (BandEntity) criteria.uniqueResult();

            return bandEntity;
        });
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveComponentInstrument(Long bandId, Long userId, Long instrumentId, boolean selected) {

        this.hibernateTemplate.execute(session -> {

            if (!selected) {
                this.removeComponentInstrument(bandId, userId, instrumentId, session);
            } else {
                this.addComponentInstrument(bandId, userId, instrumentId, session);
            }

            return null;
        });
    }

    @Override
    public List<UserEntity> searchBandComponents(BandComponentsCriteria criteria) {
        return this.hibernateTemplate.execute(session -> {
            List<UserEntity> result = new ArrayList<UserEntity>();

            if (criteria != null) {
                Criteria hCriteria = session.createCriteria(UserEntity.class, "user");

                hCriteria = hCriteria.createAlias("user.profileImage", "profileImage", CriteriaSpecification.LEFT_JOIN);

                if (criteria.getInstrumentId() != null) {
                    hCriteria = hCriteria.createAlias("user.playedMusicInstruments", "instrument", CriteriaSpecification.INNER_JOIN);
                    hCriteria = hCriteria.add(Restrictions.eq("instrument.id", criteria.getInstrumentId()));
                } else {
                    hCriteria = hCriteria.createAlias("user.playedMusicInstruments", "instrument", CriteriaSpecification.LEFT_JOIN);
                }

                if (criteria.getGenreId() != null) {
                    hCriteria = hCriteria.createAlias("user.preferredMusicGenres", "genre", CriteriaSpecification.INNER_JOIN);
                    hCriteria = hCriteria.add(Restrictions.eq("genre.id", criteria.getGenreId()));
                } else {
                    hCriteria = hCriteria.createAlias("user.preferredMusicGenres", "genre", CriteriaSpecification.LEFT_JOIN);
                }

                if (StringUtils.isNotBlank(criteria.getKeyword())) {
                    List<String> keywords = Arrays.asList(StringUtils.trimToEmpty(criteria.getKeyword()).split(" "));

                    Disjunction firstNameDisjunction = Restrictions.disjunction();
                    Disjunction lastNameDisjunction = Restrictions.disjunction();
                    Disjunction biographyDisjunction = Restrictions.disjunction();
                    Disjunction emailDisjunction = Restrictions.disjunction();

                    keywords.stream().forEach(c -> {
                        firstNameDisjunction.add(Restrictions.like("user.firstName", "%" + c + "%"));
                        lastNameDisjunction.add(Restrictions.like("user.lastName", "%" + c + "%"));
                        biographyDisjunction.add(Restrictions.like("user.biography", "%" + c + "%"));
                        emailDisjunction.add(Restrictions.like("user.emailAddress", "%" + c + "%"));
                    });

                    hCriteria.add(Restrictions.disjunction().add(firstNameDisjunction).add(lastNameDisjunction).add(biographyDisjunction).add(emailDisjunction));
                }

                if (StringUtils.isNotBlank(criteria.getPlaceId())) {
                    hCriteria.add(Restrictions.eq("user.placeId", criteria.getPlaceId()));
                }

                if (criteria.isSinger()) {
                    hCriteria = hCriteria.add(Restrictions.eq("user.singer", true));
                }

                if (criteria.isDj()) {
                    hCriteria = hCriteria.add(Restrictions.eq("user.discJockey", true));
                }


                hCriteria = hCriteria.add(Restrictions.eq("user.musician", true));
                hCriteria = hCriteria.add(Restrictions.eq("user.engagementAvailable", true));

                BandEntity bandEntity = (BandEntity) session.get(BandEntity.class, criteria.getBandId());

                List<Long> userIdsToExclude = new ArrayList<Long>();

                bandEntity.getComponents().stream().forEach(c -> {
                    userIdsToExclude.add(c.getUser().getId());
                });

                if (userIdsToExclude.size() > 0) {
                    hCriteria = hCriteria.add(Restrictions.not(Restrictions.in("user.id", userIdsToExclude)));
                }


                //hCriteria = hCriteria.setFetchMode("user.playedMusicInstruments",FetchMode.JOIN);

                /*hCriteria = hCriteria.setProjection(Projections.projectionList()
                        .add(Projections.property("user.id"), "id")
                        .add(Projections.property("user.firstName"), "firstName")
                        .add(Projections.property("user.lastName"), "lastName")
                        .add(Projections.property("user.biography"), "biography")
                        .add(Projections.property("user.town"), "town")
                        .add(Projections.property("user.playedMusicInstruments")));*/

                hCriteria = hCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

                result = hCriteria.list();
            }

            return result;
        });
    }

    @Override
    public List<BandEntity> getOwnedBands(Long userId, OwnerType ownerType) {
        return this.hibernateTemplate.execute(session -> {
            List<BandEntity> result = new ArrayList<BandEntity>();

            Criteria criteria = session.createCriteria(BandEntity.class, "band");



            criteria = criteria.createAlias("band.owners", "owners", CriteriaSpecification.INNER_JOIN);
            criteria = criteria.createAlias("owners.user", "userOwner", CriteriaSpecification.INNER_JOIN);
            criteria = criteria.createAlias("owners.ownerType", "ownerType", CriteriaSpecification.INNER_JOIN);

            criteria = criteria.createAlias("band.bandLogo", "bandLogo", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.status", "status", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.publishedVersion", "publishedVersion", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("band.stageVersions", "stageVersions", CriteriaSpecification.LEFT_JOIN);


            criteria = criteria.add(Restrictions.eq("userOwner.id", userId));
            criteria = criteria.add(Restrictions.eq("ownerType.code", ownerType.toString()));
            // Retrieves or published version if not any stage one or the stage one.
            criteria = criteria.add(Restrictions.disjunction().add(Restrictions.isNotNull("publishedVersion")).add(Restrictions.isEmpty("stageVersions")));

            criteria = criteria.add(Restrictions.isNotNull("band.name"));
            criteria = criteria.add(Restrictions.not(Restrictions.eq("band.name", "")));

            criteria = criteria.addOrder(Order.asc("band.name"));

            criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            result = criteria.list();

            return result;
        });
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveBandComponentSinger(Long bandId, Long bandComponentId, boolean value) {
        BandComponentEntity bandComponentEntity = this.hibernateTemplate.get(BandComponentEntity.class, bandComponentId);

        if (!bandComponentEntity.getBand().getId().equals(bandId)) {
            throw new RuntimeException(String.format("Band component id [%d] doesn't belong to Band Id [%d]", bandComponentId, bandId));
        }

        bandComponentEntity.setSinger(value);

        this.hibernateTemplate.update(bandComponentEntity);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveBandComponentDiscJockey(Long bandId, Long bandComponentId, boolean value) {
        BandComponentEntity bandComponentEntity = this.hibernateTemplate.get(BandComponentEntity.class, bandComponentId);

        if (!bandComponentEntity.getBand().getId().equals(bandId)) {
            throw new RuntimeException(String.format("Band component id [%d] doesn't belong to Band Id [%d]", bandComponentId, bandId));
        }

        bandComponentEntity.setDiscJockey(value);

        this.hibernateTemplate.update(bandComponentEntity);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveBandVideos(Long bandId, Set<VideoEntity> videos) {
        this.hibernateTemplate.execute(session -> {

            BandEntity bandEntity = (BandEntity) session.get(BandEntity.class, bandId);

            /*Retrieves old videos */
            List<Long> videosToDelete = new ArrayList<Long>();

            if (bandEntity.getVideos() != null) {
                bandEntity.getVideos().stream().filter(v -> v.getId() != null).forEach(v -> {
                    videosToDelete.add(v.getId());
                });
            }

            bandEntity.setVideos(videos);

            session.update(bandEntity);

            session.flush();

            if (videosToDelete.size() > 0) {
                this.utilsVideoDao.deleteOrphanVideos(videosToDelete);
            }

            return null;
        });
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Long addBandVideo(Long bandId, VideoEntity videoEntity) {
        return this.hibernateTemplate.execute(session -> {

            BandEntity bandEntity = (BandEntity) session.get(BandEntity.class, bandId);

            if (bandEntity.getVideos() == null) {
                bandEntity.setVideos(new HashSet<VideoEntity>());
            }

            bandEntity.getVideos().add(videoEntity);

            session.update(bandEntity);

            session.flush();

            return videoEntity.getId();
        });
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void removeBandVideo(Long bandId, Long videoId) {
        this.hibernateTemplate.execute(session -> {

            BandEntity bandEntity = (BandEntity) session.get(BandEntity.class, bandId);

            VideoEntity videoEntity = (VideoEntity) session.get(VideoEntity.class, videoId);

            if (bandEntity.getVideos() != null && videoEntity != null) {
                bandEntity.getVideos().remove(videoEntity);
            }

            session.update(bandEntity);

            return null;
        });
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public PublishBandResult publishBand(Long bandId, Long userId) {
        return this.hibernateTemplate.execute(session -> {
            PublishBandResult publishBandResult = new PublishBandResult();

            BandEntity bandEntity = (BandEntity) session.get(BandEntity.class, bandId);

            BandEntity publishedVersion = null;

            if (bandEntity.getPublishedVersion() == null) {
                publishBandResult = new PublishBandResult(false, null, PublishBandErrorCode.NOT_A_STAGE_VERSION);
            } else {

                publishedVersion = bandEntity.getPublishedVersion();

                // Does validations
                if (StringUtils.isBlank(bandEntity.getName())) {
                    publishBandResult = new PublishBandResult(false, null, PublishBandErrorCode.NAME_MISSING);
                } else if (StringUtils.isBlank(bandEntity.getPlaceId())) {
                    publishBandResult = new PublishBandResult(false, null, PublishBandErrorCode.TOWN_MISSING);
                } else if (bandEntity.getComponents() == null || bandEntity.getComponents().size() == 0) {
                    publishBandResult = new PublishBandResult(false, null, PublishBandErrorCode.COMPONENTS_MISSING);
                } else if (!bandEntity.getComponents().stream().filter(c -> c.isConfirmed() == true).findAny().isPresent()) {
                    publishBandResult = new PublishBandResult(false, null, PublishBandErrorCode.NO_ONE_COMPONENT_CONFIRMED);
                } else if (bandEntity.getMusicGenres() == null || bandEntity.getMusicGenres().size() == 0) {
                    publishBandResult = new PublishBandResult(false, null, PublishBandErrorCode.GENRE_MISSING);
                } else {
                    Query statusQuery = session.getNamedQuery("@HQL_GET_ENTITY_STATUS_BY_CODE");
                    statusQuery.setParameter("code", EntityStatus.PUBLISHED.toString());
                    EntityStatusEntity publishedStatus = (EntityStatusEntity) statusQuery.uniqueResult();

                    this.copyBandData(bandEntity, publishedVersion);

                    publishedVersion.setUpdated(Calendar.getInstance().getTime());
                    if (publishedVersion.getFirstPublishDate() == null) {
                        publishedVersion.setFirstPublishDate(Calendar.getInstance().getTime());
                    }

                    publishedVersion.setStatus(publishedStatus);

                    publishedVersion.setModifiedBy((UserEntity) session.get(UserEntity.class, userId));

                    session.update(publishedVersion);

                    session.flush();

                    session.delete(bandEntity);

                    session.flush();

                    publishBandResult = new PublishBandResult(true, publishedVersion.getId(), null);
                }
            }

            return publishBandResult;
        });
    }

    private void addComponentInstrument(Long bandId, Long userId, Long instrumentId, Session session) {
        Criteria criteria = session.createCriteria(BandComponentEntity.class, "component");

        criteria = criteria.createAlias("component.user", "user", CriteriaSpecification.INNER_JOIN);
        criteria = criteria.createAlias("component.band", "band", CriteriaSpecification.INNER_JOIN);

        criteria = criteria.add(Restrictions.and(Restrictions.eq("user.id", userId), Restrictions.eq("band.id", bandId)));

        // Retrieves band component entity
        BandComponentEntity bandComponentEntity = (BandComponentEntity) criteria.uniqueResult();

        if (bandComponentEntity != null) {
            if (bandComponentEntity.getPlayedInstruments() == null) {
                bandComponentEntity.setPlayedInstruments(new HashSet<MusicalInstrumentEntity>());
            }

            // Retrives requeired musical instrument.
            MusicalInstrumentEntity musicalInstrumentEntity = (MusicalInstrumentEntity) session.get(MusicalInstrumentEntity.class, instrumentId);

            if (musicalInstrumentEntity != null) {
                // If it's not already associated to this compinent, it adds.
                if (!bandComponentEntity.getPlayedInstruments().stream().filter(i -> i.getId().equals(instrumentId)).findFirst().isPresent()) {
                    bandComponentEntity.getPlayedInstruments().add(musicalInstrumentEntity);

                    // Updates entity
                    session.update(bandComponentEntity);
                }
            }
        }
    }

    private void removeComponentInstrument(Long bandId, Long userId, Long instrumentId, Session session) {
        Criteria criteria = session.createCriteria(BandComponentEntity.class, "component");

        criteria = criteria.createAlias("component.user", "user", CriteriaSpecification.INNER_JOIN);
        criteria = criteria.createAlias("component.band", "band", CriteriaSpecification.INNER_JOIN);

        criteria = criteria.add(Restrictions.and(Restrictions.eq("user.id", userId), Restrictions.eq("band.id", bandId)));

        BandComponentEntity bandComponentEntity = (BandComponentEntity) criteria.uniqueResult();

        if (bandComponentEntity != null) {
            if (bandComponentEntity.getPlayedInstruments() != null) {

                MusicalInstrumentEntity toRemove = bandComponentEntity.getPlayedInstruments().stream()
                        .filter(i -> i.getId().equals(instrumentId)).findFirst().orElse(null);

                if (toRemove != null) {
                    // Revoves it form collection
                    bandComponentEntity.getPlayedInstruments().remove(toRemove);

                    // Updates entity
                    session.update(bandComponentEntity);
                }
            }
        }
    }
}

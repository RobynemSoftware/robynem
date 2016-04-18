package com.robynem.mit.web.persistence.dao.impl;

import com.robynem.mit.web.persistence.dao.BaseDao;
import com.robynem.mit.web.persistence.dao.ClubDao;
import com.robynem.mit.web.persistence.entity.*;
import com.robynem.mit.web.util.EntityStatus;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by robyn_000 on 18/04/2016.
 */
@Service
public class ClubDaoImpl extends BaseDao implements ClubDao {

    @Override
    public ClubEntity getClubById(Long clubId) {
        return this.hibernateTemplate.get(ClubEntity.class, clubId);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public ClubEntity createStageVersion(Long clubId) {
        return this.hibernateTemplate.execute(session -> {
            ClubEntity parentClubEntity = (ClubEntity) session.get(ClubEntity.class, clubId);

            return this.createStageVersion(parentClubEntity);
        });
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public ClubEntity createStageVersion(ClubEntity parentClub) {
        return this.hibernateTemplate.execute(session -> {
            ClubEntity stage = new ClubEntity();
            // Gets the  STAGE status
            Query statusQuery = session.getNamedQuery("@HQL_GET_ENTITY_STATUS_BY_CODE");
            statusQuery.setParameter("code", EntityStatus.STAGE.toString());

            EntityStatusEntity stageStatus = (EntityStatusEntity) statusQuery.uniqueResult();

            this.copyClubData(parentClub, stage, session);

            stage.setPublishedVersion(parentClub);
            stage.setStatus(stageStatus);

            session.saveOrUpdate(stage);

            return stage;
        });
    }

    @Override
    public ClubEntity createEmptyClub(Long clubId) {
        return null;
    }

    @Override
    public ClubEntity getClubGeneralInfo(Long clubId) {
        return null;
    }

    @Override
    public ClubEntity getClubMedia(Long clubId) {
        return null;
    }

    @Override
    public void update(ClubEntity clubEntity) {

    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    private void copyClubData(ClubEntity source, ClubEntity destination, Session session) {
        destination.setStatus(source.getStatus());
        destination.setCreated(source.getCreated());
        destination.setCreatedBy(source.getCreatedBy());

        destination.setDescription(source.getDescription());
        destination.setModifiedBy(source.getModifiedBy());
        destination.setName(source.getName());
        destination.setPlaceId(source.getPlaceId());
        destination.setTown(source.getTown());
        destination.setWebSite(source.getWebSite());
        destination.setUpdated(source.getUpdated());



        destination.setClubLogo(null);
        if (source.getClubLogo() != null) {
            destination.setClubLogo(new ImageEntity());
            destination.getClubLogo().setSmallFile(source.getClubLogo().getSmallFile());
            destination.getClubLogo().setMediumFile(source.getClubLogo().getMediumFile());
            destination.getClubLogo().setLargeFile(source.getClubLogo().getLargeFile());
            destination.getClubLogo().setOriginalFile(source.getClubLogo().getOriginalFile());
            destination.getClubLogo().setFilePath(source.getClubLogo().getFilePath());
            destination.getClubLogo().setCreated(source.getClubLogo().getCreated());
            destination.getClubLogo().setUpdated(source.getClubLogo().getUpdated());
        }


        if (destination.getOwners() == null) {
            destination.setOwners(new ArrayList<ClubOwnershipEntity>());
        }
        destination.getOwners().clear();



        // Create the owners set by setting null to band attribute so hibernate can associate destintion band id on save
        if (source.getOwners() != null) {
            for (ClubOwnershipEntity clubOwnershipEntity : source.getOwners()) {
                destination.getOwners().add(new ClubOwnershipEntity(clubOwnershipEntity.getUser(), destination, clubOwnershipEntity.getOwnerType()));
            }
        }



        if (destination.getClubGenres() == null) {
            destination.setClubGenres(new ArrayList<>());
        }
        destination.getClubGenres().clear();



        if (source.getClubGenres() != null) {
            source.getClubGenres().stream().forEach(cg -> {
                destination.getClubGenres().add(cg);
            });
        }

        if (destination.getContacts() == null) {
            destination.setContacts(new ArrayList<>());
        }
        destination.getContacts().clear();



        if (source.getContacts() != null) {

            source.getContacts().stream().forEach(c -> {
                BandContactEntity bandContactEntity = new BandContactEntity(c.getEmailAddress(), c.getPhoneNumber());

                bandContactEntity.setBand(destination);

                destination.getContacts().add(bandContactEntity);
            });
        }



        if (destination.getVideos() == null) {
            destination.setVideos(new HashSet<VideoEntity>());
        }
        destination.getVideos().clear();



        if (source.getVideos() != null) {

            source.getVideos().stream().forEach(v -> {
                VideoEntity videoEntity = new VideoEntity();

                videoEntity.setYoutubeUrl(v.getYoutubeUrl());

                videoEntity.setLinkId(v.getLinkId());

                videoEntity.setCreated(v.getCreated());
                videoEntity.setUpdated(v.getUpdated());

                destination.getVideos().add(videoEntity);


            });
        }



        if (destination.getImages() == null) {
            destination.setImages(new HashSet<ImageEntity>());
        }
        destination.getImages().clear();



        if (source.getImages() != null) {

            source.getImages().stream().forEach(i -> {
                ImageEntity imageEntity = new ImageEntity();

                imageEntity.setFilePath(i.getFilePath());
                imageEntity.setLargeFile(i.getLargeFile());
                imageEntity.setMediumFile(i.getMediumFile());
                imageEntity.setOriginalFile(i.getOriginalFile());
                imageEntity.setSmallFile(i.getSmallFile());
                imageEntity.setLinkId(i.getLinkId());

                imageEntity.setCreated(i.getCreated());
                imageEntity.setUpdated(i.getUpdated());

                destination.getImages().add(imageEntity);
            });
        }



        if (destination.getAudios() == null) {
            destination.setAudios(new HashSet<AudioEntity>());
        }
        destination.getAudios().clear();



        if (source.getAudios() != null) {

            source.getAudios().stream().forEach(a -> {
                AudioEntity audioEntity = new AudioEntity();

                audioEntity.setName(a.getName());
                audioEntity.setFile(a.getFile());
                audioEntity.setSoundCloudUrl(a.getSoundCloudUrl());
                audioEntity.setLinkId(a.getLinkId());

                audioEntity.setCreated(a.getCreated());
                audioEntity.setUpdated(a.getUpdated());

                destination.getAudios().add(audioEntity);
            });
        }


    }
}

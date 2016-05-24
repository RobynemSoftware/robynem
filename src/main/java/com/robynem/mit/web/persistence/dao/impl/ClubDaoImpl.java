package com.robynem.mit.web.persistence.dao.impl;

import com.robynem.mit.web.persistence.dao.BaseDao;
import com.robynem.mit.web.persistence.dao.ClubDao;
import com.robynem.mit.web.persistence.entity.*;
import com.robynem.mit.web.util.EntityStatus;
import com.robynem.mit.web.util.OwnerType;
import com.robynem.mit.web.util.PublishClubErrorCode;
import com.robynem.mit.web.util.PublishClubResult;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public ClubEntity createEmptyClub(Long userId) {
        ClubEntity clubEntity = null;

        if (userId == null) {
            throw new RuntimeException("User id cannot be null.");
        }

        final ClubDao thisInstance = this;

        clubEntity = this.hibernateTemplate.execute(new HibernateCallback<ClubEntity>() {
            @Override
            public ClubEntity doInHibernate(Session session) throws HibernateException, SQLException {
                ClubEntity parentClubEntity = null;

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

                // Creates the parent club (NOT PUBLISHED)
                parentClubEntity = new ClubEntity();

                parentClubEntity.setCreatedBy(ownerUser);

                parentClubEntity.setCreated(Calendar.getInstance().getTime());
                parentClubEntity.setStatus(notPublishedStatus);

                // Saves the new parent band
                session.saveOrUpdate(parentClubEntity);

                // Save the club ownership
                ClubOwnershipEntity parentClubOwnershipEntity = new ClubOwnershipEntity(ownerUser, parentClubEntity, ownerOwnerTypeEntity);
                session.saveOrUpdate(parentClubOwnershipEntity);

                // Creates and save stage version
                ClubEntity stageVersion = thisInstance.createStageVersion(parentClubEntity);
                //session.saveOrUpdate(stageVersion); we save it into createStageVersioneMethod

                // Save the club ownership
                ClubOwnershipEntity stageClubOwnershipEntity = new ClubOwnershipEntity(ownerUser, stageVersion, ownerOwnerTypeEntity);
                session.saveOrUpdate(stageClubOwnershipEntity);

                // Update stage version reference on parent one
                List<ClubEntity> stageVersions = new ArrayList<ClubEntity>();
                stageVersions.add(stageVersion);

                parentClubEntity.setStageVersions(stageVersions);

                return parentClubEntity;
            }
        });

        return clubEntity;
    }

    @Override
    public ClubEntity getClubGeneralInfo(Long clubId) {
        return this.hibernateTemplate.execute(session -> {
            ClubEntity clubEntity = null;

            Criteria criteria = session.createCriteria(ClubEntity.class, "club");

            criteria = criteria.add(Restrictions.idEq(clubId));

            criteria = criteria.createAlias("club.owners", "owners", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("club.clubLogo", "clubLogo", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("club.status", "status", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("club.publishedVersion", "publishedVersion", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("club.stageVersions", "stageVersions", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("club.clubGenres", "clubGenres", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("club.contacts", "contacts", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("club.openingInfos", "openingInfos", CriteriaSpecification.LEFT_JOIN);

            clubEntity = (ClubEntity) criteria.uniqueResult();

            return clubEntity;
        });
    }

    @Override
    public ClubEntity getClubMedia(Long clubId) {
        return this.hibernateTemplate.execute(session -> {
            ClubEntity clubEntity = null;

            Criteria criteria = session.createCriteria(ClubEntity.class, "club");

            criteria = criteria.add(Restrictions.idEq(clubId));

            criteria = criteria.createAlias("club.owners", "owners", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("club.images", "images", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("club.status", "status", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("club.publishedVersion", "publishedVersion", CriteriaSpecification.LEFT_JOIN);
            criteria = criteria.createAlias("club.stageVersions", "stageVersions", CriteriaSpecification.LEFT_JOIN);

            clubEntity = (ClubEntity) criteria.uniqueResult();

            return clubEntity;
        });
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void update(ClubEntity clubEntity) {
        this.hibernateTemplate.execute(session -> {

            /*Deletes all contacts to recreate list*/
            Query query = session.getNamedQuery("@HQL_DELETE_ALL_CLUB_CONTACTS");
            query.setParameter("clubId", clubEntity.getId());

            query.executeUpdate();

            /*Deletes all opening info to recreate list*/
            query = session.getNamedQuery("@HQL_DELETE_ALL_CLUB_OPENING_INFO");
            query.setParameter("clubId", clubEntity.getId());

            query.executeUpdate();

            session.update(clubEntity);
            return null;
        });
    }

    @Override
    public String getClubStatusCode(Long clubId) {
        String code = null;

        List<String> result = this.hibernateTemplate.findByNamedQueryAndNamedParam(
                "@HQL_GET_CLUB_STATUS_CODE", "clubId", clubId);

        if (result != null && result.size() > 0) {
            code = result.get(0);
        }

        return code;
    }

    @Override
    public Long getStageGalleryImageId(Long publishedClubId, Long publishedImageId) {
        Long imageId = null;

        List<Long> result = this.hibernateTemplate.findByNamedQueryAndNamedParam(
                "@HQL_GET_STAGE_CLUB_IMAGE_ID", new String[] {"clubId", "imageId"},
                new Object[] {publishedClubId, publishedImageId});

        if (result != null && result.size() > 0) {
            imageId = result.get(0);
        }

        return imageId;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public PublishClubResult publishClub(Long clubId, Long userId) {
        return this.hibernateTemplate.execute(session -> {
            PublishClubResult publishClubResult = new PublishClubResult();

            ClubEntity clubEntity = (ClubEntity) session.get(ClubEntity.class, clubId);

            ClubEntity publishedVersion = null;

            if (clubEntity.getPublishedVersion() == null) {
                publishClubResult = new PublishClubResult(false, null, PublishClubErrorCode.NOT_A_STAGE_VERSION);
            } else {

                publishedVersion = clubEntity.getPublishedVersion();

                PublishClubErrorCode openingInfoValidationCode = this.validateOpeningInfo(clubEntity);

                // Does validations
                if (StringUtils.isBlank(clubEntity.getName())) {
                    publishClubResult = new PublishClubResult(false, null, PublishClubErrorCode.NAME_MISSING);
                } else if (StringUtils.isBlank(clubEntity.getAddressPlaceId())) {
                    publishClubResult = new PublishClubResult(false, null, PublishClubErrorCode.ADDRESS_MISSING);
                } else if (clubEntity.getOpeningInfos() == null || clubEntity.getOpeningInfos().size() == 0) {
                    publishClubResult = new PublishClubResult(false, null, PublishClubErrorCode.OPENING_INFO_MISSING);
                }  else if (clubEntity.getClubGenres() == null || clubEntity.getClubGenres().size() == 0) {
                    publishClubResult = new PublishClubResult(false, null, PublishClubErrorCode.GENRE_MISSING);
                } else if (openingInfoValidationCode != null) {
                    publishClubResult = new PublishClubResult(false, null, openingInfoValidationCode);
                } else {


                    Query callableQuery = session.createSQLQuery("call mit_spPublishClub(:stageClubId, :publishedClubId, :publishUserId)");
                    callableQuery.setParameter("stageClubId", clubEntity.getId());
                    callableQuery.setParameter("publishedClubId", publishedVersion.getId());
                    callableQuery.setParameter("publishUserId", userId);

                    callableQuery.executeUpdate();

                    publishClubResult = new PublishClubResult(true, publishedVersion.getId(), null);
                }
            }

            return publishClubResult;
        });
    }

    /**
     * Checks if opening info are correct configured.
     * @param clubEntity
     * @return
     */
    private PublishClubErrorCode validateOpeningInfo(ClubEntity clubEntity) {

        if (clubEntity.getOpeningInfos() != null) {
            List<ClubOpeningInfo> list = clubEntity.getOpeningInfos().stream().sorted((oi1, oi2) -> oi1.getStartDay().compareTo(oi2.getStartDay())).collect(Collectors.toList());

            // Date range
            for (ClubOpeningInfo oi : list) {
                if (list.stream().filter(oi1 -> oi1.getStartDay() >= oi.getStartDay() && oi1.getStartDay() <= oi.getEndDay() && !oi1.equals(oi)).findFirst().isPresent()) {
                    return PublishClubErrorCode.OPENING_INFO_INCORRECT_DATE_RANGE;
                }
            }

            // Date range - same row (day from gt day to)
            if (list.stream().filter(oi -> oi.getStartDay() > oi.getEndDay()).findFirst().isPresent()) {
                return PublishClubErrorCode.OPENING_INFO_INCORRECT_DATE_RANGE_SAME_ROW;
            }

            // At least one opening day
            if (!list.stream().filter(oi -> oi.isOpened()).findFirst().isPresent()) {
                return PublishClubErrorCode.OPENING_INFO_NO_OPENING;
            }
        }

        return null;
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
        destination.setAddress(source.getAddress());




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
            destination.setOwners(new HashSet<>());
        }
        destination.getOwners().clear();

        // Create the owners set by setting null to band attribute so hibernate can associate destintion band id on save
        if (source.getOwners() != null) {
            for (ClubOwnershipEntity clubOwnershipEntity : source.getOwners()) {
                destination.getOwners().add(new ClubOwnershipEntity(clubOwnershipEntity.getUser(), destination, clubOwnershipEntity.getOwnerType()));
            }
        }



        if (destination.getClubGenres() == null) {
            destination.setClubGenres(new HashSet<>());
        }
        destination.getClubGenres().clear();



        if (source.getClubGenres() != null) {
            source.getClubGenres().stream().forEach(cg -> {
                destination.getClubGenres().add(cg);
            });
        }


        if (destination.getContacts() == null) {
            destination.setContacts(new HashSet<>());
        }
        destination.getContacts().clear();

        if (source.getContacts() != null) {

            source.getContacts().stream().forEach(c -> {
                ClubContactEntity clubContactEntity = new ClubContactEntity(c.getEmailAddress(), c.getPhoneNumber());

                clubContactEntity.setClub(destination);

                destination.getContacts().add(clubContactEntity);
            });
        }

        if (destination.getImages() == null) {
            destination.setImages(new HashSet<>());
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

        if (destination.getOpeningInfos() == null) {
            destination.setOpeningInfos(new HashSet<>());
        }
        destination.getOpeningInfos().clear();



        if (source.getOpeningInfos() != null) {

            source.getOpeningInfos().stream().forEach(i -> {
                ClubOpeningInfo clubOpeningInfo = new ClubOpeningInfo();

                clubOpeningInfo.setEndDay(i.getEndDay());
                clubOpeningInfo.setEndHour(i.getEndHour());
                clubOpeningInfo.setOpened(i.isOpened());
                clubOpeningInfo.setStartDay(i.getStartDay());
                clubOpeningInfo.setStartHour(i.getStartHour());

                clubOpeningInfo.setCreated(i.getCreated());
                clubOpeningInfo.setUpdated(i.getUpdated());

                destination.getOpeningInfos().add(clubOpeningInfo);
            });
        }

    }
}

package com.robynem.mit.web.persistence.dao;

import com.robynem.mit.web.persistence.entity.ClubEntity;

/**
 * Created by robyn_000 on 18/04/2016.
 */
public interface ClubDao {

    ClubEntity getClubById(Long clubId);

    ClubEntity createStageVersion(Long clubId);

    ClubEntity createStageVersion(ClubEntity clubEntity);

    ClubEntity createEmptyClub(Long clubId);

    ClubEntity getClubGeneralInfo(Long clubId);

    ClubEntity getClubMedia(Long clubId);

    void update(ClubEntity clubEntity);
}

package com.robynem.mit.web.persistence.dao;

import com.robynem.mit.web.persistence.entity.UserEntity;

import java.util.List;

/**
 * Created by roberto on 12/12/2015.
 */
public interface AccountDao {

    UserEntity addUser(UserEntity entity);

    UserEntity getUserById(Long id);

    UserEntity getUserByIdWithProfileImage(Long id);

    UserEntity getUserByEmailAddress(String emailAddress);

    UserEntity getUserByFacebookId(Long facebookId);

    UserEntity getUserByEmailAndPassword(String emailAddress, String password);

    void updateUser(UserEntity userEntity);

    List<UserEntity> filterMusiciansByNameForAutocomplete(Long bandId, String name);
}

package com.robynem.mit.web.controller.profile;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.model.authentication.PortalUserModel;
import com.robynem.mit.web.model.profile.MusicianModel;
import com.robynem.mit.web.model.profile.ProfileModel;
import com.robynem.mit.web.persistence.dao.*;
import com.robynem.mit.web.persistence.entity.MusicGenreEntity;
import com.robynem.mit.web.persistence.entity.MusicalInstrumentEntity;
import com.robynem.mit.web.persistence.entity.UserEntity;
import com.robynem.mit.web.util.Constants;
import com.robynem.mit.web.util.MessageSeverity;
import com.robynem.mit.web.util.PortalHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.util.*;

/**
 * Created by robyn_000 on 26/12/2015.
 */
@Controller
@RequestMapping("/private/profile")
public class ProfileController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UtilsDao<UserEntity> userUtilsDao;

    @Autowired
    private RegistryDao registryDao;

    @Autowired
    private MediaDao mediaDao;

    @Value("${media.image.max-size}")
    private long maxFileSize;

    @RequestMapping("/editProfile")
    public ModelAndView showEditProfile(@ModelAttribute ModelMap modelMap) {

        try {
            PortalUserModel portalUserModel = this.getAuthenticatedUser();

            UserEntity userEntity = this.userUtilsDao.getByIdWithFetchedObjects(UserEntity.class, portalUserModel.getId(),
                    "profileImage", "preferredMusicGenres", "playedMusicInstruments", "masterInstruments");

            modelMap.addAttribute("profileModel", this.getProfileModel(userEntity));

            modelMap.addAttribute("musicalInstrumentsList", this.registryDao.getAllMusicalInstruments());

            modelMap.addAttribute("musicGenresList", this.registryDao.getAllMusicGenres());

            // If it's not a post back, loads musician form from db
            if (!this.isPostBack()) {
                modelMap.addAttribute("musicianModel", this.getMusicianModel(userEntity));
            }
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        } finally {
            // Force garbage collector to free memory.
            System.gc();
        }

        return new ModelAndView("profile/editProfile", modelMap);
    }

    @RequestMapping(value = "/updateFirstName", method = RequestMethod.POST)
    public AbstractView updateFirstName(@RequestParam String value) {
        ModelMap modelMap = new ModelMap();

        boolean success = false;

        try {
            PortalUserModel portalUserModel = this.getAuthenticatedUser();

            if (portalUserModel != null) {
                UserEntity userEntity = this.accountDao.getUserById(portalUserModel.getId());

                if (userEntity != null) {
                    if (StringUtils.isNotBlank(value)) {
                        userEntity.setFirstName(value);
                        userEntity.setUpdated(Calendar.getInstance().getTime());

                        this.accountDao.updateUser(userEntity);

                        portalUserModel.setFirstName(userEntity.getFirstName());

                        success = true;
                    } else {
                        this.addApplicationMessage(this.getMessage("profile.validation.first-name-invalid"), MessageSeverity.FATAL, null, modelMap);
                    }
                } else {
                    throw new Exception("userEntity is NULL");
                }
            } else {
                throw new Exception("portalUserModel is NULL");
            }

        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        modelMap.put("success", success);

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/updateLastName", method = RequestMethod.POST)
    public AbstractView updateLastName(@RequestParam String value) {
        ModelMap modelMap = new ModelMap();

        boolean success = false;

        try {
            PortalUserModel portalUserModel = this.getAuthenticatedUser();

            if (portalUserModel != null) {
                UserEntity userEntity = this.accountDao.getUserById(portalUserModel.getId());

                if (userEntity != null) {
                    if (StringUtils.isNotBlank(value)) {
                        userEntity.setLastName(value);
                        userEntity.setUpdated(Calendar.getInstance().getTime());

                        this.accountDao.updateUser(userEntity);

                        portalUserModel.setLastName(userEntity.getFirstName());

                        success = true;
                    } else {
                        this.addApplicationMessage(this.getMessage("profile.validation.last-name-invalid"), MessageSeverity.FATAL, null, modelMap);
                    }
                } else {
                    throw new Exception("userEntity is NULL");
                }
            } else {
                throw new Exception("portalUserModel is NULL");
            }

        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        modelMap.put("success", success);

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/updateGender", method = RequestMethod.POST)
    public AbstractView updateGender(@RequestParam String value) {
        ModelMap modelMap = new ModelMap();

        boolean success = false;

        try {
            PortalUserModel portalUserModel = this.getAuthenticatedUser();

            if (portalUserModel != null) {
                UserEntity userEntity = this.accountDao.getUserById(portalUserModel.getId());

                if (userEntity != null) {
                    if (StringUtils.isNotBlank(value)) {
                        userEntity.setGender(value);
                        userEntity.setUpdated(Calendar.getInstance().getTime());

                        this.accountDao.updateUser(userEntity);

                        success = true;
                    } else {
                        this.addApplicationMessage(this.getMessage("profile.validation.gender-invalid"), MessageSeverity.FATAL, null, modelMap);
                    }
                } else {
                    throw new Exception("userEntity is NULL");
                }
            } else {
                throw new Exception("portalUserModel is NULL");
            }

        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        modelMap.put("success", success);

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/updateBirthDate", method = RequestMethod.POST)
    public AbstractView updateBirthDate(@RequestParam String value) {
        ModelMap modelMap = new ModelMap();

        boolean success = false;

        try {
            PortalUserModel portalUserModel = this.getAuthenticatedUser();

            if (portalUserModel != null) {
                UserEntity userEntity = this.accountDao.getUserById(portalUserModel.getId());

                if (userEntity != null) {
                    Date birthDate = PortalHelper.parseDate(value, this.request.getLocale(), DateFormat.SHORT);
                    if (birthDate != null) {
                        userEntity.setBirthDate(birthDate);
                        userEntity.setUpdated(Calendar.getInstance().getTime());

                        this.accountDao.updateUser(userEntity);

                        success = true;
                    } else {
                        this.addApplicationMessage(this.getMessage("profile.validation.birth-date-invalid"), MessageSeverity.FATAL, null, modelMap);
                    }
                } else {
                    throw new Exception("userEntity is NULL");
                }
            } else {
                throw new Exception("portalUserModel is NULL");
            }

        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        modelMap.put("success", success);

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/updateTown", method = RequestMethod.POST)
    public AbstractView updateTown(@RequestParam String placeId, @RequestParam String town) {
        ModelMap modelMap = new ModelMap();

        boolean success = false;

        try {
            PortalUserModel portalUserModel = this.getAuthenticatedUser();

            if (portalUserModel != null) {
                UserEntity userEntity = this.accountDao.getUserById(portalUserModel.getId());

                if (userEntity != null) {
                    if (StringUtils.isNotBlank(placeId) && StringUtils.isNotBlank(town)) {
                        userEntity.setPlaceId(placeId);
                        userEntity.setTown(town);
                        userEntity.setUpdated(Calendar.getInstance().getTime());

                        this.accountDao.updateUser(userEntity);

                        success = true;
                    } else {
                        this.addApplicationMessage(this.getMessage("profile.validation.town-invalid"), MessageSeverity.FATAL, null, modelMap);
                    }
                } else {
                    throw new Exception("userEntity is NULL");
                }
            } else {
                throw new Exception("portalUserModel is NULL");
            }

        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        modelMap.put("success", success);

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/uploadProfileImage", method = RequestMethod.POST)
    public ModelAndView uploadProfileImage(@RequestParam MultipartFile pictureFile, ModelMap modelMap) {

        try {
            if (!pictureFile.isEmpty()) {

                if (!StringUtils.trimToEmpty(pictureFile.getContentType()).toLowerCase().contains("image")) {
                    this.addApplicationMessage(this.getMessage("profile.validation.invalid-image"),
                            MessageSeverity.FATAL, null, modelMap);
                } else if(pictureFile.getSize() > this.maxFileSize) {
                    this.addApplicationMessage(this.getMessage("profile.validation.image-to-large"),
                            MessageSeverity.FATAL, null, modelMap);
                } else {
                    PortalUserModel portalUserModel = this.getAuthenticatedUser();

                    ByteArrayInputStream bais = new ByteArrayInputStream(pictureFile.getBytes());

                    this.mediaDao.updateUserProfileImage(portalUserModel.getId(), bais);
                }
            }
        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
        }

        return new ModelAndView("forward:/private/profile/editProfile", modelMap);
    }

    @RequestMapping(value = "/saveMusicianForm", method = RequestMethod.POST)
    public ModelAndView saveMusicianForm(ModelMap modelMap, @ModelAttribute MusicianModel musicianModel) {
        try {

            if (this.validateMusicianForm(musicianModel, modelMap)) {
                PortalUserModel portalUserModel = this.getAuthenticatedUser();

                UserEntity userEntity = this.userUtilsDao.getByIdWithFetchedObjects(UserEntity.class, portalUserModel.getId(),
                        "preferredMusicGenres", "playedMusicInstruments", "masterInstruments");

                // Updates musician fields
                userEntity.setMusician(musicianModel.isMusician());
                userEntity.setSinger(musicianModel.isSinger());
                userEntity.setMusicMaster(musicianModel.isMusicMaster());
                userEntity.setEngagementAvailable(musicianModel.isEngagementAvailable());
                userEntity.setBiography(musicianModel.getBiography());
                userEntity.setDiscJockey(musicianModel.isDiscJockey());

                userEntity.setPlayedMusicInstruments(new HashSet<MusicalInstrumentEntity>());
                for (String id : musicianModel.getPlayedInstruments()) {
                    userEntity.getPlayedMusicInstruments().add(new MusicalInstrumentEntity(Long.valueOf(id)));
                }

                userEntity.setPreferredMusicGenres(new HashSet<MusicGenreEntity>());
                for (String id : musicianModel.getPreferredGenres()) {
                    userEntity.getPreferredMusicGenres().add(new MusicGenreEntity(Long.valueOf(id)));
                }

                userEntity.setMasterInstruments(new HashSet<MusicalInstrumentEntity>());
                for (String id : musicianModel.getTeachedInstruments()) {
                    userEntity.getMasterInstruments().add(new MusicalInstrumentEntity(Long.valueOf(id)));
                }

                this.accountDao.updateUser(userEntity);

                this.addApplicationMessage(this.getMessage("profile.musician-form.save.succes"), MessageSeverity.INFO, null, modelMap);
            }
        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
            this.setPostBack(true);
        }

        return new ModelAndView("forward:/private/profile/editProfile", modelMap);
    }

    private boolean validateMusicianForm(MusicianModel musicianModel, ModelMap modelMap) {
        boolean valid = true;

        if (musicianModel.isMusician()) {
            if (!musicianModel.isSinger() && !musicianModel.isDiscJockey() && (musicianModel.getPlayedInstruments() == null || musicianModel.getPlayedInstruments().size() == 0)) {
                this.addApplicationMessage(this.getMessage("profile.musician-form.validation.vocalist-or-instruments-mandatory"), MessageSeverity.FATAL, null, modelMap);
                valid = false;
            }

            if (musicianModel.isMusicMaster()) {
                if (musicianModel.getTeachedInstruments() == null || musicianModel.getTeachedInstruments().size() == 0) {
                    this.addApplicationMessage(this.getMessage("profile.musician-form.validation.teached-instruments-mandatory"), MessageSeverity.FATAL, null, modelMap);
                    valid = false;
                }
            }
        }

        if (!valid) {
            this.setPostBack(true);
        }

        return valid;
    }

    private ProfileModel getProfileModel(UserEntity userEntity) {
        ProfileModel profileModel = null;

        if (userEntity != null) {
            profileModel = new ProfileModel();
            profileModel.setFirstName(userEntity.getFirstName());
            profileModel.setLastName(userEntity.getLastName());
            profileModel.setGender(userEntity.getGender());
            profileModel.setEmailAddress(userEntity.getEmailAddress());
            profileModel.setBirthDate(PortalHelper.formatDate(userEntity.getBirthDate(), this.request.getLocale()));
            if (userEntity.getBirthDate() != null) {
                profileModel.setBirthDateMillis(userEntity.getBirthDate().getTime());
            }
            profileModel.setPlaceId(userEntity.getPlaceId());
            profileModel.setTown(userEntity.getTown());
            if (userEntity.getProfileImage() != null) {
                profileModel.setProfileImageId(userEntity.getProfileImage().getId());
            }

        }

        return profileModel;
    }

    private MusicianModel getMusicianModel(UserEntity userEntity) {
        MusicianModel musicianModel = null;

        if (userEntity != null) {
            musicianModel = new MusicianModel();
            musicianModel.setMusician(userEntity.isMusician());
            musicianModel.setBiography(userEntity.getBiography());
            musicianModel.setEngagementAvailable(userEntity.isEngagementAvailable());
            musicianModel.setMusicMaster(userEntity.isMusicMaster());
            musicianModel.setSinger(userEntity.isSinger());
            musicianModel.setDiscJockey(userEntity.isDiscJockey());

            if (userEntity.getPreferredMusicGenres() != null) {
                for (MusicGenreEntity item : userEntity.getPreferredMusicGenres()) {
                    musicianModel.getPreferredGenres().add(String.valueOf(item.getId()));
                }
            }

            if (userEntity.getPlayedMusicInstruments() != null) {
                for (MusicalInstrumentEntity item : userEntity.getPlayedMusicInstruments()) {
                    musicianModel.getPlayedInstruments().add(String.valueOf(item.getId()));
                }
            }

            if (userEntity.getMasterInstruments() != null) {
                for (MusicalInstrumentEntity item : userEntity.getMasterInstruments()) {
                    musicianModel.getTeachedInstruments().add(String.valueOf(item.getId()));
                }
            }
        }

        return musicianModel;
    }
}

package com.robynem.mit.web.controller.authentication;


import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.model.authentication.MitLoginForm;
import com.robynem.mit.web.model.authentication.PortalUserModel;
import com.robynem.mit.web.model.authentication.SignUpModel;
import com.robynem.mit.web.model.common.MessageModel;
import com.robynem.mit.web.persistence.dao.AccountDao;
import com.robynem.mit.web.persistence.entity.UserEntity;
import com.robynem.mit.web.util.*;
import facebook4j.User;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;

import java.rmi.server.UID;
import java.util.*;

/**
 * Created by roberto on 06/12/2015.
 */
@Controller
@RequestMapping("/authentication")
public class AuthenticationController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private FacebookHelper fbHelper;

    @Autowired
    private AccountDao accountDao;

    @RequestMapping("/fbLogin")
    public ModelAndView fbLogin(@ModelAttribute("mitLoginForm")MitLoginForm mitLoginForm) {

        ModelMap mp = new ModelMap();
        User fbUser = null;
        UserEntity userEntity = null;
        List<MessageModel> messages = new ArrayList<MessageModel>();

        boolean mustAdd = false;

        String view = mitLoginForm.getRedirectView();

        try {
            fbUser = this.fbHelper.getFacebookUser(mitLoginForm.getAccessToken());

            if (StringUtils.isNotBlank(fbUser.getEmail())) {
                userEntity = accountDao.getUserByEmailAddress(fbUser.getEmail());

                if (userEntity == null) {
                    mustAdd = true;

                    userEntity = accountDao.getUserByFacebookId(Long.parseLong(fbUser.getId()));

                    if (userEntity == null) {
                        mustAdd = true;
                    }
                    else {
                        mustAdd = false;
                    }
                }

                if (mustAdd) {
                    if (this.validateFacebookUser(fbUser, messages)) {
                        userEntity = new UserEntity();
                        userEntity.setCreated(Calendar.getInstance().getTime());
                        userEntity.setEmailAddress(fbUser.getEmail());
                        userEntity.setFacebookId(Long.parseLong(fbUser.getId()));

                        if (StringUtils.isNotBlank(fbUser.getFirstName())) {
                            userEntity.setFirstName(fbUser.getFirstName());
                        } else {
                            userEntity.setFirstName(fbUser.getName());
                        }
                        userEntity.setLastName(fbUser.getLastName());

                        if (StringUtils.isNotBlank(fbUser.getGender())) {
                            if (UserGender.MALE.toString().equalsIgnoreCase(fbUser.getGender())) {
                                userEntity.setGender(UserGender.MALE.toString());
                            } else if (UserGender.FEMALE.toString().equalsIgnoreCase(fbUser.getGender())) {
                                userEntity.setGender(UserGender.FEMALE.toString());
                            }
                        }

                        userEntity.setLastLogin(Calendar.getInstance().getTime());
                        userEntity.setPassword(PortalHelper.generatePassword());

                        userEntity = accountDao.addUser(userEntity);
                    }

                }
            } else {
                messages.add(new MessageModel(this.getMessage("login.facebook.user-not-found"), MessageSeverity.WARNING));
            }

            /*TEST PURPOSE*/
            /*messages.add(new MessageModel("Messaggio di info", MessageSeverity.INFO));
            messages.add(new MessageModel("Messaggio di warning", MessageSeverity.WARNING));
            messages.add(new MessageModel("Messaggio di error", MessageSeverity.FATAL));
            messages.add(new MessageModel("Messaggio con link", MessageSeverity.INFO, "/"));*/

            if (userEntity != null) {
                this.authenticate(userEntity);
            }


            this.addApplicationMessages(messages, mp);

            if (mustAdd && messages.size() == 0) {
                this.addApplicationMessage(this.getMessage("login.complete-profile", userEntity.getFirstName()), MessageSeverity.INFO, null,  mp);
                view = "forward://private/profile/editProfile";
            }

        } catch (Exception e) {
            this.manageException(e, LOG, mp);
        }

        return new ModelAndView(view, mp);
    }

    @RequestMapping("/showEmailLogin")
    public ModelAndView showEmailSignIn() {
        ModelAndView mv = new ModelAndView("authentication/emailLogin");

        return mv;
    }

    @RequestMapping("/showSignUp")
    public ModelAndView showSignUp() {
        ModelAndView mv = new ModelAndView("authentication/signup");

        return mv;
    }

    @RequestMapping("/checkEmail")
    public AbstractView checkEmail(@RequestParam("email") String email) {
        boolean exists = this.accountDao.getUserByEmailAddress(email) != null;

        return this.getJsonView("exists", exists);
    }

    @RequestMapping("/signup")
    public ModelAndView signup(@ModelAttribute SignUpModel signUpModel) {
        ModelAndView mv = new ModelAndView("forward:/private/profile/editProfile");

        ModelMap mp = new ModelMap();

        try {
            UserEntity userEntity = new UserEntity();
            userEntity = new UserEntity();

            userEntity.setCreated(Calendar.getInstance().getTime());
            userEntity.setEmailAddress(StringUtils.trimToEmpty(signUpModel.getEmailAddress()));

            userEntity.setFirstName(StringUtils.trimToEmpty(signUpModel.getFirstName()));
            userEntity.setLastName(StringUtils.trimToEmpty(signUpModel.getLastName()));
            userEntity.setLastLogin(Calendar.getInstance().getTime());
            userEntity.setPassword(StringUtils.trimToEmpty(signUpModel.getPassword()));

            userEntity = accountDao.addUser(userEntity);

            if (userEntity != null) {
                this.authenticate(userEntity);
            }

            this.addApplicationMessage(this.getMessage("login.complete-profile", userEntity.getFirstName()), MessageSeverity.INFO, null, null);

        } catch (Exception e) {
            this.manageException(e, LOG, mp);
        }

        return mv;
    }

    @RequestMapping("/logout")
    public String logout() {
        this.deauthenticate();

        return "redirect:/index";
    }

    @RequestMapping("/redirect")
    public String redirect(@RequestParam("redirectView") String redirectView) {
        return redirectView;
    }

    @RequestMapping(value = "/emailLogin", method = RequestMethod.POST)
    public AbstractView emailLogin(@ModelAttribute MitLoginForm mitLoginForm) {
        Map<String, Object> model = new HashMap<String, Object>();

        boolean valid = false;
        String loginMessage = null;

        try {
            if (StringUtils.isNotBlank(mitLoginForm.getEmailAddress()) && StringUtils.isNotBlank(mitLoginForm.getPassword())) {

                LOG.debug("Trying to authenticate user with email {}", StringUtils.trimToEmpty(mitLoginForm.getEmailAddress()));

                UserEntity userEntity = this.accountDao.getUserByEmailAddress(StringUtils.trimToEmpty(mitLoginForm.getEmailAddress()));

                // Checks if this email is registered.
                if (userEntity == null) {
                    valid = false;
                    loginMessage = this.getMessage("login.account-not-present");
                } else {
                    userEntity = this.accountDao.getUserByEmailAndPassword(StringUtils.trimToEmpty(mitLoginForm.getEmailAddress()),
                            StringUtils.trimToEmpty(mitLoginForm.getPassword()));

                    if (userEntity != null) {
                        valid = true;
                        this.authenticate(userEntity);
                    } else {
                        loginMessage = this.getMessage("login.invalid-credentials");
                    }
                }
            }

            model.put("success", valid);

            if (!valid) {
                model.put("loginMessage", loginMessage);
            }

            // For test purpose
            //throw new Exception("Errore di prova");
        } catch (Exception e) {
            this.manageException(e, LOG, model);
        }

        return this.getJsonView(model);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public AbstractView doResetPassword(@RequestParam("emailAddress") String emailAddress) {

        Map<String, Object> model = new HashMap<String, Object>();

        boolean success = true;
        String resetMessage = null;
        try {
            UserEntity userEntity = this.accountDao.getUserByEmailAddress(emailAddress);

            if (userEntity == null) {
                success = false;
                resetMessage = this.getMessage("login.account-not-present");
            } else {
                userEntity.setPassword(PortalHelper.generatePassword());
                userEntity.setUpdated(Calendar.getInstance().getTime());

                this.accountDao.updateUser(userEntity);
            }

            model.put("success", success);
            model.put("resetMessage", resetMessage);
        } catch (Exception e) {
            this.manageException(e, LOG, model);
        }

        return this.getJsonView(model);
    }

    @RequestMapping("/resetPasswordCompleted")
    public ModelAndView resetPasswordCompleted(@RequestParam("redirectView") String redirectView, ModelMap modelMap) {

        try {
            this.addApplicationMessage(this.getMessage("login.reset-password.completed"), MessageSeverity.INFO, null, modelMap);
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        return new ModelAndView(redirectView, modelMap);
    }

    /**
     * Checks if user is authenticated on ajax callback
     * @return
     */
    @RequestMapping("/isAuthenticated")
    public AbstractView isAuthenticated() {
        ModelMap modelMap = new ModelMap();

        boolean authenticated = false;

        try {
            PortalUserModel portalUserModel = this.getAuthenticatedUser();

            if (portalUserModel != null) {
                authenticated = true;
            }
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        modelMap.put("authenticated", authenticated);

        return this.getJsonView(modelMap);
    }

    @RequestMapping("/sessionTimeout")
    public ModelAndView sessionTimeOut(ModelMap modelMap) {
        try {
            this.addApplicationMessage(this.getMessage("login.session-timeout"), MessageSeverity.WARNING, null, modelMap);

        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        return new ModelAndView("/index", modelMap);
    }

    private boolean validateFacebookUser(User fbUser, List<MessageModel> errors) {
        boolean valid = true;

        if (StringUtils.isBlank(fbUser.getEmail())) {
            errors.add(new MessageModel(this.getMessage("login.facebook.email-not-retrieved"), MessageSeverity.WARNING));
            valid = false;
        }

        if (valid) {
            if (StringUtils.isBlank(fbUser.getFirstName()) && StringUtils.isBlank(fbUser.getName())) {
                errors.add(new MessageModel(this.getMessage("login.facebook.missing-data"), MessageSeverity.WARNING));
                valid = false;
            }
        }

        return valid;
    }
}

package com.robynem.mit.web.controller;

import com.robynem.mit.web.model.authentication.PortalUserModel;
import com.robynem.mit.web.model.common.MessageModel;
import com.robynem.mit.web.persistence.dao.AccountDao;
import com.robynem.mit.web.persistence.entity.UserEntity;
import com.robynem.mit.web.util.Constants;
import com.robynem.mit.web.util.MessageSeverity;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by roberto on 14/12/2015.
 */
@Scope("request")
public class BaseController {

    @Autowired
    protected ResourceBundleMessageSource messageSource;

    @Autowired
    protected HttpServletRequest request;


    @Autowired
    protected HttpSession session;

    @Autowired
    private AccountDao accountDao;

    public String getMessage(String code) {
        return this.messageSource.getMessage(code, null, this.request.getLocale());
    }

    public String getMessage(String code, String... args) {
        return this.messageSource.getMessage(code, args, request.getLocale());
    }

    public MappingJackson2JsonView getJsonView(String key, Object value) {

        final MappingJackson2JsonView mappingJacksonJsonView = new MappingJackson2JsonView();

        mappingJacksonJsonView.addStaticAttribute(key, value);

        return mappingJacksonJsonView;
    }

    public MappingJackson2JsonView getJsonView(Map<String, Object> objects) {

        final MappingJackson2JsonView mappingJacksonJsonView = new MappingJackson2JsonView();

        for (String key : objects.keySet()) {
            mappingJacksonJsonView.addStaticAttribute(key, objects.get(key));
        }
        return mappingJacksonJsonView;
    }

    public MappingJackson2JsonView getJsonView(ModelMap modelMap) {

        final MappingJackson2JsonView mappingJacksonJsonView = new MappingJackson2JsonView();

        for (String key : modelMap.keySet()) {
            mappingJacksonJsonView.addStaticAttribute(key, modelMap.get(key));
        }
        return mappingJacksonJsonView;
    }

    public void authenticate(UserEntity userEntity) {
        if (userEntity != null) {

            PortalUserModel portalUserModel = new PortalUserModel();

            portalUserModel.setId(userEntity.getId());
            portalUserModel.setFirstName(userEntity.getFirstName());
            portalUserModel.setLastName(userEntity.getLastName());
            portalUserModel.setEmailAddress(userEntity.getEmailAddress());
            portalUserModel.setFacebookId(userEntity.getFacebookId());

            this.session.setAttribute(Constants.PORTAL_USER_KEY, portalUserModel);

            userEntity.setLastLogin(Calendar.getInstance().getTime());

            this.accountDao.updateUser(userEntity);
        }
    }

    public void deauthenticate() {
        if (this.session.getAttribute(Constants.PORTAL_USER_KEY) != null) {
            this.session.removeAttribute(Constants.PORTAL_USER_KEY);
        }
    }

    public void manageException(Throwable e, Logger logger, ModelMap model) {
        logger.error(e.getMessage(), e);

        List<MessageModel> messages = new ArrayList<MessageModel>();

        messages.add(new MessageModel(this.getMessage("global.application-error"), MessageSeverity.FATAL));

        model.addAttribute(Constants.APPLICATION_MESSAGES_KEY, messages);
    }

    public void manageException(Throwable e, Logger logger, Map<String, Object> model) {
        logger.error(e.getMessage(), e);

        List<MessageModel> messages = new ArrayList<MessageModel>();

        messages.add(new MessageModel(this.getMessage("global.application-error"), MessageSeverity.FATAL));

        model.put(Constants.APPLICATION_MESSAGES_KEY, messages);
    }

    public void addApplicationMessage(String message, MessageSeverity severity, String link, ModelMap modelMap) {
        MessageModel messageModel = new MessageModel(message, severity, link);

        List<MessageModel> messages = null;

        if (modelMap != null) {
            messages = (List<MessageModel>) modelMap.get(Constants.APPLICATION_MESSAGES_KEY);
        } else {
            messages = (List<MessageModel>) this.request.getAttribute(Constants.APPLICATION_MESSAGES_KEY);
        }


        if (messages == null) {
            messages = new ArrayList<MessageModel>();
        }

        messages.add(messageModel);

        if (modelMap != null) {
            modelMap.addAttribute(Constants.APPLICATION_MESSAGES_KEY, messages);
        } else {
            this.request.setAttribute(Constants.APPLICATION_MESSAGES_KEY, messages);
        }

    }

    public void addApplicationMessages(List<MessageModel> messagesToAdd, ModelMap modelMap) {

        List<MessageModel> messages = (List<MessageModel>) modelMap.get(Constants.APPLICATION_MESSAGES_KEY);

        if (messages == null) {
            messages = new ArrayList<MessageModel>();
        }

        messages.addAll(messagesToAdd);

        if (modelMap != null) {
            modelMap.addAttribute(Constants.APPLICATION_MESSAGES_KEY, messages);
        } else {
            this.request.setAttribute(Constants.APPLICATION_MESSAGES_KEY, messages);
        }

    }

    public PortalUserModel getAuthenticatedUser() {
        return (PortalUserModel) this.session.getAttribute(Constants.PORTAL_USER_KEY);
    }

    public void setPostBack(boolean value) {
        this.request.setAttribute(Constants.IS_POST_BACK, value);
    }

    public boolean isPostBack() {
        Boolean value = (Boolean)this.request.getAttribute(Constants.IS_POST_BACK);

        if (value != null && value.booleanValue()) {
            return true;
        } else {
            return false;
        }
    }

    public void addSessionAttribute(String attributeName, Object attributeValue) {
        this.session.setAttribute(attributeName, attributeValue);
    }

    public <T> T getSessionAttribute(String attributeName) {
        return (T) this.session.getAttribute(attributeName);
    }

    public void removeSessionAddtribute(String attributeName) {
        this.session.removeAttribute(attributeName);
    }

    public void addRequestAttribute(String attributeName, Object attributeValue) {
        this.request.setAttribute(attributeName, attributeValue);
    }

    public <T> T getRequestAttribute(String attributeName) {
        return (T) this.request.getAttribute(attributeName);
    }

    public void removeRequestAddtribute(String attributeName) {
        this.request.removeAttribute(attributeName);
    }

    public String getContextPath() {
        return this.request.getContextPath();
    }
}

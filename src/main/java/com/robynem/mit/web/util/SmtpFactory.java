package com.robynem.mit.web.util;

import com.robynem.mit.web.persistence.dao.AccountDao;
import com.robynem.mit.web.persistence.dao.BandDao;
import com.robynem.mit.web.persistence.dao.UtilsDao;
import com.robynem.mit.web.persistence.entity.BandEntity;
import com.robynem.mit.web.persistence.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by robyn_000 on 13/04/2016.
 */
@Service
public class SmtpFactory {

    private static String BAND_COMPONENT_INTIVATION_CONTENT_FILE = "bandComponentInvitationEmail.txt";

    @Autowired
    @Qualifier(value = "mailSender")
    private JavaMailSender javaMailSender;

    @Autowired
    protected ResourceBundleMessageSource messageSource;

    @Autowired
    private BandDao bandDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UtilsDao<BandEntity> utilsBandDao;

    @Value("${mail.from}")
    private String from;

    @Value("${mail.from-name}")
    private String fromName;

    @Value("${portal-url}")
    private String portalUrl;

    private Locale defaultLocale = Locale.ENGLISH;

    public SmtpHelper getBandComponentInvitation(Long bandId, Long senderUserId, Long receiverUserId) {
        SmtpHelper smtpHelper = this.initSmtpHelper();

        UserEntity receiverUser = this.accountDao.getUserById(receiverUserId);
        UserEntity senderUser = this.accountDao.getUserById(senderUserId);
        BandEntity bandEntity = this.utilsBandDao.getByIdWithFetchedObjects(BandEntity.class, bandId, "stageVersions", "publishedVersion");

        if (bandEntity.getStageVersions() != null && bandEntity.getStageVersions().size() > 0) {
            bandEntity = bandEntity.getStageVersions().get(0);
        }

        final BandEntity finalBandEntity = bandEntity;

        Locale locale = this.defaultLocale;

        if (StringUtils.isNotBlank(receiverUser.getLanguage())) {
            locale = Locale.forLanguageTag(receiverUser.getLanguage());
        }

        smtpHelper.setLocale(locale);
        smtpHelper.setSubject(this.messageSource.getMessage("mail.subject.band-component-invitation", null, locale));
        smtpHelper.setContentFileName(BAND_COMPONENT_INTIVATION_CONTENT_FILE);
        smtpHelper.setTo(receiverUser.getEmailAddress());

        SmtpFactory $this = this;

        smtpHelper.setParameters(new HashMap<String, String>() {
            {
                put("${receiver-name}", receiverUser.getFirstName() + " " + receiverUser.getLastName());
                put("${sender-name}", senderUser.getFirstName() + " " + senderUser.getLastName());
                put("${view-band-url}", $this.portalUrl +  "viewBand?bandId=" + bandId);
                put("${band-name}", finalBandEntity.getName());
                put("${portal-url}", $this.portalUrl);
            }
        });

        return smtpHelper;
    }

    private SmtpHelper initSmtpHelper() {
        SmtpHelper smtpHelper = new SmtpHelper();
        smtpHelper.setJavaMailSender(this.javaMailSender);
        smtpHelper.setFrom(this.from);
        smtpHelper.setFromName(this.fromName);

        return smtpHelper;
    }
}

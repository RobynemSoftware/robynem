package com.robynem.mit.web.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.rmi.server.UID;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by robyn_000 on 16/12/2015.
 */
public class PortalHelper {

    static final Logger LOG = LoggerFactory.getLogger(PortalHelper.class);

    private PortalHelper() {

    }

    public static String generatePassword() {
        return StringUtils.substring(new UID().toString(), 0, Constants.AUTOMATIC_PASSWORD_LENGHT);
    }

    public static String formatDate(Date date, Locale locale) {
        String dateString = null;

        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(getDateFormat(locale, DateFormat.SHORT), locale);

                dateString = sdf.format(date);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }


        return dateString;
    }

    public static Date parseDate(String value, Locale locale, int dateFormat) {
        Date date = null;

        if (value != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(getDateFormat(locale, dateFormat), locale);

                date = sdf.parse(value);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return date;
    }

    public static String getDateFormat(Locale locale, int dateFormat) {
        Locale[] availableLocales = DateFormat.getAvailableLocales();
        Locale currentLocale = locale;

        boolean match = false;
        for (Locale checkLoc : availableLocales) {
            if (checkLoc.equals(currentLocale)) {
                match = true;
            }
        }

        if (!match) {
            currentLocale = Locale.US;
        }

        DateFormat f = DateFormat.getDateInstance(dateFormat, currentLocale);
        SimpleDateFormat sf = (SimpleDateFormat) f;

        String format = sf.toLocalizedPattern();

        return format;
    }



    public synchronized static Long getUniqueId() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static Blob getBlob(InputStream stream) throws IOException, SQLException {
        return new SerialBlob(IOUtils.toByteArray(stream));
    }

    public static String replaceSpecialCharactersForHtml(String value) {
        if (value != null) {
            value = value.replaceAll("è", "&egrave;").replaceAll("é", "&egrave;").replaceAll("ò", "&ograve;").replaceAll("à", "&agrave;").replaceAll("ù", "&ugrave;");
        }

        return value;
    }

    public static String createInSqlClause(List<Long> values) {
        StringBuilder builder = new StringBuilder();

        if (values == null || !values.stream().findAny().isPresent()) {
            throw new RuntimeException("Values cannot be empty!");
        }

        String placeholder = "(%s)";

        int counter = 0;
        for (Long obj : values) {
            counter++;

            builder.append(obj.toString());

            if (counter < values.size()) {
                builder.append(",");
            }
        }

        return String.format(placeholder, builder.toString());
    }

    public static void createDirectory(String name) throws Exception {
        File dir = new File(name);

        if (!dir.exists()) {
            dir.mkdirs();
            dir.setWritable(true);
        }
    }

    public static void saveFile(InputStream stream, String directory, String destinationFileName) throws Exception {

        createDirectory(directory);

        try (FileOutputStream fos = new FileOutputStream(directory + destinationFileName)) {
            byte[] buff = new byte[stream.available()];
            stream.read(buff);

            fos.write(buff);
        }
    }


}

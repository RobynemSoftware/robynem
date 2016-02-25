package com.robynem.mit.web.controller.media;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.persistence.dao.AccountDao;
import com.robynem.mit.web.persistence.dao.MediaDao;
import com.robynem.mit.web.persistence.entity.ImageEntity;
import com.robynem.mit.web.util.ImageSize;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by robyn_000 on 04/01/2016.
 */
@Controller
@RequestMapping("/media")
public class MediaController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(MediaController.class);

    @Value("${media.users-images.folder}")
    private String userImagesBasePath;

    @Value("${media.bands-images.folder}")
    private String bandImagesBasePath;

    @Value("${media.image-format}")
    private String imageFormat;

    @Autowired
    private MediaDao mediaDao;

    @RequestMapping("/getUserImage")
    public ResponseEntity<byte[]> getUserImage(@RequestParam Long imageId, @RequestParam(required = false) ImageSize size, HttpServletResponse response) {

        return this.getImage(imageId, size, response);
    }

    @RequestMapping("/getBandImage")
    public ResponseEntity<byte[]> getBandImage(@RequestParam Long imageId, @RequestParam(required = false) ImageSize size, HttpServletResponse response) {

        return this.getImage(imageId, size, response);
    }

    @RequestMapping("/getImage")
    public ResponseEntity<byte[]> getImage(@RequestParam Long imageId, @RequestParam(required = false) ImageSize size, HttpServletResponse response) {

        InputStream is = null;
        try {
            ImageEntity imageEntity = this.mediaDao.getImageById(imageId);

            if (imageEntity != null) {
                if (size == null) {
                    size = ImageSize.ORIGINAL;
                }

                switch (size) {
                    case SMALL:
                        is = imageEntity.getSmallFile() != null ? imageEntity.getSmallFile().getBinaryStream() : null;
                        break;

                    case MEDIUM:
                        is = imageEntity.getMediumFile() != null ? imageEntity.getMediumFile().getBinaryStream() : null;
                        break;

                    case LARGE:
                        is = imageEntity.getLargeFile() != null ? imageEntity.getLargeFile().getBinaryStream() : null;
                        break;

                    case ORIGINAL:
                        is = imageEntity.getOriginalFile() != null ? imageEntity.getOriginalFile().getBinaryStream() : null;
                        break;
                }
            }


            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<byte[]>(IOUtils.toByteArray(is), headers, HttpStatus.CREATED);

        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage());
                }
            }

            // Force garbage collector to free memory.
            System.gc();
        }

        return null;
    }


}

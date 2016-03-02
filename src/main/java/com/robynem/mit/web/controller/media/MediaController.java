package com.robynem.mit.web.controller.media;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.persistence.dao.MediaDao;
import com.robynem.mit.web.persistence.entity.AudioEntity;
import com.robynem.mit.web.persistence.entity.ImageEntity;
import com.robynem.mit.web.util.ImageSize;
import net.sourceforge.javaflacencoder.AudioStreamEncoder;
import net.sourceforge.javaflacencoder.FLACEncoder;
import net.sourceforge.javaflacencoder.FLAC_FileEncoder;
import org.apache.commons.io.IOUtils;
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
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.*;

/**
 * Created by robyn_000 on 04/01/2016.
 */
@Controller
@RequestMapping("/media")
public class MediaController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(MediaController.class);

    @Value("${media.audio.base-path}")
    private String audioBasePath;

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

            return new ResponseEntity<byte[]>(IOUtils.toByteArray(is), headers, HttpStatus.OK);

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

    //@RequestMapping("/getAudio")
    public ResponseEntity<byte[]> getAudio_(@RequestParam Long audioId, HttpServletResponse response) {

        ResponseEntity<byte[]> responseEntity = null;
        try {
            AudioEntity audioEntity = this.mediaDao.getAudioById(audioId);

            try(InputStream is = new FileInputStream(this.audioBasePath + audioEntity.getFileName())) {

                final HttpHeaders headers = new HttpHeaders();
                /*headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentLength(is.available());*/
                headers.add("Content-Disposition", String.format("attachment; filename=%s", audioEntity.getFileName()));
                headers.add("Content-Length", String.valueOf(is.available()));
                headers.add("Content-Type", "audio/mp3");

                responseEntity = new ResponseEntity<byte[]>(IOUtils.toByteArray(is), headers, HttpStatus.OK);
            }

            return responseEntity;

        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
        } finally {
            // Force garbage collector to free memory.
            System.gc();
        }

        return null;
    }

    @RequestMapping("/getAudio")
    public void getAudio(@RequestParam Long audioId, HttpServletResponse response) {

        try {
            AudioEntity audioEntity = this.mediaDao.getAudioById(audioId);

            try(InputStream is = new FileInputStream(this.audioBasePath + audioEntity.getFileName())) {


                response.setContentType("audio/mp3");
                response.setContentLength(is.available());
                response.addHeader("Content-Disposition", String.format("attachment; filename=%s", audioEntity.getFileName()));
                response.addHeader("Accept-Ranges", "bytes");

                int buffSize = 3072;

                byte[] buff = new byte[buffSize];

                int read = 0;
                int actuals = 0;
                while((actuals = is.read(buff, 0, buffSize)) > 0) {
                    IOUtils.copy(new ByteArrayInputStream(buff), response.getOutputStream());

                    read = actuals;

                    response.flushBuffer();

                    buff = new byte[buffSize];
                }
            }

        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
        } finally {
            // Force garbage collector to free memory.
            System.gc();
        }
    }


}

package com.robynem.mit.web.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.CharBuffer;

/**
 * Created by robyn_000 on 03/01/2016.
 */
public class ImageHelper {

    static final Logger LOG = LoggerFactory.getLogger(ImageHelper.class);

    private ImageHelper() {

    }

    public static InputStream scaleImage(InputStream stream, int width, int height, String imageFormat) throws Exception {

        BufferedImage originalImage = ImageIO.read(stream);

        int imageWidth  = originalImage.getWidth();
        int imageHeight = originalImage.getHeight();

        double scaleX = (double)width/imageWidth;
        double scaleY = (double)height/imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage resizedImage = bilinearScaleOp.filter(originalImage, new BufferedImage(width, height, originalImage.getType()));

        /*BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();*/

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageIO.write(resizedImage, imageFormat, baos);

        stream.reset();

        return new ByteArrayInputStream(baos.toByteArray());

    }



    public static void saveImage(InputStream stream, String destinationFileName) throws Exception {
        FileOutputStream fos = new FileOutputStream(destinationFileName);

        try {
            byte[] buff = new byte[stream.available()];
            stream.read(buff);


            fos.write(buff);
        } finally {
            fos.flush();
            fos.close();
        }
    }



    public static void deleteImage(String fileName) throws Exception {
        File image = new File(fileName);

        if (image.exists()) {
            image.delete();
        }
    }
}

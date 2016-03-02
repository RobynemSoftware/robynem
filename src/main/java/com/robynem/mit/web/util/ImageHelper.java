package com.robynem.mit.web.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
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

    public static void _scaleAndSaveImage(String originalFileName, String destinationFileName, int width, int height) throws Exception {


        StringBuffer cmd = new StringBuffer();
        cmd.append("convert -resize ")
                .append(String.format("%sx%s ", String.valueOf(width), String.valueOf(height)))
                .append(String.format("\"%s\" ", originalFileName))
                .append(String.format("\"%s\" ", destinationFileName));

        String finalCmd = cmd.toString().replace("/", "\\");
        String[] cmdArr = finalCmd.split(" ");

        Process p = Runtime.getRuntime().exec(cmdArr);
        int result = p.waitFor();

        InputStreamReader isr = new InputStreamReader(p.getInputStream());
        CharBuffer cb = CharBuffer.allocate(p.getInputStream().available());

        isr.read(cb);

        LOG.debug("Process result: {}", cb.toString());


        isr = new InputStreamReader(p.getErrorStream());
        cb = CharBuffer.allocate(p.getInputStream().available());

        isr.read(cb);

        LOG.debug("Process Error: {}", cb.toString());


    }

    public static void scaleAndSaveImage(String originalFileName, String destinationFileName, int width, int height, String imageFormat) throws Exception {

        BufferedImage originalImage = ImageIO.read(new File(originalFileName));

        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        ImageIO.write(resizedImage, imageFormat, new File(destinationFileName));
    }

    public static InputStream scaleImage(InputStream stream, int width, int height, String imageFormat) throws Exception {

        BufferedImage originalImage = ImageIO.read(stream);

        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

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

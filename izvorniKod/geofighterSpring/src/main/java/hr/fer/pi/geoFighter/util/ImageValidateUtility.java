package hr.fer.pi.geoFighter.util;

import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageValidateUtility {
    private static final int MAX_IMAGE_SIZE = 10000000;

    /**
     * Checks whether file is an image
     * @param fileURL HTTP URL of the file to be checked
     */
    public static void validateImage(URL fileURL) {

        HttpURLConnection httpConn = null;
        int responseCode = 0;
        try {
            httpConn = (HttpURLConnection) fileURL.openConnection();
            responseCode = httpConn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {

            String contentType = httpConn.getContentType();
            if(!contentType.startsWith("image"))
                throw new SpringGeoFighterException("URL is not of image");

            int contentLength = httpConn.getContentLength();
            if(contentLength > MAX_IMAGE_SIZE || contentLength == -1)
                throw new SpringGeoFighterException("Image size is larger than 10MB or unknown");


        } else {
            throw new SpringGeoFighterException("No file to check. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }
}

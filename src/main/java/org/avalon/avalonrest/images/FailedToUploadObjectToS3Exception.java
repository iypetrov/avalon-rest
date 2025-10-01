package org.avalon.avalonrest.images;

public class FailedToUploadObjectToS3Exception extends RuntimeException {
    public FailedToUploadObjectToS3Exception(String message) {
        super(message);
    }
}
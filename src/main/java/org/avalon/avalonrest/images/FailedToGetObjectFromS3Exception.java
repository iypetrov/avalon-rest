package org.avalon.avalonrest.images;

public class FailedToGetObjectFromS3Exception extends RuntimeException {
    public FailedToGetObjectFromS3Exception(String message) {
        super(message);
    }
}
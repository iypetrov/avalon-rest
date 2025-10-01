package org.avalon.avalonrest.exceptions;

public record ApiErrorResponse(Integer status, String message) {
}
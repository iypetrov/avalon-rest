package org.avalon.avalonrest.utils;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocaleHandler {
    public static Locale resolve(String localeString) {
        if (localeString == null || localeString.isEmpty()) {
            localeString = "bg";
        }
        return new Locale(localeString);
    }
}
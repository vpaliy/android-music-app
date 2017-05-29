package com.vpaliy.mediaplayer.media.utils;


import android.support.annotation.NonNull;


public class MediaHelper {

    private static final char CATEGORY_SEPARATOR = '/';
    private static final char LEAF_SEPARATOR = '|';

    public static String createMediaID(String musicID, String... categories) {
        StringBuilder sb = new StringBuilder();
        if (categories != null) {
            for (int i=0; i < categories.length; i++) {
                if (!isValidCategory(categories[i])) {
                    throw new IllegalArgumentException("Invalid category: " + categories[i]);
                }
                sb.append(categories[i]);
                if (i < categories.length - 1) {
                    sb.append(CATEGORY_SEPARATOR);
                }
            }
        }
        if (musicID != null) {
            sb.append(LEAF_SEPARATOR).append(musicID);
        }
        return sb.toString();
    }


    private static boolean isValidCategory(String category) {
        return category == null || (category.indexOf(CATEGORY_SEPARATOR) < 0 &&
                                    category.indexOf(LEAF_SEPARATOR) < 0);
    }

    public static String extractMusicId(@NonNull String mediaID) {
        int pos = mediaID.indexOf(LEAF_SEPARATOR);
        if (pos >= 0) {
            return mediaID.substring(pos+1);
        }
        return null;
    }


}

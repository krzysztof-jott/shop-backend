package com.ex.shop.admin.common.utils;

import com.github.slugify.Slugify;
import org.apache.commons.io.FilenameUtils;

public class SlugifyUtils {
    public static String slugifyFileName(String filename) { // metoda może być statyczna, bo nie będę w nią nic wstrzykiwał
        String name = FilenameUtils.getBaseName(filename);
        Slugify slg = new Slugify(); // wykonuję test czy ta metoda faktycznie działa (ctrl + shift + T)
        String changedName = slg
                .withCustomReplacement("_", "-")
                .slugify(name);
        return changedName + "." + FilenameUtils.getExtension(filename);
    }

    public static String slugifySlug(String slug) {
        Slugify slugify = new Slugify();
        return slugify.withCustomReplacement("_", "-")
                .slugify(slug);
    }
}
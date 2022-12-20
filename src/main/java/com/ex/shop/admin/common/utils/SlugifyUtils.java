package com.ex.shop.admin.common.utils;

import com.github.slugify.Slugify;
import org.apache.commons.io.FilenameUtils;

// 14.0UP tworzę klasę narzędziową:
public class SlugifyUtils {
    public static String slugifyFileName(String filename) { // metoda może być statyczna, bo nie będę w nią nic wstrzykiwał
        // 14.1UP rozbijam nazwę na 2 części, pobieram część bazową:
        String name = FilenameUtils.getBaseName(filename); // bazowa część nazwy pliku
        // 14.2UP używam już biblioteki Slugify:
        Slugify slg = new Slugify(); // 14.4UP wykonuję test czy ta metoda faktycznie działa (ctrl + shift + T)
        // 14.7 dodaję metodę withCustom...
        String changedName = slg
                .withCustomReplacement("_", "-")
                .slugify(name);

        // 14.3UP dodaję jeszcze rozszerzenie po changedName:
        return changedName + "." + FilenameUtils.getExtension(filename);
    }

    public static String slugifySlug(String slug) {
        Slugify slugify = new Slugify();// korzystam z tej biblioteki Slugify
        return slugify.withCustomReplacement("_", "-")
                .slugify(slug); // teraz wszystko powinno się już zapisywać na backendzie
    }
}
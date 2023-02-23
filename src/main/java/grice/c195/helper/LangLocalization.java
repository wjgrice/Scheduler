package grice.c195.helper;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The LangLocalization class is responsible for setting the language localization of the program.
 * It provides a static method that can be called from other classes to set the localization.
 * The class uses a ResourceBundle to store the language localization data, which can be retrieved with getLangBundle().
 * By default, the locale is set to the system default locale. This can be changed manually by updating the locale variable.
 */
public class LangLocalization {
    // set the Locale to the default value of the system. You can change this to another Locale value, such as Locale.FRENCH, to test French localization
    private static final Locale locale = Locale.getDefault();
    // create a ResourceBundle using the "langBundle" resource bundle and the previously selected Locale
    private static final ResourceBundle langBundle  = ResourceBundle.getBundle("langBundle", locale);

    /**
     * The setLangBundle method is used to set the language localization of the program.
     * It takes a Locale as a parameter and sets the ResourceBundle to use that Locale.
     * @return LangLocalization.langBundle
     */
    public static ResourceBundle getLangBundle() {
        return langBundle;
    }
}

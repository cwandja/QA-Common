package de.funke.qa.common.utilities;

import de.funke.qa.common.enumeration.Publication;
import de.funke.qa.common.enumeration.Stage;

public class Helper {
    public static final String HTTP_PROTOCOL = "http://";
    public static final String HTTPS_PROTOCOL = "https://";
    public static final String ALLE_PUBLICATIONS = "alle";
    public static final String NONE_PUBLICATION = "none";
    public static final String PUBLICATION = "publication";
    public static final String STAGE = "stage";
    public static final String SPLITTING_ENV_VARIABLE_VALUE = ",";



    public static String getBaseUrl(Publication publication) {
        String stage = System.getProperty(STAGE, Stage.PROD.toString()).toLowerCase();
        String baseUrl=null;
        String prefix = publication.isHttpsForced() ? HTTPS_PROTOCOL : HTTP_PROTOCOL;
        String suffix = ".de";
        String domain = publication.getDomain();
        switch (Stage.valueOf(stage.toUpperCase())) {
            case UAT:
                baseUrl = prefix+"uat."+domain+suffix;
                break;
            case PROD:
            default:
                baseUrl = prefix+"www."+domain+suffix;
                break;
        }
        if (baseUrl == null) {
            throw new IllegalStateException("no mapping for publication=" + publication + ", stage=" + stage);
        }
        return baseUrl;
    }

}

package de.funke.qa.common.utilities;

import de.funke.qa.common.data.Article;
import de.funke.qa.common.enumeration.ArticleEnum;
import de.funke.qa.common.enumeration.Publication;
import de.funke.qa.common.enumeration.Stage;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.ListIterator;

public class Helper {
    static final Logger logger = Logger.getLogger(Helper.class);
    public static final String HTTP_PROTOCOL = "http://";
    public static final String HTTPS_PROTOCOL = "https://";
    public static final String ALLE_PUBLICATIONS = "alle";
    public static final String NONE_PUBLICATION = "none";
    public static final String PUBLICATION = "publication";
    public static final String STAGE = "stage";
    public static final String SPLITTING_ENV_VARIABLE_VALUE = ",";
    public static final String SUCCESS_MESSAGE = "AMP validation successful";
    public static final String INTEGRATIONTEST_RUBRIK="/test/integrationstest/artikel-regressionstest/";
    public static final String DEFAULT_BASE_URL_PLACEHOLDER = "stage.";
    public static final String DEFAULT_BASE_URL="http://"+DEFAULT_BASE_URL_PLACEHOLDER+"multi.funkedigital.de";


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

    public static String getDefaultBaseUrl(Stage stage) {
        String baseUrl = DEFAULT_BASE_URL;
        switch (stage){
            case UAT:
            return baseUrl.replace(DEFAULT_BASE_URL_PLACEHOLDER, Stage.UAT.toString().toLowerCase()+".");
            default:
                return baseUrl.replace(DEFAULT_BASE_URL_PLACEHOLDER, "");
        }
    }

    public static Article getArticle(List<Article> articles, ArticleEnum Type){
        Article articleFound = new Article();
        for (ListIterator li = articles.listIterator(0); li.hasNext(); ) {
            Article article = (Article) li.next();
            if(article.getTitle().toUpperCase().contains(Type.toString())){
                articleFound = article;
                articleFound.setType(Type);
                break;
            }
        }
        return articleFound;
    }

}

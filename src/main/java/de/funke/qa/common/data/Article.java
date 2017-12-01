package de.funke.qa.common.data;

import de.funke.qa.common.enumeration.Publication;
import de.funke.qa.common.utilities.Helper;

public class Article {
    private String id;
    private String title;
    private String url;

    public static String NACHRICHT_ID="212686771";
    public Article(String id, String baseUrl){
        this.url = baseUrl + Helper.INTEGRATIONTEST_RUBRIK + id;
    }
    public Article(String id, String title, String url){
        this.id = id;
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

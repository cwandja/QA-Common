package de.funke.qa.common.data;

import de.funke.qa.common.enumeration.ArticleEnum;
import de.funke.qa.common.enumeration.Publication;
import de.funke.qa.common.utilities.Helper;

public class Article {
    private String id;
    private String title;
    private String url;
    private ArticleEnum type;

    public Article(){

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

    public ArticleEnum getType() {
        return type;
    }

    public void setType(ArticleEnum type) {
        this.type = type;
    }
}

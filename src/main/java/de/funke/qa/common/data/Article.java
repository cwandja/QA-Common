package de.funke.qa.common.data;

import de.funke.qa.common.enumeration.ArticleEnum;
import de.funke.qa.common.enumeration.Publication;
import de.funke.qa.common.utilities.Helper;
import org.apache.commons.lang3.StringUtils;

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
        if(StringUtils.isEmpty(id)){
           if(StringUtils.isNotEmpty(url)){
            String [] paths = url.split("/");
            String result = paths[paths.length - 2];
            id = result.replace("article", "");
           }
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return getUrl("");
    }

    public String getUrl(String baseUrl) {
        if(StringUtils.isNotEmpty(baseUrl)) {
            if (StringUtils.isNotEmpty(id)) {
                url = baseUrl + "/" + id;
            }
        }
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

package ru.names.ym_gaTool.api.yandex.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Representation of yandex api table method response
 *
 * @author kbogdanov 16.03.16
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dimension {

    private String name;

    private String url;

    private String favicon;

    public Dimension() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }
}

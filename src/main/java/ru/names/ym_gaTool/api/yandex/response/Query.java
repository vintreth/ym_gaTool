package ru.names.ym_gaTool.api.yandex.response;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Representation of yandex api table method response
 *
 * @author kbogdanov 16.03.16
 */
public class Query {

    private int[] ids;
    private String[] dimensions;
    private String[] metrics;
    private String[] sort;
    private String date1;
    private String date2;
    private String filters;
    private int limit;
    private int offset;
    @JsonProperty("auto_group_size")
    private String autoGroupSize;
    private String quantile;
    private String attribution;
    private String currency;
    private String group;

    public Query() {
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    public String[] getDimensions() {
        return dimensions;
    }

    public void setDimensions(String[] dimensions) {
        this.dimensions = dimensions;
    }

    public String[] getMetrics() {
        return metrics;
    }

    public void setMetrics(String[] metrics) {
        this.metrics = metrics;
    }

    public String[] getSort() {
        return sort;
    }

    public void setSort(String[] sort) {
        this.sort = sort;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getAutoGroupSize() {
        return autoGroupSize;
    }

    public void setAutoGroupSize(String autoGroupSize) {
        this.autoGroupSize = autoGroupSize;
    }

    public String getQuantile() {
        return quantile;
    }

    public void setQuantile(String quantile) {
        this.quantile = quantile;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}

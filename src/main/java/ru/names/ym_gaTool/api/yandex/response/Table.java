package ru.names.ym_gaTool.api.yandex.response;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Representation of yandex api table method response
 *
 * @author kbogdanov 16.03.16
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Table {

    private Query query;
    private Data[] data;
    @JsonProperty("total_rows")
    private int totalRows;
    private boolean sampled;
    @JsonProperty("sample_share")
    private float sampleShare;
    @JsonProperty("sample_size")
    private int sampleSize;
    @JsonProperty("sample_space")
    private int sampleSpace;
    @JsonProperty("data_lag")
    private int dataLag;
    private float[] totals;
    @JsonIgnore
    private float[] min;
    @JsonIgnore
    private float[] max;

    public Table() {
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public boolean isSampled() {
        return sampled;
    }

    public void setSampled(boolean sampled) {
        this.sampled = sampled;
    }

    public float getSampleShare() {
        return sampleShare;
    }

    public void setSampleShare(float sampleShare) {
        this.sampleShare = sampleShare;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    public int getSampleSpace() {
        return sampleSpace;
    }

    public void setSampleSpace(int sampleSpace) {
        this.sampleSpace = sampleSpace;
    }

    public int getDataLag() {
        return dataLag;
    }

    public void setDataLag(int dataLag) {
        this.dataLag = dataLag;
    }

    public float[] getTotals() {
        return totals;
    }

    public void setTotals(float[] totals) {
        this.totals = totals;
    }

    public float[] getMin() {
        return min;
    }

    public void setMin(float[] min) {
        this.min = min;
    }

    public float[] getMax() {
        return max;
    }

    public void setMax(float[] max) {
        this.max = max;
    }
}

package ru.names.ym_gaTool.api.response.yandex;

/**
 * @author kbogdanov 16.03.16
 */
public class Data {

    private Dimension[] dimensions;
    private float[] metrics;

    public Data() {}

    public Dimension[] getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimension[] dimensions) {
        this.dimensions = dimensions;
    }

    public float[] getMetrics() {
        return metrics;
    }

    public void setMetrics(float[] metrics) {
        this.metrics = metrics;
    }
}

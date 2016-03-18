package ru.names.ym_gaTool.api.yandex.response;

/**
 * Representation of yandex api table method response
 *
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

    public String getKeyWord() {
        if (null != dimensions && 0 < dimensions.length) {
            return dimensions[0].getName();
        }

        return null;
    }

    public String getClientId() {
        if (null != dimensions && 1 < dimensions.length) {
            return dimensions[1].getName();
        }

        return null;
    }
}

package com.highway2urhell.web.rest.vm;

import com.highway2urhell.service.dto.EntryPointByApplication;

import java.util.List;

/**
 * View Model object for storing a Home stats on applications and entrypoints.
 */
public class EntryPointByApplicationVM {

    private Long[] data;

    private String[] labels;

    private String[] series;

    public EntryPointByApplicationVM(List<EntryPointByApplication> entryPointByApplicationList) {
        data = new Long[entryPointByApplicationList.size()];
        labels = new String[entryPointByApplicationList.size()];
        series = new String[entryPointByApplicationList.size()];
        for (int i = 0; i < entryPointByApplicationList.size(); i++) {
            labels[i] = entryPointByApplicationList.get(i).getAnalyse();
            series[i] = entryPointByApplicationList.get(i).getApplication();
            data[i] = entryPointByApplicationList.get(i).getData();
        }
    }

    public Long[] getData() {
        return data;
    }

    public void setData(Long[] data) {
        this.data = data;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public String[] getSeries() {
        return series;
    }

    public void setSeries(String[] series) {
        this.series = series;
    }

    @Override
    public String toString() {
        return "EntryPointByApplicationVM{" +
            "data=" + data +
            ", labels=" + labels +
            ", series=" + series +
            '}';
    }
}

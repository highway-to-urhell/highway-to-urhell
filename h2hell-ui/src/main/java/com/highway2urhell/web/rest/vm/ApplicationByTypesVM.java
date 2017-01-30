package com.highway2urhell.web.rest.vm;

import com.highway2urhell.service.dto.ApplicationByType;

import java.util.List;

/**
 * View Model object for storing a Home stats on applications and entrypoints.
 */
public class ApplicationByTypesVM {

    private Long[] data;

    private String[] labels;

    public ApplicationByTypesVM() {}

    public ApplicationByTypesVM(List<ApplicationByType> applicationByTypeList) {
        data = new Long[applicationByTypeList.size()];
        labels = new String[applicationByTypeList.size()];
        for (int i = 0; i < applicationByTypeList.size(); i++) {
            labels[i] = applicationByTypeList.get(i).getLabel();
            data[i] = applicationByTypeList.get(i).getData();
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

    @Override
    public String toString() {
        return "ApplicationByTypesVM{" +
            "data=" + data +
            ", labels=" + labels +
            '}';
    }
}

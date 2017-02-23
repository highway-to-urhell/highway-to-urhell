package com.highway2urhell.web.rest.vm;

import com.highway2urhell.domain.EntryPoint;

import java.util.List;

/**
 * View Model object for storing a Home stats on applications and entrypoints.
 */
public class VizualisationPathVM {

    private List<EntryPoint> entrypoints;

    public List<EntryPoint> getEntrypoints() {
        return entrypoints;
    }

    public void setEntrypoints(List<EntryPoint> entrypoints) {
        this.entrypoints = entrypoints;
    }

    @Override
    public String toString() {
        return "HomeVM{" +
            "entrypoints=" + entrypoints +
            '}';
    }
}

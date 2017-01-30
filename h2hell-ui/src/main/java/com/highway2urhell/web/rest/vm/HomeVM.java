package com.highway2urhell.web.rest.vm;

/**
 * View Model object for storing a Home stats on applications and entrypoints.
 */
public class HomeVM {

    /*
     vm.applications = { count: 10 };
        vm.entrypoints = { count: 50 };

        vm.stats = { data: [[65, 59, 80, 81, 56, 55, 40],
            [28, 48, 40, 19, 86, 27, 90]], labels: ['2006', '2007', '2008', '2009', '2010', '2011', '2012'], series: ['Series A', 'Series B'] };
        vm.types = { data: [300, 500, 100], labels: ["Download Sales", "In-Store Sales", "Mail-Order Sales"] };
    */


    private Long applicationsCount;
    private Long entrypointsCount;

    private EntryPointByApplicationVM stats;

    private ApplicationByTypesVM types;

    public Long getApplicationsCount() {
        return applicationsCount;
    }

    public void setApplicationsCount(Long applicationsCount) {
        this.applicationsCount = applicationsCount;
    }

    public Long getEntrypointsCount() {
        return entrypointsCount;
    }

    public void setEntrypointsCount(Long entrypointsCount) {
        this.entrypointsCount = entrypointsCount;
    }

    public EntryPointByApplicationVM getStats() {
        return stats;
    }

    public void setStats(EntryPointByApplicationVM stats) {
        this.stats = stats;
    }

    public ApplicationByTypesVM getTypes() {
        return types;
    }

    public void setTypes(ApplicationByTypesVM types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "HomeVM{" +
            "applicationsCount=" + applicationsCount +
            "entrypointsCount=" + entrypointsCount +
            ", stats=" + stats +
            ", types=" + types +
            '}';
    }
}

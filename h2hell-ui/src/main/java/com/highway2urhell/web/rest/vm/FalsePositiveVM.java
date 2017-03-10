package com.highway2urhell.web.rest.vm;

/**
 * Created by guillaumedufour on 10/03/2017.
 */
public class FalsePositiveVM {
    private Long id;
    private String status;

    public FalsePositiveVM(){

    }

    public FalsePositiveVM(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.highway2urhell.service;

import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.domain.FrameworkInformations;

import java.util.List;

public interface LeechService {
    void receiveData(List<EntryPathData> incoming);

    FrameworkInformations getFrameworkInformations();

    boolean isTriggeredAtStartup();

}

package com.highway2urhell.service.mapper;

import com.highway2urhell.domain.enumeration.TypeMessageEvent;

/**
 * Created by guillaumedufour on 12/02/2017.
 */
public class TypeMessageEventMapper {
    public static TypeMessageEvent domainToDto(com.highway2urhell.web.rest.dto.v1api.TypeMessageEvent dto) {
        switch (dto) {
            case ENABLE_ENTRY_POINT:
                return TypeMessageEvent.ENABLE_ENTRY_POINT;
            case INIT_PATH:
                return TypeMessageEvent.INIT_PATH;
            case SOURCE:
                return TypeMessageEvent.SOURCE;
            default:
                return TypeMessageEvent.ENABLE_ENTRY_POINT;
        }
    }

    public static com.highway2urhell.web.rest.dto.v1api.TypeMessageEvent dtoToDomain(TypeMessageEvent domain) {
        switch (domain) {
            case ENABLE_ENTRY_POINT:
                return com.highway2urhell.web.rest.dto.v1api.TypeMessageEvent.ENABLE_ENTRY_POINT;
            case INIT_PATH:
                return com.highway2urhell.web.rest.dto.v1api.TypeMessageEvent.INIT_PATH;
            case SOURCE:
                return com.highway2urhell.web.rest.dto.v1api.TypeMessageEvent.SOURCE;
            default:
                return com.highway2urhell.web.rest.dto.v1api.TypeMessageEvent.ENABLE_ENTRY_POINT;
        }
    }
}

package com.panini.album.catalog.dto;

import com.panini.album.catalog.Country;

public record CountryDto(
        Integer id,
        String code,
        String name,
        String iso2,
        String wcGroup,
        Integer displayOrder
) {
    public static CountryDto from(Country c) {
        return new CountryDto(c.getId(), c.getCode(), c.getName(), c.getIso2(), c.getWcGroup(), c.getDisplayOrder());
    }
}

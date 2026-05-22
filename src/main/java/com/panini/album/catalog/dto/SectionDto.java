package com.panini.album.catalog.dto;

import com.panini.album.catalog.Section;

public record SectionDto(
        Integer id,
        String code,
        String name,
        Integer displayOrder
) {
    public static SectionDto from(Section s) {
        return new SectionDto(s.getId(), s.getCode(), s.getName(), s.getDisplayOrder());
    }
}

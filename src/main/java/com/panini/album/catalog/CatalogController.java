package com.panini.album.catalog;

import com.panini.album.catalog.dto.CountryDto;
import com.panini.album.catalog.dto.SectionDto;
import com.panini.album.catalog.dto.StickerDto;
import com.panini.album.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CountryRepository countryRepository;
    private final SectionRepository sectionRepository;
    private final StickerRepository stickerRepository;

    @GetMapping("/sections")
    public List<SectionDto> sections() {
        return sectionRepository.findAll().stream()
                .sorted((a, b) -> a.getDisplayOrder().compareTo(b.getDisplayOrder()))
                .map(SectionDto::from)
                .toList();
    }

    @GetMapping("/countries")
    public List<CountryDto> countries() {
        return countryRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(CountryDto::from)
                .toList();
    }

    @GetMapping("/stickers")
    public List<StickerDto> allStickers() {
        return stickerRepository.findAllWithRelations().stream()
                .map(StickerDto::from)
                .toList();
    }

    @GetMapping("/countries/{code}/stickers")
    public List<StickerDto> stickersByCountry(@PathVariable String code) {
        countryRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new NotFoundException("País no encontrado: " + code));
        return stickerRepository.findByCountryCode(code.toUpperCase()).stream()
                .map(StickerDto::from)
                .toList();
    }

    @GetMapping("/sections/{code}/stickers")
    public List<StickerDto> stickersBySection(@PathVariable String code) {
        sectionRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new NotFoundException("Sección no encontrada: " + code));
        return stickerRepository.findBySectionCodeAndNoCountry(code.toUpperCase()).stream()
                .map(StickerDto::from)
                .toList();
    }
}

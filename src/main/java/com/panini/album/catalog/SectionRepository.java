package com.panini.album.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Integer> {
    Optional<Section> findByCode(String code);
}

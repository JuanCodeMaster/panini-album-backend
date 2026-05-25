package com.panini.album.catalog;

import com.panini.album.album.UserStickerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CatalogSeeder implements CommandLineRunner {

    private final SectionRepository sectionRepository;
    private final CountryRepository countryRepository;
    private final StickerRepository stickerRepository;
    private final UserStickerRepository userStickerRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (stickerRepository.count() == 0) {
            log.info("Sembrando catálogo Panini Mundial 2026…");
            seedInitial();
            long total = stickerRepository.count();
            log.info("Catálogo sembrado: {} stickers ({} países).", total, countryRepository.count());
        } else {
            log.info("Catálogo presente ({} stickers). Aplicando migración de nombres/imágenes…", stickerRepository.count());
            migrateExisting();
            log.info("Migración completada.");
        }
    }

    // ---------- Seed inicial ----------

    private void seedInitial() {
        Section intro = sectionRepository.save(Section.builder()
                .code("INTRO").name("Introducción").displayOrder(1).build());
        Section museum = sectionRepository.save(Section.builder()
                .code("MUSEUM").name("FIFA Museum").displayOrder(2).build());
        Section team = sectionRepository.save(Section.builder()
                .code("TEAM").name("Selecciones").displayOrder(3).build());
        Section cocaCola = sectionRepository.save(Section.builder()
                .code("COCACOLA").name("Coca-Cola Exclusivos").displayOrder(4).build());

        seedIntro(intro);
        seedMuseum(museum);
        seedCountries();
        Map<String, String[]> roster = PlayerData.teams();
        for (Country country : countryRepository.findAllByOrderByDisplayOrderAsc()) {
            seedCountryStickers(team, country, roster.get(country.getCode()));
        }
        seedCocaCola(cocaCola);
    }

    private void seedCocaCola(Section cc) {
        List<Sticker> stickers = new ArrayList<>();
        for (String[] row : cocaColaData()) {
            Country country = countryRepository.findByCode(row[2]).orElse(null);
            stickers.add(Sticker.builder()
                    .section(cc)
                    .country(country)
                    .code(row[0])
                    .displayName(row[1])
                    .stickerType(StickerType.SPECIAL)
                    .foil(true)
                    .inPacks(false) // No vienen en sobres estándar
                    .imageUrl(playerAvatarUrl(row[1]))
                    .build());
        }
        stickerRepository.saveAll(stickers);
    }

    // Formato: code, displayName, countryCode (para fondo bandera + agrupación).
    private String[][] cocaColaData() {
        return new String[][]{
                {"CC1",  "Lamine Yamal",        "ESP"},
                {"CC2",  "Joshua Kimmich",      "GER"},
                {"CC3",  "Harry Kane",          "ENG"},
                {"CC4",  "Santiago Giménez",    "MEX"},
                {"CC5",  "Joško Gvardiol",      "CRO"},
                {"CC6",  "Federico Valverde",   "URU"},
                {"CC7",  "Jefferson Lerma",     "COL"},
                {"CC8",  "Enner Valencia",      "ECU"},
                {"CC9",  "Gabriel Magalhães",   "BRA"},
                {"CC10", "Virgil van Dijk",     "NED"},
                {"CC11", "Alphonso Davies",     "CAN"},
                {"CC12", "Emiliano Martínez",   "ARG"},
                {"CC13", "Raúl Jiménez",        "MEX"},
                {"CC14", "Lautaro Martínez",    "ARG"},
        };
    }

    private String playerAvatarUrl(String name) {
        String encoded = java.net.URLEncoder.encode(name, java.nio.charset.StandardCharsets.UTF_8);
        return "https://ui-avatars.com/api/?name=" + encoded
                + "&background=D4AF37&color=3A2A05&size=200&bold=true&font-size=0.4";
    }

    private void seedIntro(Section intro) {
        List<Sticker> stickers = new ArrayList<>();
        stickers.add(buildSpecial(intro, "00", "Panini Logo", StickerType.LOGO_PANINI, true));
        stickers.add(buildSpecial(intro, "FWC1", "FIFA World Cup 2026 - Trophy", StickerType.HOST, true));
        stickers.add(buildSpecial(intro, "FWC2", "Host Country - USA", StickerType.HOST, true));
        stickers.add(buildSpecial(intro, "FWC3", "Host Country - Mexico", StickerType.HOST, true));
        stickers.add(buildSpecial(intro, "FWC4", "Host Country - Canada", StickerType.HOST, true));
        stickers.add(buildSpecial(intro, "FWC5", "Host Cities - USA East", StickerType.HOST, false));
        stickers.add(buildSpecial(intro, "FWC6", "Host Cities - USA West", StickerType.HOST, false));
        stickers.add(buildSpecial(intro, "FWC7", "Host Cities - Mexico", StickerType.HOST, false));
        stickers.add(buildSpecial(intro, "FWC8", "Host Cities - Canada", StickerType.HOST, false));
        stickerRepository.saveAll(stickers);
    }

    private void seedMuseum(Section museum) {
        String[][] museumStickers = {
                {"FWC9", "Italy 1934 - FIFA Museum"},
                {"FWC10", "Brazil 1958 - FIFA Museum"},
                {"FWC11", "England 1966 - FIFA Museum"},
                {"FWC12", "Mexico 1970 - FIFA Museum"},
                {"FWC13", "Argentina 1978 - FIFA Museum"},
                {"FWC14", "Italy 1990 - FIFA Museum"},
                {"FWC15", "France 1998 - FIFA Museum"},
                {"FWC16", "Germany 2006 - FIFA Museum"},
                {"FWC17", "South Africa 2010 - FIFA Museum"},
                {"FWC18", "Brazil 2014 - FIFA Museum"},
                {"FWC19", "Argentina 2022 - FIFA Museum"},
        };
        List<Sticker> stickers = new ArrayList<>();
        for (String[] m : museumStickers) {
            stickers.add(buildSpecial(museum, m[0], m[1], StickerType.MUSEUM, true));
        }
        stickerRepository.saveAll(stickers);
    }

    private void seedCountries() {
        // Datos por código (luego asigno displayOrder según orden del álbum por grupos A→L)
        Map<String, String[]> meta = new java.util.LinkedHashMap<>();
        meta.put("ALG", new String[]{"Argelia", "DZ"});
        meta.put("ARG", new String[]{"Argentina", "AR"});
        meta.put("AUS", new String[]{"Australia", "AU"});
        meta.put("AUT", new String[]{"Austria", "AT"});
        meta.put("BEL", new String[]{"Bélgica", "BE"});
        meta.put("BIH", new String[]{"Bosnia y Herzegovina", "BA"});
        meta.put("BRA", new String[]{"Brasil", "BR"});
        meta.put("CAN", new String[]{"Canadá", "CA"});
        meta.put("CIV", new String[]{"Costa de Marfil", "CI"});
        meta.put("COD", new String[]{"Congo RD", "CD"});
        meta.put("COL", new String[]{"Colombia", "CO"});
        meta.put("CPV", new String[]{"Cabo Verde", "CV"});
        meta.put("CRO", new String[]{"Croacia", "HR"});
        meta.put("CUW", new String[]{"Curazao", "CW"});
        meta.put("CZE", new String[]{"Chequia", "CZ"});
        meta.put("ECU", new String[]{"Ecuador", "EC"});
        meta.put("EGY", new String[]{"Egipto", "EG"});
        meta.put("ENG", new String[]{"Inglaterra", "GB-ENG"});
        meta.put("ESP", new String[]{"España", "ES"});
        meta.put("FRA", new String[]{"Francia", "FR"});
        meta.put("GER", new String[]{"Alemania", "DE"});
        meta.put("GHA", new String[]{"Ghana", "GH"});
        meta.put("HAI", new String[]{"Haití", "HT"});
        meta.put("IRN", new String[]{"Irán", "IR"});
        meta.put("IRQ", new String[]{"Irak", "IQ"});
        meta.put("JOR", new String[]{"Jordania", "JO"});
        meta.put("JPN", new String[]{"Japón", "JP"});
        meta.put("KOR", new String[]{"Corea del Sur", "KR"});
        meta.put("KSA", new String[]{"Arabia Saudita", "SA"});
        meta.put("MAR", new String[]{"Marruecos", "MA"});
        meta.put("MEX", new String[]{"México", "MX"});
        meta.put("NED", new String[]{"Países Bajos", "NL"});
        meta.put("NOR", new String[]{"Noruega", "NO"});
        meta.put("NZL", new String[]{"Nueva Zelanda", "NZ"});
        meta.put("PAN", new String[]{"Panamá", "PA"});
        meta.put("PAR", new String[]{"Paraguay", "PY"});
        meta.put("POR", new String[]{"Portugal", "PT"});
        meta.put("QAT", new String[]{"Qatar", "QA"});
        meta.put("RSA", new String[]{"Sudáfrica", "ZA"});
        meta.put("SCO", new String[]{"Escocia", "GB-SCT"});
        meta.put("SEN", new String[]{"Senegal", "SN"});
        meta.put("SUI", new String[]{"Suiza", "CH"});
        meta.put("SWE", new String[]{"Suecia", "SE"});
        meta.put("TUN", new String[]{"Túnez", "TN"});
        meta.put("TUR", new String[]{"Turquía", "TR"});
        meta.put("URU", new String[]{"Uruguay", "UY"});
        meta.put("USA", new String[]{"Estados Unidos", "US"});
        meta.put("UZB", new String[]{"Uzbekistán", "UZ"});

        Map<String, String> groups = wcGroupMap();
        Map<String, Integer> orderMap = albumOrderMap();
        for (Map.Entry<String, String[]> e : meta.entrySet()) {
            String code = e.getKey();
            countryRepository.save(Country.builder()
                    .code(code)
                    .name(e.getValue()[0])
                    .iso2(e.getValue()[1])
                    .wcGroup(groups.get(code))
                    .displayOrder(orderMap.getOrDefault(code, 99))
                    .build());
        }
    }

    /**
     * Orden de aparición en el álbum Panini WC 2026.
     * Sigue los grupos A→L del sorteo oficial; dentro de cada grupo, el anfitrión/cabeza
     * de serie va primero (posición 1) y los demás en orden de bombo descendente.
     */
    private static Map<String, Integer> albumOrderMap() {
        String[] orderedCodes = {
                // A
                "MEX", "RSA", "KOR", "CZE",
                // B
                "CAN", "BIH", "QAT", "SUI",
                // C
                "BRA", "MAR", "HAI", "SCO",
                // D
                "USA", "PAR", "AUS", "TUR",
                // E
                "GER", "CUW", "CIV", "ECU",
                // F
                "NED", "JPN", "SWE", "TUN",
                // G
                "BEL", "EGY", "IRN", "NZL",
                // H
                "ESP", "CPV", "KSA", "URU",
                // I
                "FRA", "SEN", "IRQ", "NOR",
                // J
                "ARG", "ALG", "AUT", "JOR",
                // K
                "POR", "COD", "UZB", "COL",
                // L
                "ENG", "CRO", "GHA", "PAN",
        };
        Map<String, Integer> m = new java.util.HashMap<>();
        for (int i = 0; i < orderedCodes.length; i++) m.put(orderedCodes[i], i + 1);
        return m;
    }

    /**
     * Grupos oficiales del sorteo final del Mundial 2026 (Washington D.C., 5/dic/2025).
     * 12 grupos × 4 selecciones = 48.
     */
    private static Map<String, String> wcGroupMap() {
        Map<String, String> m = new java.util.HashMap<>();
        // Group A
        m.put("MEX", "A"); m.put("KOR", "A"); m.put("RSA", "A"); m.put("CZE", "A");
        // Group B
        m.put("CAN", "B"); m.put("SUI", "B"); m.put("QAT", "B"); m.put("BIH", "B");
        // Group C
        m.put("BRA", "C"); m.put("MAR", "C"); m.put("SCO", "C"); m.put("HAI", "C");
        // Group D
        m.put("USA", "D"); m.put("PAR", "D"); m.put("AUS", "D"); m.put("TUR", "D");
        // Group E
        m.put("GER", "E"); m.put("ECU", "E"); m.put("CIV", "E"); m.put("CUW", "E");
        // Group F
        m.put("NED", "F"); m.put("JPN", "F"); m.put("TUN", "F"); m.put("SWE", "F");
        // Group G
        m.put("BEL", "G"); m.put("IRN", "G"); m.put("EGY", "G"); m.put("NZL", "G");
        // Group H
        m.put("ESP", "H"); m.put("URU", "H"); m.put("KSA", "H"); m.put("CPV", "H");
        // Group I
        m.put("FRA", "I"); m.put("SEN", "I"); m.put("NOR", "I"); m.put("IRQ", "I");
        // Group J
        m.put("ARG", "J"); m.put("AUT", "J"); m.put("ALG", "J"); m.put("JOR", "J");
        // Group K
        m.put("POR", "K"); m.put("COL", "K"); m.put("UZB", "K"); m.put("COD", "K");
        // Group L
        m.put("ENG", "L"); m.put("CRO", "L"); m.put("PAN", "L"); m.put("GHA", "L");
        return m;
    }

    private void seedCountryStickers(Section section, Country country, String[] roster) {
        List<Sticker> stickers = new ArrayList<>(20);
        for (int pos = 1; pos <= 20; pos++) {
            stickers.add(buildCountrySticker(section, country, pos, roster));
        }
        stickerRepository.saveAll(stickers);
    }

    private Sticker buildCountrySticker(Section section, Country country, int pos, String[] roster) {
        String code = country.getCode() + pos;
        StickerType type;
        String name;
        boolean foil;

        if (pos == 1) {
            type = StickerType.BADGE;
            name = "Escudo " + country.getName();
            foil = true;
        } else if (pos == 13) {
            type = StickerType.TEAM_PHOTO;
            name = "Foto del equipo " + country.getName();
            foil = false;
        } else {
            type = StickerType.PLAYER;
            name = playerName(country, pos, roster);
            foil = false;
        }

        return Sticker.builder()
                .section(section)
                .country(country)
                .code(code)
                .numberInCountry(pos)
                .displayName(name)
                .stickerType(type)
                .foil(foil)
                .inPacks(true)
                .imageUrl(imageUrlFor(country, pos, type, name))
                .build();
    }

    // ---------- Migración (datos ya seedados) ----------

    private void migrateExisting() {
        Map<String, String[]> roster = PlayerData.teams();
        Map<String, String> groups = wcGroupMap();
        int updated = 0;
        List<Country> countries = countryRepository.findAllByOrderByDisplayOrderAsc();
        log.info("Migrando {} países…", countries.size());

        // Backfill wcGroup
        int groupsUpdated = 0;
        for (Country c : countries) {
            String g = groups.get(c.getCode());
            if (g != null && !g.equals(c.getWcGroup())) {
                c.setWcGroup(g);
                countryRepository.save(c);
                groupsUpdated++;
            }
        }
        if (groupsUpdated > 0) {
            log.info("Grupos del Mundial asignados: {}", groupsUpdated);
        }

        // Backfill displayOrder al orden real del álbum (grupos A→L)
        Map<String, Integer> orderMap = albumOrderMap();
        int orderUpdated = 0;
        for (Country c : countries) {
            Integer newOrder = orderMap.get(c.getCode());
            if (newOrder != null && (c.getDisplayOrder() == null || !newOrder.equals(c.getDisplayOrder()))) {
                c.setDisplayOrder(newOrder);
                countryRepository.save(c);
                orderUpdated++;
            }
        }
        if (orderUpdated > 0) {
            log.info("Orden del álbum actualizado: {} países", orderUpdated);
        }

        // Limpiar stickers huérfanos del TEAM (sin pos válido) y sus user_stickers asociados.
        // Estos eran restos de seeds anteriores que aparecían "sin número" al lado del escudo.
        int orphanCount = 0;
        for (Country c : countries) {
            List<Sticker> orphans = stickerRepository.findOrphanCountryStickers(c.getCode());
            if (orphans.isEmpty()) continue;
            List<Long> ids = orphans.stream().map(Sticker::getId).toList();
            userStickerRepository.deleteByStickerIdIn(ids);
            stickerRepository.deleteAll(orphans);
            stickerRepository.flush();
            orphanCount += orphans.size();
            log.info("  {} → borrados {} stickers huérfanos", c.getCode(), orphans.size());
        }
        if (orphanCount > 0) {
            log.info("Total huérfanos eliminados: {}", orphanCount);
        }

        // Crear sección Coca-Cola si no existe (con sus 14 cromos)
        Section cocaCola = sectionRepository.findByCode("COCACOLA").orElse(null);
        if (cocaCola == null) {
            cocaCola = sectionRepository.save(Section.builder()
                    .code("COCACOLA").name("Coca-Cola Exclusivos").displayOrder(4).build());
            seedCocaCola(cocaCola);
            log.info("Sección COCACOLA creada con 14 cromos");
        } else {
            int upserted = upsertCocaCola(cocaCola);
            if (upserted > 0) log.info("Coca-Cola: sincronizados {} cromos exclusivos", upserted);
        }

        for (Country country : countries) {
            List<Sticker> stickers = stickerRepository.findByCountryCode(country.getCode());
            String[] data = roster.get(country.getCode());
            int countryUpdates = 0;
            for (Sticker s : stickers) {
                Integer pos = s.getNumberInCountry();
                if (pos == null) continue;

                StickerType newType = (pos == 1) ? StickerType.BADGE
                        : (pos == 13) ? StickerType.TEAM_PHOTO
                        : StickerType.PLAYER;
                String newName = (pos == 1) ? "Escudo " + country.getName()
                        : (pos == 13) ? "Foto del equipo " + country.getName()
                        : playerName(country, pos, data);
                boolean newFoil = pos == 1;
                String newImage = imageUrlFor(country, pos, newType, newName);

                s.setStickerType(newType);
                s.setDisplayName(newName);
                s.setFoil(newFoil);
                s.setImageUrl(newImage);
                countryUpdates++;
                updated++;
            }
            stickerRepository.saveAll(stickers);
            stickerRepository.flush();
            log.info("  {} ({}) → {} stickers", country.getCode(), country.getName(), countryUpdates);
        }
        log.info("Migración completada: {} stickers actualizados", updated);
    }

    private int upsertCocaCola(Section cocaCola) {
        int changed = 0;
        for (String[] row : cocaColaData()) {
            String code = row[0];
            String name = row[1];
            Country country = countryRepository.findByCode(row[2]).orElse(null);
            Sticker sticker = stickerRepository.findByCode(code).orElse(null);
            if (sticker == null) {
                stickerRepository.save(Sticker.builder()
                        .section(cocaCola)
                        .country(country)
                        .code(code)
                        .displayName(name)
                        .stickerType(StickerType.SPECIAL)
                        .foil(true)
                        .inPacks(false)
                        .imageUrl(playerAvatarUrl(name))
                        .build());
                changed++;
                continue;
            }
            sticker.setSection(cocaCola);
            sticker.setCountry(country);
            sticker.setNumberInCountry(null);
            sticker.setDisplayName(name);
            sticker.setStickerType(StickerType.SPECIAL);
            sticker.setFoil(true);
            sticker.setInPacks(false);
            sticker.setImageUrl(playerAvatarUrl(name));
            stickerRepository.save(sticker);
            changed++;
        }
        return changed;
    }

    // ---------- Helpers ----------

    /**
     * Nombre del jugador para la posición. Si hay roster real lo usa; si no,
     * cae a "Jugador N" donde N es el índice contando solo posiciones de jugador
     * (excluyendo escudo y team photo).
     */
    private String playerName(Country country, int pos, String[] roster) {
        if (roster != null && pos >= 1 && pos <= 20 && roster[pos - 1] != null) {
            return roster[pos - 1];
        }
        int playerIndex = pos < 13 ? (pos - 1) : (pos - 2);  // skip pos 1 (badge) y 13 (team photo)
        return country.getName() + " · Jugador " + playerIndex;
    }

    private String imageUrlFor(Country country, int pos, StickerType type, String displayName) {
        if (type == StickerType.BADGE) {
            // Escudo = bandera grande del país
            return "https://flagcdn.com/w160/" + country.getIso2().toLowerCase() + ".png";
        }
        if (type == StickerType.TEAM_PHOTO) {
            // Sin imagen real disponible; el frontend usará un placeholder
            return null;
        }
        // Avatar con iniciales del jugador
        String name = displayName.replace(country.getName() + " · ", "");
        String encoded = URLEncoder.encode(name, StandardCharsets.UTF_8);
        return "https://ui-avatars.com/api/?name=" + encoded
                + "&background=0033A0&color=ffffff&size=200&bold=true&font-size=0.4";
    }

    private Sticker buildSpecial(Section section, String code, String name, StickerType type, boolean foil) {
        return Sticker.builder()
                .section(section)
                .code(code)
                .displayName(name)
                .stickerType(type)
                .foil(foil)
                .inPacks(true)
                .build();
    }
}

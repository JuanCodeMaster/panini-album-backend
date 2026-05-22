package com.panini.album.catalog;

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
        // 12 cromos exclusivos de Coca-Cola (no en sobres estándar).
        // Formato: code, displayName, countryCode (para fondo bandera + agrupación)
        String[][] data = {
                {"CC1",  "Lamine Yamal",        "ESP"},
                {"CC2",  "Joshua Kimmich",      "GER"},
                {"CC3",  "Harry Kane",          "ENG"},
                {"CC4",  "Santiago Giménez",    "MEX"},
                {"CC5",  "Antonee Robinson",    "USA"},
                {"CC6",  "Jefferson Lerma",     "COL"},
                {"CC7",  "Edson Álvarez",       "MEX"},
                {"CC8",  "Virgil van Dijk",     "NED"},
                {"CC9",  "Alphonso Davies",     "CAN"},
                {"CC10", "Weston McKennie",     "USA"},
                {"CC11", "Lautaro Martínez",    "ARG"},
                {"CC12", "Gabriel Magalhães",   "BRA"},
        };

        List<Sticker> stickers = new ArrayList<>();
        for (String[] row : data) {
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
        String[][] data = {
                {"ALG", "Argelia", "DZ"}, {"ARG", "Argentina", "AR"},
                {"AUS", "Australia", "AU"}, {"AUT", "Austria", "AT"},
                {"BEL", "Bélgica", "BE"}, {"BIH", "Bosnia y Herzegovina", "BA"},
                {"BRA", "Brasil", "BR"}, {"CAN", "Canadá", "CA"},
                {"CIV", "Costa de Marfil", "CI"}, {"COD", "Congo RD", "CD"},
                {"COL", "Colombia", "CO"}, {"CPV", "Cabo Verde", "CV"},
                {"CRO", "Croacia", "HR"}, {"CUW", "Curazao", "CW"},
                {"CZE", "Chequia", "CZ"}, {"ECU", "Ecuador", "EC"},
                {"EGY", "Egipto", "EG"}, {"ENG", "Inglaterra", "GB-ENG"},
                {"ESP", "España", "ES"}, {"FRA", "Francia", "FR"},
                {"GER", "Alemania", "DE"}, {"GHA", "Ghana", "GH"},
                {"HAI", "Haití", "HT"}, {"IRN", "Irán", "IR"},
                {"IRQ", "Irak", "IQ"}, {"JOR", "Jordania", "JO"},
                {"JPN", "Japón", "JP"}, {"KOR", "Corea del Sur", "KR"},
                {"KSA", "Arabia Saudita", "SA"}, {"MAR", "Marruecos", "MA"},
                {"MEX", "México", "MX"}, {"NED", "Países Bajos", "NL"},
                {"NOR", "Noruega", "NO"}, {"NZL", "Nueva Zelanda", "NZ"},
                {"PAN", "Panamá", "PA"}, {"PAR", "Paraguay", "PY"},
                {"POR", "Portugal", "PT"}, {"QAT", "Qatar", "QA"},
                {"RSA", "Sudáfrica", "ZA"}, {"SCO", "Escocia", "GB-SCT"},
                {"SEN", "Senegal", "SN"}, {"SUI", "Suiza", "CH"},
                {"SWE", "Suecia", "SE"}, {"TUN", "Túnez", "TN"},
                {"TUR", "Turquía", "TR"}, {"URU", "Uruguay", "UY"},
                {"USA", "Estados Unidos", "US"}, {"UZB", "Uzbekistán", "UZ"},
        };
        Map<String, String> groups = wcGroupMap();
        int order = 1;
        for (String[] row : data) {
            countryRepository.save(Country.builder()
                    .code(row[0]).name(row[1]).iso2(row[2])
                    .wcGroup(groups.get(row[0]))
                    .displayOrder(order++).build());
        }
    }

    /**
     * Mapping de cada selección a su grupo del Mundial 2026 (A..L, 12 grupos × 4).
     * Distribución mock para anclar la UI; el sorteo real cambiará estos valores.
     */
    private static Map<String, String> wcGroupMap() {
        Map<String, String> m = new java.util.HashMap<>();
        // Anfitriones separados (CONCACAF + invitados rotativos)
        m.put("MEX", "A"); m.put("KOR", "A"); m.put("CUW", "A"); m.put("NOR", "A");
        m.put("USA", "B"); m.put("HAI", "B"); m.put("IRN", "B"); m.put("AUT", "B");
        m.put("CAN", "C"); m.put("PAR", "C"); m.put("RSA", "C"); m.put("BIH", "C");
        // Top seeds CONMEBOL
        m.put("ARG", "D"); m.put("IRQ", "D"); m.put("EGY", "D"); m.put("CPV", "D");
        m.put("BRA", "E"); m.put("NZL", "E"); m.put("JOR", "E"); m.put("COD", "E");
        // UEFA / mezcla
        m.put("ESP", "F"); m.put("KSA", "F"); m.put("CIV", "F"); m.put("UZB", "F");
        m.put("FRA", "G"); m.put("PAN", "G"); m.put("ALG", "G"); m.put("QAT", "G");
        m.put("GER", "H"); m.put("ECU", "H"); m.put("GHA", "H"); m.put("AUS", "H");
        m.put("ENG", "I"); m.put("NED", "I"); m.put("SUI", "I"); m.put("CZE", "I");
        m.put("POR", "J"); m.put("COL", "J"); m.put("TUR", "J"); m.put("SEN", "J");
        m.put("BEL", "K"); m.put("JPN", "K"); m.put("MAR", "K"); m.put("SCO", "K");
        m.put("CRO", "L"); m.put("URU", "L"); m.put("TUN", "L"); m.put("SWE", "L");
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

        // Crear sección Coca-Cola si no existe (con sus 12 cromos)
        if (sectionRepository.findByCode("COCACOLA").isEmpty()) {
            Section cc = sectionRepository.save(Section.builder()
                    .code("COCACOLA").name("Coca-Cola Exclusivos").displayOrder(4).build());
            seedCocaCola(cc);
            log.info("Sección COCACOLA creada con 12 cromos");
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

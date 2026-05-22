package com.panini.album.catalog;

import java.util.HashMap;
import java.util.Map;

/**
 * Datos oficiales de jugadores para el álbum Panini Mundial 2026.
 * Estructura real por país (20 stickers):
 *   posición 1  → escudo (BADGE FOIL)
 *   posiciones 2-12  → 11 jugadores (porteros + defensas + medios)
 *   posición 13 → foto del equipo (TEAM_PHOTO)
 *   posiciones 14-20 → 7 jugadores (medios ofensivos + delanteros)
 *
 * Solo se incluyen las selecciones con checklist público disponible. Para las
 * demás se usan nombres genéricos "Jugador N" preservando la estructura real.
 */
public final class PlayerData {

    private PlayerData() {}

    /**
     * @return Mapa de código de país → arreglo de 20 nombres (índice 0..19 corresponde
     *         a posición 1..20). Las posiciones 1 y 13 son siempre el escudo y la foto.
     */
    public static Map<String, String[]> teams() {
        Map<String, String[]> m = new HashMap<>();

        m.put("ARG", new String[]{
                "Escudo Argentina",
                "Emiliano Martínez", "Nahuel Molina", "Cristian Romero", "Nicolás Otamendi",
                "Nicolás Tagliafico", "Leonardo Balerdi", "Enzo Fernández", "Alexis Mac Allister",
                "Rodrigo De Paul", "Exequiel Palacios", "Leandro Paredes",
                "Foto del equipo Argentina",
                "Nico Paz", "Franco Mastantuono", "Nico González", "Lionel Messi",
                "Lautaro Martínez", "Julián Álvarez", "Giuliano Simeone"
        });

        m.put("BRA", new String[]{
                "Escudo Brasil",
                "Alisson", "Bento", "Marquinhos", "Éder Militão",
                "Gabriel Magalhães", "Danilo", "Wesley", "Lucas Paquetá",
                "Casemiro", "Bruno Guimarães", "Luiz Henrique",
                "Foto del equipo Brasil",
                "Vinícius Júnior", "Rodrygo", "João Pedro", "Matheus Cunha",
                "Gabriel Martinelli", "Raphinha", "Estêvão"
        });

        m.put("ESP", new String[]{
                "Escudo España",
                "Unai Simón", "Robin Le Normand", "Aymeric Laporte", "Dean Huijsen",
                "Pedro Porro", "Dani Carvajal", "Marc Cucurella", "Martín Zubimendi",
                "Rodri", "Pedri", "Fabián Ruiz",
                "Foto del equipo España",
                "Mikel Merino", "Lamine Yamal", "Dani Olmo", "Nico Williams",
                "Ferran Torres", "Álvaro Morata", "Mikel Oyarzabal"
        });

        m.put("FRA", new String[]{
                "Escudo Francia",
                "Mike Maignan", "Theo Hernández", "William Saliba", "Jules Koundé",
                "Ibrahima Konaté", "Dayot Upamecano", "Lucas Digne", "Aurélien Tchouaméni",
                "Eduardo Camavinga", "Manu Koné", "Adrien Rabiot",
                "Foto del equipo Francia",
                "Michael Olise", "Ousmane Dembélé", "Bradley Barcola", "Désiré Doué",
                "Kingsley Coman", "Hugo Ekitiké", "Kylian Mbappé"
        });

        m.put("GER", new String[]{
                "Escudo Alemania",
                "Marc-André ter Stegen", "Jonathan Tah", "David Raum", "Nico Schlotterbeck",
                "Antonio Rüdiger", "Waldemar Anton", "Ridle Baku", "Maximilian Mittelstädt",
                "Joshua Kimmich", "Florian Wirtz", "Felix Nmecha",
                "Foto del equipo Alemania",
                "Leon Goretzka", "Jamal Musiala", "Serge Gnabry", "Kai Havertz",
                "Leroy Sané", "Karim Adeyemi", "Nick Woltemade"
        });

        m.put("ENG", new String[]{
                "Escudo Inglaterra",
                "Jordan Pickford", "John Stones", "Marc Guéhi", "Ezri Konsa",
                "Trent Alexander-Arnold", "Reece James", "Dan Burn", "Jordan Henderson",
                "Declan Rice", "Jude Bellingham", "Cole Palmer",
                "Foto del equipo Inglaterra",
                "Morgan Rogers", "Anthony Gordon", "Phil Foden", "Bukayo Saka",
                "Harry Kane", "Marcus Rashford", "Ollie Watkins"
        });

        m.put("POR", new String[]{
                "Escudo Portugal",
                "Diogo Costa", "José Sá", "Rúben Dias", "João Cancelo",
                "Diogo Dalot", "Nuno Mendes", "Gonçalo Inácio", "Bernardo Silva",
                "Bruno Fernandes", "Rúben Neves", "Vitinha",
                "Foto del equipo Portugal",
                "João Neves", "Cristiano Ronaldo", "Francisco Trincão", "João Félix",
                "Gonçalo Ramos", "Pedro Neto", "Rafael Leão"
        });

        m.put("USA", new String[]{
                "Escudo Estados Unidos",
                "Matt Freese", "Chris Richards", "Tim Ream", "Mark McKenzie",
                "Alex Freeman", "Antonee Robinson", "Tyler Adams", "Tanner Tessmann",
                "Weston McKennie", "Christian Roldan", "Timothy Weah",
                "Foto del equipo Estados Unidos",
                "Diego Luna", "Malik Tillman", "Christian Pulisic", "Brenden Aaronson",
                "Ricardo Pepi", "Haji Wright", "Folarin Balogun"
        });

        m.put("MEX", new String[]{
                "Escudo México",
                "Luis Malagón", "Johan Vásquez", "Jorge Sánchez", "César Montes",
                "Jesús Gallardo", "Israel Reyes", "Diego Lainez", "Carlos Rodríguez",
                "Edson Álvarez", "Orbelín Pineda", "Marcel Ruiz",
                "Foto del equipo México",
                "Érick Sánchez", "Hirving Lozano", "Santiago Giménez", "Raúl Jiménez",
                "Alexis Vega", "Roberto Alvarado", "César Huerta"
        });

        m.put("CAN", new String[]{
                "Escudo Canadá",
                "Dayne St. Clair", "Alphonso Davies", "Alistair Johnston", "Samuel Adekugbe",
                "Richie Laryea", "Derek Cornelius", "Moïse Bombito", "Kamal Miller",
                "Stephen Eustáquio", "Ismaël Koné", "Jonathan Osorio",
                "Foto del equipo Canadá",
                "Jacob Shaffelburg", "Mathieu Choinière", "Niko Sigur", "Tajon Buchanan",
                "Liam Millar", "Cyle Larin", "Jonathan David"
        });

        m.put("COL", new String[]{
                "Escudo Colombia",
                "Camilo Vargas", "David Ospina", "Dávinson Sánchez", "Yerry Mina",
                "Daniel Muñoz", "Johan Mojica", "Jhon Lucumí", "Santiago Arias",
                "Jefferson Lerma", "Kevin Castaño", "Richard Ríos",
                "Foto del equipo Colombia",
                "James Rodríguez", "Juan Fernando Quintero", "Jorge Carrascal", "Jhon Arias",
                "Jhon Córdoba", "Luis Suárez", "Luis Díaz"
        });

        m.put("URU", new String[]{
                "Escudo Uruguay",
                "Sergio Rochet", "Santiago Mele", "Ronald Araújo", "José María Giménez",
                "Sebastián Cáceres", "Mathías Olivera", "Guillermo Varela", "Nahitan Nández",
                "Federico Valverde", "Giorgian De Arrascaeta", "Rodrigo Bentancur",
                "Foto del equipo Uruguay",
                "Manuel Ugarte", "Nicolás de la Cruz", "Maxi Araújo", "Darwin Núñez",
                "Federico Viñas", "Rodrigo Aguirre", "Facundo Pellistri"
        });

        m.put("CRO", new String[]{
                "Escudo Croacia",
                "Dominik Livaković", "Duje Ćaleta-Car", "Joško Gvardiol", "Josip Stanišić",
                "Luka Vušković", "Josip Šutalo", "Kristijan Jakić", "Luka Modrić",
                "Mateo Kovačić", "Martin Baturina", "Lovro Majer",
                "Foto del equipo Croacia",
                "Mario Pašalić", "Petar Sučić", "Ivan Perišić", "Marco Pašalić",
                "Ante Budimir", "Andrej Kramarić", "Franjo Ivanović"
        });

        m.put("NED", new String[]{
                "Escudo Países Bajos",
                "Bart Verbruggen", "Virgil van Dijk", "Micky van de Ven", "Jurriën Timber",
                "Denzel Dumfries", "Nathan Aké", "Jeremie Frimpong", "Jan Paul van Hecke",
                "Tijjani Reijnders", "Ryan Gravenberch", "Teun Koopmeiners",
                "Foto del equipo Países Bajos",
                "Frenkie de Jong", "Xavi Simons", "Justin Kluivert", "Memphis Depay",
                "Donyell Malen", "Wout Weghorst", "Cody Gakpo"
        });

        m.put("BEL", new String[]{
                "Escudo Bélgica",
                "Thibaut Courtois", "Arthur Theate", "Timothy Castagne", "Zeno Debast",
                "Brandon Mechele", "Maxim De Cuyper", "Thomas Meunier", "Youri Tielemans",
                "Amadou Onana", "Nicolas Raskin", "Alexis Saelemaekers",
                "Foto del equipo Bélgica",
                "Hans Vanaken", "Kevin De Bruyne", "Jérémy Doku", "Charles De Ketelaere",
                "Leandro Trossard", "Loïs Openda", "Romelu Lukaku"
        });

        m.put("MAR", new String[]{
                "Escudo Marruecos",
                "Yassine Bounou", "Munir El Kajoui", "Achraf Hakimi", "Noussair Mazraoui",
                "Nayef Aguerd", "Romain Saïss", "Jawad El Yamiq", "Adam Masina",
                "Sofyan Amrabat", "Azzedine Ounahi", "Eliesse Ben Seghir",
                "Foto del equipo Marruecos",
                "Bilal El Khannouss", "Ismael Saibari", "Youssef En-Nesyri", "Abde Ezzalzouli",
                "Soufiane Rahimi", "Brahim Díaz", "Ayoub El Kaabi"
        });

        m.put("JPN", new String[]{
                "Escudo Japón",
                "Zion Suzuki", "Henry Hiroki Mochizuki", "Ayumu Seko", "Junnosuke Suzuki",
                "Shogo Taniguchi", "Tsuyoshi Watanabe", "Kaishu Sano", "Yuki Soma",
                "Ao Tanaka", "Daichi Kamada", "Takefusa Kubo",
                "Foto del equipo Japón",
                "Ritsu Doan", "Keito Nakamura", "Takumi Minamino", "Shuto Machino",
                "Junya Ito", "Koki Ogawa", "Ayase Ueda"
        });

        m.put("KOR", new String[]{
                "Escudo Corea del Sur",
                "Jo Hyeon-woo", "Kim Seung-Gyu", "Kim Min-jae", "Cho Yu-min",
                "Seol Young-woo", "Lee Han-beom", "Lee Tae-seok", "Lee Myung-jae",
                "Lee Jae-sung", "Hwang In-beom", "Lee Kang-in",
                "Foto del equipo Corea del Sur",
                "Paik Seung-ho", "Jens Castrop", "Lee Dong-gyeong", "Cho Gue-sung",
                "Son Heung-min", "Hwang Hee-chan", "Oh Hyeon-Gyu"
        });

        m.put("AUS", new String[]{
                "Escudo Australia",
                "Mathew Ryan", "Joe Gauci", "Harry Souttar", "Alessandro Circati",
                "Jordan Bos", "Aziz Behich", "Cameron Burgess", "Lewis Miller",
                "Miloš Degenek", "Jackson Irvine", "Riley McGree",
                "Foto del equipo Australia",
                "Aiden O'Neill", "Connor Metcalfe", "Patrick Yazbek", "Craig Goodwin",
                "Kusini Yengi", "Nestory Irankunda", "Mohamed Touré"
        });

        m.put("SUI", new String[]{
                "Escudo Suiza",
                "Gregor Kobel", "Yvon Mvogo", "Manuel Akanji", "Ricardo Rodríguez",
                "Nico Elvedi", "Aurèle Amenda", "Silvan Widmer", "Granit Xhaka",
                "Denis Zakaria", "Remo Freuler", "Fabian Rieder",
                "Foto del equipo Suiza",
                "Ardon Jashari", "Johan Manzambi", "Michel Aebischer", "Breel Embolo",
                "Rubén Vargas", "Dan Ndoye", "Zeki Amdouni"
        });

        m.put("NOR", new String[]{
                "Escudo Noruega",
                "Ørjan Nyland", "Julian Ryerson", "Leo Østigård", "Kristoffer Vassbakk Ajer",
                "Marcus Holmgren Pedersen", "David Møller Wolfe", "Torbjørn Heggem",
                "Morten Thorsby", "Martin Ødegaard", "Sander Berge", "Andreas Schjelderup",
                "Foto del equipo Noruega",
                "Patrick Berg", "Erling Haaland", "Alexander Sørloth", "Aron Dønnum",
                "Jørgen Strand Larsen", "Antonio Nusa", "Oscar Bobb"
        });

        m.put("IRN", new String[]{
                "Escudo Irán",
                "Alireza Beiranvand", "Morteza Pouraliganji", "Ehsan Hajsafi", "Milad Mohammadi",
                "Shojae Khalilzadeh", "Ramin Rezaeian", "Hossein Kanaani", "Sadegh Moharrami",
                "Saleh Hardani", "Saeed Ezatolahi", "Saman Ghoddos",
                "Foto del equipo Irán",
                "Omid Noorafkan", "Roozbeh Cheshmi", "Mohammad Mohebi", "Sardar Azmoun",
                "Mehdi Taremi", "Alireza Jahanbakhsh", "Ali Gholizadeh"
        });

        m.put("KSA", new String[]{
                "Escudo Arabia Saudita",
                "Nawaf Alaqidi", "Abdulrahman Al-Sanbi", "Saud Abdulhamid", "Nawaf Bouwashl",
                "Jihad Thakri", "Moteb Al-Harbi", "Hassan Altambakti", "Musab Aljuwayr",
                "Ziyad Aljohani", "Abdullah Alkhaibari", "Nasser Aldawsari",
                "Foto del equipo Arabia Saudita",
                "Saleh Abu Alshamat", "Marwan Alsahafi", "Salem Aldawsari",
                "Abdulrahman Al-Aboud", "Feras Akbrikan", "Saleh Alshehri", "Abdullah Al-Hamdan"
        });

        m.put("SCO", new String[]{
                "Escudo Escocia",
                "Angus Gunn", "Jack Hendry", "Kieran Tierney", "Aaron Hickey",
                "Andrew Robertson", "Scott McKenna", "John Souttar", "Anthony Ralston",
                "Grant Hanley", "Scott McTominay", "Billy Gilmour",
                "Foto del equipo Escocia",
                "Lewis Ferguson", "Ryan Christie", "Kenny McLean", "John McGinn",
                "Lyndon Dykes", "Che Adams", "Ben Gannon-Doak"
        });

        m.put("SWE", new String[]{
                "Escudo Suecia",
                "Victor Johansson", "Isak Hien", "Gabriel Gudmundsson", "Emil Holm",
                "Victor Nilsson Lindelöf", "Gustaf Lagerbielke", "Lucas Bergvall",
                "Hugo Larsson", "Jesper Karlström", "Yasin Ayari", "Mattias Svanberg",
                "Foto del equipo Suecia",
                "Daniel Svensson", "Ken Sema", "Roony Bardghji", "Dejan Kulusevski",
                "Anthony Elanga", "Alexander Isak", "Viktor Gyökeres"
        });

        m.put("EGY", new String[]{
                "Escudo Egipto",
                "Mohamed El Shenawy", "Mohamed Hany", "Mohamed Hamdy", "Yasser Ibrahim",
                "Khaled Sobhi", "Ramy Rabia", "Hossam Abdelmaguid", "Ahmed Fatouh",
                "Marwan Attia", "Zizo", "Hamdy Fathy",
                "Foto del equipo Egipto",
                "Mohamed Lasheen", "Emam Ashour", "Osama Faisal", "Mohamed Salah",
                "Mostafa Mohamed", "Trezeguet", "Omar Marmoush"
        });

        m.put("AUT", new String[]{
                "Escudo Austria",
                "Alexander Schlager", "Patrick Pentz", "David Alaba", "Kevin Danso",
                "Philipp Lienhart", "Stefan Posch", "Phillipp Mwene", "Alexander Prass",
                "Xaver Schlager", "Marcel Sabitzer", "Konrad Laimer",
                "Foto del equipo Austria",
                "Florian Grillitsch", "Nicolas Seiwald", "Romano Schmid", "Patrick Wimmer",
                "Christoph Baumgartner", "Michael Gregoritsch", "Marko Arnautović"
        });

        m.put("TUN", new String[]{
                "Escudo Túnez",
                "Bechir Ben Said", "Aymen Dahmen", "Yan Valery", "Montassar Talbi",
                "Yassine Meriah", "Ali Abdi", "Dylan Bronn", "Ellyes Skhiri",
                "Aïssa Laïdouni", "Ferjani Sassi", "Mohamed Ali Ben Romdhane",
                "Foto del equipo Túnez",
                "Hannibal Mejbri", "Elias Achouri", "Elias Saad", "Hazem Mastouri",
                "Ismaël Gharbi", "Sayfallah Ltaief", "Naïm Sliti"
        });

        m.put("CZE", new String[]{
                "Escudo Chequia",
                "Matěj Kovář", "Jindřich Staněk", "Ladislav Krejčí", "Vladimír Coufal",
                "Jaroslav Zelený", "Tomáš Holeš", "David Zima", "Michal Sadílek",
                "Lukáš Provod", "Lukáš Červ", "Tomáš Souček",
                "Foto del equipo Chequia",
                "Pavel Šulc", "Matěj Vydra", "Vasil Kušej", "Tomáš Chorý",
                "Václav Černý", "Adam Hložek", "Patrik Schick"
        });

        m.put("TUR", new String[]{
                "Escudo Turquía",
                "Uğurcan Çakır", "Mert Müldür", "Zeki Çelik", "Abdülkerim Bardakcı",
                "Çağlar Söyüncü", "Merih Demiral", "Ferdi Kadıoğlu", "Kaan Ayhan",
                "İsmail Yüksek", "Hakan Çalhanoğlu", "Orkun Kökçü",
                "Foto del equipo Turquía",
                "Arda Güler", "İrfan Can Kahveci", "Yunus Akgün", "Can Uzun",
                "Barış Alper Yılmaz", "Kerem Aktürkoğlu", "Kenan Yıldız"
        });

        m.put("SEN", new String[]{
                "Escudo Senegal",
                "Édouard Mendy", "Yehvann Diouf", "Moussa Niakhaté", "Abdoulaye Seck",
                "Ismail Jakobs", "El Hadji Malick Diouf", "Kalidou Koulibaly",
                "Idrissa Gana Gueye", "Pape Matar Sarr", "Pape Gueye", "Habib Diarra",
                "Foto del equipo Senegal",
                "Lamine Camara", "Sadio Mané", "Ismaïla Sarr", "Boulaye Dia",
                "Iliman Ndiaye", "Nicolas Jackson", "Krépin Diatta"
        });

        m.put("GHA", new String[]{
                "Escudo Ghana",
                "Lawrence Ati Zigi", "Tariq Lamptey", "Mohammed Salisu", "Alidu Seidu",
                "Alexander Djiku", "Gideon Mensah", "Caleb Yirenkyi", "Abdul Fatawu Issahaku",
                "Thomas Partey", "Salis Abdul Samed", "Kamaldeen Sulemana",
                "Foto del equipo Ghana",
                "Mohammed Kudus", "Iñaki Williams", "Jordan Ayew", "André Ayew",
                "Joseph Paintsil", "Osman Bukari", "Antoine Semenyo"
        });

        m.put("ALG", new String[]{
                "Escudo Argelia",
                "Alexis Guendouz", "Ramy Bensebaini", "Youcef Atal", "Rayan Aït-Nouri",
                "Mohamed Amine Tougaï", "Aïssa Mandi", "Ismaël Bennacer", "Houssem Aouar",
                "Hicham Boudaoui", "Ramiz Zerrouki", "Nabil Bentaleb",
                "Foto del equipo Argelia",
                "Farès Chaïbi", "Riyad Mahrez", "Saïd Benrahma", "Anis Hadj Moussa",
                "Amine Gouiri", "Baghdad Bounedjah", "Mohammed Amoura"
        });

        m.put("ECU", new String[]{
                "Escudo Ecuador",
                "Hernán Galíndez", "Gonzalo Valle", "Piero Hincapié", "Pervis Estupiñán",
                "Willian Pacho", "Ángelo Preciado", "Joel Ordóñez", "Moisés Caicedo",
                "Alan Franco", "Kendry Páez", "Pedro Vite",
                "Foto del equipo Ecuador",
                "John Yeboah", "Leonardo Campana", "Gonzalo Plata", "Nilson Angulo",
                "Alan Minda", "Kevin Rodríguez", "Enner Valencia"
        });

        m.put("PAR", new String[]{
                "Escudo Paraguay",
                "Roberto Fernández", "Orlando Gill", "Gustavo Gómez", "Fabián Balbuena",
                "Juan José Cáceres", "Omar Alderete", "Junior Alonso", "Mathías Villasanti",
                "Diego Gómez", "Damián Bobadilla", "Andrés Cubas",
                "Foto del equipo Paraguay",
                "Matías Galarza Fonda", "Julio Enciso", "Alejandro Romero Gamarra",
                "Miguel Almirón", "Ramón Sosa", "Ángel Romero", "Antonio Sanabria"
        });

        m.put("PAN", new String[]{
                "Escudo Panamá",
                "Orlando Mosquera", "Luis Mejía", "Fidel Escobar", "Andrés Andrade",
                "Michael Amir Murillo", "Eric Davis", "José Córdoba", "César Blackman",
                "Cristian Martínez", "Aníbal Godoy", "Adalberto Carrasquilla",
                "Foto del equipo Panamá",
                "Édgar Bárcenas", "Carlos Harvey", "Ismael Díaz", "José Fajardo",
                "Cecilio Waterman", "José Luis Rodríguez", "Alberto Quintero"
        });

        m.put("QAT", new String[]{
                "Escudo Qatar",
                "Meshaal Barsham", "Sultan Albrake", "Lucas Mendes", "Homam Ahmed",
                "Boualem Khoukhi", "Pedro Miguel", "Tarek Salman", "Mohamed Al-Mannai",
                "Karim Boudiaf", "Assim Madibo", "Ahmed Fatehi",
                "Foto del equipo Qatar",
                "Mohammed Waad", "Abdulaziz Hatem", "Hassan Al-Haydos", "Edmilson Junior",
                "Akram Afif", "Ahmed Al Ganehi", "Almoez Ali"
        });

        m.put("CIV", new String[]{
                "Escudo Costa de Marfil",
                "Yahia Fofana", "Ghislain Konan", "Wilfried Singo", "Odilon Kossounou",
                "Evan Ndicka", "Willy Boly", "Emmanuel Agbadou", "Ousmane Diomandé",
                "Franck Kessié", "Seko Fofana", "Ibrahim Sangaré",
                "Foto del equipo Costa de Marfil",
                "Jean-Philippe Gbamin", "Amad Diallo", "Sébastien Haller", "Simon Adingra",
                "Yan Diomandé", "Evann Guessand", "Oumar Diakité"
        });

        m.put("COD", new String[]{
                "Escudo Congo RD",
                "Lionel Mpasi", "Aaron Wan-Bissaka", "Axel Tuanzebe", "Arthur Masuaku",
                "Chancel Mbemba", "Joris Kayembe", "Charles Pickel", "Ngal'ayel Mukau",
                "Edo Kayembe", "Samuel Moutoussamy", "Noah Sadiki",
                "Foto del equipo Congo RD",
                "Théo Bongonda", "Meschack Elia", "Yoane Wissa", "Brian Cipenga",
                "Fiston Mayele", "Cédric Bakambu", "Nathanaël Mbuku"
        });

        m.put("CPV", new String[]{
                "Escudo Cabo Verde",
                "Vozinha", "Logan Costa", "Pico", "Diney",
                "Steven Moreira", "Wagner Pina", "João Paulo", "Yannick Semedo",
                "Kevin Pina", "Patrick Andrade", "Jamiro Monteiro",
                "Foto del equipo Cabo Verde",
                "Deroy Duarte", "Garry Rodrigues", "Jovane Cabral", "Ryan Mendes",
                "Dailon Livramento", "Willy Semedo", "Bebé"
        });

        m.put("CUW", new String[]{
                "Escudo Curazao",
                "Eloy Room", "Armando Obispo", "Sherel Floranus", "Jürien Gaari",
                "Joshua Brenet", "Roshon van Eijma", "Shurandy Sambo", "Livano Comenencia",
                "Godfried Roemeratoe", "Juninho Bacuna", "Leandro Bacuna",
                "Foto del equipo Curazao",
                "Tahith Chong", "Kenji Gorré", "Jearl Margaritha", "Jürgen Locadia",
                "Jeremy Antonisse", "Gervane Kastaneer", "Sontje Hansen"
        });

        m.put("HAI", new String[]{
                "Escudo Haití",
                "Johny Placide", "Carlens Arcus", "Martin Experience", "Jean-Kévin Duverne",
                "Ricardo Adé", "Duke Lacroix", "Garven Métusala", "Hannes Delcroix",
                "Leverton Pierre", "Danley Jean Jacques", "Jean-Ricner Bellegarde",
                "Foto del equipo Haití",
                "Christopher Attys", "Derrick Etienne Jr.", "Josue Casimir",
                "Ruben Providence", "Duckens Nazon", "Louicius Deedson", "Frantzdy Pierrot"
        });

        m.put("IRQ", new String[]{
                "Escudo Irak",
                "Jalal Hassan", "Rebin Sulaka", "Hussein Ali", "Akam Hashem",
                "Merchas Doski", "Zaid Tahseen", "Manaf Younis", "Zidane Iqbal",
                "Amir Al-Ammari", "Ibrahim Bayesh", "Ali Jasim",
                "Foto del equipo Irak",
                "Youssef Amyn", "Aymen Hussein", "Marko Farji", "Osama Rashid",
                "Ali Al-Hamadi", "Aymen Hussein", "Mohanad Ali"
        });

        m.put("JOR", new String[]{
                "Escudo Jordania",
                "Yazeed Abulaila", "Ihsan Haddad", "Mohammad Abu Hashish", "Yazan Al-Arab",
                "Abdallah Nasib", "Saleem Obaid", "Mohammad Abu Hashish", "Ibrahim Saadeh",
                "Nizar Al-Rashdan", "Noor Al-Rawabdeh", "Mohannad Abu Taha",
                "Foto del equipo Jordania",
                "Amer Jamous", "Musa Al-Taamari", "Yazan Al-Naimat", "Mahmoud Al-Mardi",
                "Ali Olwan", "Mohammad Abu Zrayq", "Ibrahim Sabra"
        });

        m.put("NZL", new String[]{
                "Escudo Nueva Zelanda",
                "Max Crocombe", "Alex Paulsen", "Michael Boxall", "Liberato Cacace",
                "Tim Payne", "Tyler Bindon", "Francis de Vries", "Finn Surman",
                "Joe Bell", "Sarpreet Singh", "Ryan Thomas",
                "Foto del equipo Nueva Zelanda",
                "Matthew Garbett", "Marko Stamenić", "Ben Old", "Chris Wood",
                "Elijah Just", "Callum McCowatt", "Kosta Barbarouses"
        });

        m.put("RSA", new String[]{
                "Escudo Sudáfrica",
                "Ronwen Williams", "Sipho Chaine", "Aubrey Modiba", "Samukele Kabini",
                "Mbekezeli Mbokazi", "Khulumani Ndamane", "Siyabonga Ngezana",
                "Khuliso Mudau", "Nkosinathi Sibisi", "Teboho Mokoena", "Thalente Mbatha",
                "Foto del equipo Sudáfrica",
                "Bathusi Aubaas", "Yaya Sithole", "Sipho Mbule", "Lyle Foster",
                "Iqraam Rayners", "Mohau Nkota", "Oswin Appollis"
        });

        m.put("UZB", new String[]{
                "Escudo Uzbekistán",
                "Utkir Yusupov", "Farrukh Sayfiyev", "Sherzod Nasrullaev", "Umar Eshmurodov",
                "Husniddin Aliqulov", "Rustamjon Ashurmatov", "Khojiakbar Alijonov",
                "Abdukodir Khusanov", "Odiljon Hamrobekov", "Otabek Shukurov",
                "Jamshid Iskanderov",
                "Foto del equipo Uzbekistán",
                "Azizbek Turgunboev", "Khojimat Erkinov", "Eldor Shomurodov",
                "Oston Urunov", "Jaloliddin Masharipov", "Igor Sergeev", "Abbosbek Fayzullaev"
        });

        m.put("BIH", new String[]{
                "Escudo Bosnia y Herzegovina",
                "Nikola Vasilj", "Amer Dedić", "Sead Kolašinac", "Tarik Muharemović",
                "Nihad Mujakić", "Nikola Katić", "Amir Hadžiahmetović", "Benjamin Tahirović",
                "Armin Gigović", "Ivan Šunjić", "Ivan Bašić",
                "Foto del equipo Bosnia y Herzegovina",
                "Dženis Burnić", "Esmir Bajraktarević", "Amar Memić", "Ermedin Demirović",
                "Edin Džeko", "Samed Baždar", "Haris Tabaković"
        });

        return m;
    }
}

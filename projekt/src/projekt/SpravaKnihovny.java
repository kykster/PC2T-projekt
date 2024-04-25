package projekt;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SpravaKnihovny {
    private Map<String, Kniha> knihy;

    public SpravaKnihovny() {
        this.knihy = new HashMap<>();
    }

    public void pridejKnihu(String nazev, List<String> autori, int rokVydani, boolean jeDostupna, TypKnihy typ, Zanr zanr, Rocnik rocnik) {
        Kniha kniha = new Kniha(nazev, autori, rokVydani, jeDostupna, typ, zanr, rocnik);
        knihy.put(nazev, kniha);
    }
    
    public void pridejUcebnici(String nazev, List<String> autori, int rokVydani, boolean jeDostupna, String rocnikStr) {
        // Převedení textového vstupu na malá písmena
        Rocnik rocnik = Rocnik.valueOf(rocnikStr.toUpperCase());
        Kniha ucebnice = new Kniha(nazev, autori, rokVydani, jeDostupna, TypKnihy.Ucebnice, null, rocnik);
        knihy.put(nazev, ucebnice);
    }

    public void pridejRoman(String nazev, List<String> autori, int rokVydani, boolean jeDostupna, String zanrStr) {
        // Převedení textového vstupu na velká písmena
        Zanr zanr = Zanr.valueOf(zanrStr.toUpperCase());
        Kniha roman = new Kniha(nazev, autori, rokVydani, jeDostupna, TypKnihy.Roman, zanr, null);
        knihy.put(nazev, roman);
    }
    
    public boolean existujeKniha(String nazev) {
        return knihy.containsKey(nazev);
    }
    
    public void upravKniha(String nazev, String novyAutor, int novyRokVydani, boolean novaDostupnost) {
        if (knihy.containsKey(nazev)) {
            Kniha kniha = knihy.get(nazev);
            kniha.getAutori().clear();
            kniha.getAutori().add(novyAutor);
            kniha.setDostupna(novaDostupnost);
            kniha.rokVydani = novyRokVydani;
            System.out.println("Detaily knihy byly úspěšně upraveny.");
        } else {
            System.out.println("Kniha nebyla nalezena.");
        }
    }

    public void smazKniha(String nazev) {
        if (knihy.containsKey(nazev)) {
            knihy.remove(nazev);
            System.out.println("Kniha byla úspěšně smazána.");
        } else {
            System.out.println("Kniha nebyla nalezena.");
        }
    }

    public void zmenStavKniha(String nazev, boolean jeDostupna) {
        if (knihy.containsKey(nazev)) {
            Kniha kniha = knihy.get(nazev);
            kniha.setDostupna(jeDostupna);
            System.out.println("Stav knihy byl úspěšně změněn.");
        } else {
            System.out.println("Kniha nebyla nalezena.");
        }
    }
    
    public List<Kniha> getSeznamAbecedne() {
        List<Kniha> seznam = new ArrayList<>(knihy.values());
        Collections.sort(seznam, (k1, k2) -> k1.getNazev().compareTo(k2.getNazev()));
        return seznam;
    }

    public void vypisVsechnyKnihy() {
        for (Kniha kniha : knihy.values()) {
            System.out.println(kniha);
        }
    }

    public void hledejKniha(String nazev) {
        if (knihy.containsKey(nazev)) {
            Kniha kniha = knihy.get(nazev);
            System.out.println("Informace o knize:");
            System.out.println("Název: " + kniha.getNazev());
            System.out.println("Autoři: " + String.join(", ", kniha.getAutori()));
            System.out.println("Typ knihy: " + kniha.getTypKnihy());
            if (kniha.getTypKnihy() == TypKnihy.Roman) {
                System.out.println("Žánr: " + kniha.getZanr());
            } else if (kniha.getTypKnihy() == TypKnihy.Ucebnice) {
                System.out.println("Ročník: " + kniha.getRocnik());
            }
            System.out.println("Rok vydání: " + kniha.getRokVydani());
            System.out.println("Stav dostupnosti: " + (kniha.jeDostupna() ? "k dispozici" : "vypůjčena"));
        } else {
            System.out.println("Kniha nebyla nalezena.");
        }
    }

    public void vypisKnihyPodleAutora(String autor) {
        List<Kniha> autorskeKnihy = new ArrayList<>();
        for (Kniha kniha : knihy.values()) {
            if (kniha.getAutori().contains(autor)) {
                autorskeKnihy.add(kniha);
            }
        }
        autorskeKnihy.sort(Comparator.comparingInt(Kniha::getRokVydani));
        for (Kniha kniha : autorskeKnihy) {
            System.out.println(kniha);
        }
    }

    public void vypisKnihyPodleZanru(String zanrStr) {
        Zanr hledanyZanr = null;
        try {
            // Převedení textového vstupu na malá písmena a odstranění bílých znaků
            zanrStr = zanrStr.trim().toLowerCase();
            hledanyZanr = Zanr.valueOf(zanrStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Neplatný žánr. Zadejte znovu.");
            return;
        }

        boolean nalezeno = false;
        for (Kniha kniha : knihy.values()) {
            if (kniha.getTypKnihy() == TypKnihy.Roman && kniha.getZanr() == hledanyZanr) {
                System.out.println(kniha.getNazev()); // pouze název dané knihy pro daný žánr (pokud nechci, smaž .getNazev
                nalezeno = true;
            }
        }

        if (!nalezeno) {
            System.out.println("Žádné knihy tohoto žánru nebyly nalezeny.");
        }
    }

    public void vypisVypujceneKnihyPodleTypu() {
        boolean nalezeno = false;
        for (Kniha kniha : knihy.values()) {
            if (!kniha.jeDostupna()) {
                if (kniha.getTypKnihy() == TypKnihy.Roman) {
                    System.out.println("Vypůjčený román: " + kniha);
                    nalezeno = true;
                } else if (kniha.getTypKnihy() == TypKnihy.Ucebnice) {
                    System.out.println("Vypůjčená učebnice: " + kniha);
                    nalezeno = true;
                }
            }
        }

        if (!nalezeno) {
            System.out.println("Nebyly nalezeny žádné vypůjčené knihy.");
        }
    }

    public void ulozKnihuDoSouboru(String nazev) {
        if (knihy.containsKey(nazev)) {
            Kniha kniha = knihy.get(nazev);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(nazev + ".txt"))) {
                writer.write("Název: " + kniha.getNazev() + "\n");
                writer.write("Autoři: " + kniha.getAutori() + "\n");
                writer.write("Rok vydání: " + kniha.getRokVydani() + "\n");
                writer.write("Dostupnost: " + (kniha.jeDostupna() ? "Ano" : "Ne") + "\n");
                writer.write("Typ: " + kniha.getTypKnihy() + "\n");
                writer.write("Žánr: " + kniha.getZanr() + "\n");
                writer.write("Ročník: " + kniha.getRocnik() + "\n");
                System.out.println("Informace o knize byly úspěšně uloženy do souboru " + nazev + ".txt");
            } catch (IOException e) {
                System.out.println("Chyba při ukládání informací o knize do souboru.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Kniha nebyla nalezena.");
        }
    }

    public Kniha nactiKnihaZeSouboru(String nazevSouboru) throws NumberFormatException {
        try {
            // Ověření existence souboru
            File file = new File(nazevSouboru);
            if (!file.exists()) {
                System.out.println("Soubor '" + nazevSouboru + "' neexistuje.");
                return null;
            }

            // Otevření souboru pro čtení
            try (BufferedReader reader = new BufferedReader(new FileReader(nazevSouboru))) {
                String nazev = null;
                List<String> autori = new ArrayList<>();
                int rokVydani = 0;
                boolean jeDostupna = false;
                TypKnihy typ = null;
                Zanr zanr = null;
                Rocnik rocnik = null;

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Název:")) {
                        nazev = line.substring(7).trim();
                    } else if (line.startsWith("Autoři:")) {
                        String[] autoriArray = line.substring(8).trim().split(",");
                        autori.addAll(Arrays.asList(autoriArray));
                    } else if (line.startsWith("Rok vydání:")) {
                        rokVydani = Integer.parseInt(line.substring(11).trim());
                    } else if (line.startsWith("Dostupnost:")) {
                        jeDostupna = Boolean.parseBoolean(line.substring(11).trim());
                    } else if (line.startsWith("Typ:")) {
                        typ = TypKnihy.valueOf(line.substring(5).trim());
                    } else if (typ == TypKnihy.Roman && line.startsWith("Žánr:")) {
                        zanr = Zanr.valueOf(line.substring(6).trim());
                    } else if (typ == TypKnihy.Ucebnice && line.startsWith("Ročník:")) {
                        rocnik = Rocnik.valueOf(line.substring(8).trim());
                    }
                }

                // Vytvoření instance knihy s načtenými atributy
                if (nazev != null && !autori.isEmpty() && typ != null) {
                    return new Kniha(nazev, autori, rokVydani, jeDostupna, typ, zanr, rocnik);
                } else {
                    throw new IllegalArgumentException("Neúplné informace o knize ve souboru.");
                }
            }
        } catch (IOException e) {
            System.out.println("Nastala chyba při načítání knihy ze souboru.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Chyba při parsování dat ze souboru.");
            e.printStackTrace();
        }
        return null; // Pokud dojde k chybě nebo soubor je prázdný, vrátíme null
    }



    public void ulozDoSQLDatabaze() {
        try {
            // Otevření spojení s databází
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moje_databaze", "uzivatel", "heslo");

            // Příprava příkazu pro vložení nebo aktualizaci informací o knihách
            String insertOrUpdateQuery = "INSERT INTO knihy (nazev, autori, rok_vydani, dostupna, typ, zanr, rocnik) " +
                                         "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                                         "ON DUPLICATE KEY UPDATE autori = VALUES(autori), rok_vydani = VALUES(rok_vydani), " +
                                         "dostupna = VALUES(dostupna), typ = VALUES(typ), zanr = VALUES(zanr), rocnik = VALUES(rocnik)";
            PreparedStatement statement = connection.prepareStatement(insertOrUpdateQuery);

            // Pro každou knihu uložíme informace do databáze
            for (Kniha kniha : knihy.values()) {
                statement.setString(1, kniha.getNazev());
                statement.setString(2, String.join(",", kniha.getAutori()));
                statement.setInt(3, kniha.getRokVydani());
                statement.setBoolean(4, kniha.jeDostupna());
                statement.setString(5, kniha.getTypKnihy().toString());
                statement.setString(6, kniha.getZanr().toString());
                statement.setString(7, kniha.getRocnik().toString());
                statement.executeUpdate();
            }

            // Uzavření spojení s databází
            connection.close();
            System.out.println("Veškeré informace byly úspěšně uloženy do SQL databáze.");
        } catch (SQLException e) {
            System.out.println("Chyba při ukládání informací do SQL databáze.");
            e.printStackTrace();
        }
    }


    public void nactiZSQLDatabaze() {
        knihy = new HashMap<>(); // Inicializace mapy pro uložení načtených knih

        // Připojení k databázi
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moje_databaze", "uzivatel", "heslo")) {
            // Vytvoření dotazu pro načtení dat
            String sql = "SELECT nazev, autori, rokVydani, jeDostupna, typKnihy, zanr, rocnik FROM knihy";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Provedení dotazu a zpracování výsledků
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String nazev = resultSet.getString("nazev");
                        // Načtení seznamu autorů
                        List<String> autori = new ArrayList<>();
                        String[] autoriArray = resultSet.getString("autori").split(",");
                        for (String autor : autoriArray) {
                            autori.add(autor.trim());
                        }
                        int rokVydani = resultSet.getInt("rokVydani");
                        boolean jeDostupna = resultSet.getBoolean("jeDostupna");
                        String typ = resultSet.getString("typKnihy");
                        String zanr = resultSet.getString("zanr");
                        String rocnik = resultSet.getString("rocnik");
                        
                        // Vytvoření instance knihy a uložení do mapy
                        Kniha kniha = new Kniha(nazev, autori, rokVydani, jeDostupna, TypKnihy.valueOf(typ), Zanr.valueOf(zanr), Rocnik.valueOf(rocnik));
                        knihy.put(nazev, kniha); // Použijeme název knihy jako klíč
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Chyba při načítání dat z SQL databáze.");
            e.printStackTrace();
        }
    }
}

   
package projekt;

import java.util.List;

// Třída reprezentující knihu
public class Kniha {
    private String nazev;
    private List<String> autori;
    int rokVydani;
    private boolean jeDostupna;
    private TypKnihy typKnihy;
    private Zanr zanr;
    private Rocnik rocnik;

    public Kniha(String nazev, List<String> autori, int rokVydani, boolean jeDostupna, TypKnihy typKnihy, Zanr zanr, Rocnik rocnik) {
        this.nazev = nazev;
        this.autori = autori;
        this.rokVydani = rokVydani;
        this.jeDostupna = jeDostupna;
        this.typKnihy = typKnihy;
        this.zanr = zanr;
        this.rocnik = rocnik;
    }

    public String getNazev() {
        return nazev;
    }

    public List<String> getAutori() {
        return autori;
    }

    public int getRokVydani() {
        return rokVydani;
    }

    public boolean jeDostupna() {
        return jeDostupna;
    }

    public void setDostupna(boolean dostupna) {
        jeDostupna = dostupna;
    }

    public TypKnihy getTypKnihy() {
        return typKnihy;
    }
   
    public Zanr getZanr() {
        return zanr;
    }
  
    public Rocnik getRocnik() {
        return rocnik;
    }
    
    @Override
    public String toString() {
        return "Název: " + nazev + ", Autoři: " + autori + ", Rok vydání: " + rokVydani + ", Dostupnost: " + (jeDostupna ? "Ano" : "Ne");
    }
}

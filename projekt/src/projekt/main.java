package projekt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        SpravaKnihovny knihovna = new SpravaKnihovny();
        Scanner scanner = new Scanner(System.in);
        boolean beh = true;
        while (beh) {
        System.out.println("Vyberte možnost:");
        System.out.println("1. Přidat novou knihu");
        System.out.println("2. Upravit knihu");
        System.out.println("3. Smazat knihu");
        System.out.println("4. Označit knihu jako vypůjčenou/vrácenou");
        System.out.println("5. Vypsat všechny knihy");
        System.out.println("6. Hledat knihu");
        System.out.println("7. Vypsat všechny knihy od autora");
        System.out.println("8. Vypsat všechny knihy podle žánru");
        System.out.println("9. Vypsat všechny vypůjčené knihy podle typu");
        System.out.println("10. Uložit knihu do souboru");
        System.out.println("11. Načíst knihu ze souboru");
        System.out.println("12. Uložit do SQL databáze");
        System.out.println("13. Načíst z SQL databáze");
        System.out.println("14. Ukončit");

        int volba;
        try {
            volba = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Neplatná volba. Zadejte číslo od 1 do 14.");
            continue; // Přeskočí zbytek smyčky a začne další iteraci
        }

        switch (volba) {
        case 1:
            // Přidat novou knihu
            while (true) {
                System.out.println("Zadejte název knihy:");
                String nazev = scanner.nextLine();

                System.out.println("Zadejte autora/autory (oddělené čárkou):");
                String[] autori = scanner.nextLine().split(",");
                List<String> seznamAutoru = new ArrayList<>();
                for (String autor : autori) {
                    seznamAutoru.add(autor.trim());
                }

                int rokVydani;
                while (true) {
                    System.out.println("Zadejte rok vydání:");
                    String rokVydaniStr = scanner.nextLine();
                    try {
                        rokVydani = Integer.parseInt(rokVydaniStr);
                        break; // Platný vstup, opustíme smyčku
                    } catch (NumberFormatException e) {
                        System.out.println("Neplatný formát. Zadejte rok vydání znovu.");
                    }
                }

                while (true) {
                    System.out.println("Je kniha k dispozici? (ano/ne):");
                    String odpoved = scanner.nextLine().toLowerCase(); // Převedeme vstup na malá písmena
                    if (odpoved.equals("ano") || odpoved.equals("ne")) {
                        boolean jeDostupna = odpoved.equals("ano");

                        TypKnihy typ;
                        while (true) {
                            System.out.println("Zadejte typ knihy (Roman/Ucebnice):");
                            String typStr = scanner.nextLine().toLowerCase(); // Převedeme vstup na malá písmena
                            
                            if (typStr.equals("roman")) {
                                typ = TypKnihy.Roman;
                                String zanr;

                                while (true) {
                                    System.out.println("Zadejte žánr (Fantasy, Sci_fi, Detektivni, Romanticky, Horor):");
                                    String zanrStr = scanner.nextLine().toLowerCase(); // Převedeme vstup na malá písmena
                                    if (zanrStr.equals("fantasy") || zanrStr.equals("sci_fi") || zanrStr.equals("detektivni") || zanrStr.equals("romanticky") || zanrStr.equals("horor")) {
                                        knihovna.pridejRoman(nazev, seznamAutoru, rokVydani, jeDostupna, zanrStr);
                                        break; // Platný vstup, pokračujeme dál
                                    } else {
                                        System.out.println("Neplatná hodnota. Zadejte znovu (Fantasy, Sci_fi, Detektivni, Romanticky, Horor):");
                                    }
                                }
                                break;

                            } else if (typStr.equals("ucebnice")) {
                                typ = TypKnihy.Ucebnice;
                                String rocnikEnum = null;

                                while (true) {
                                    System.out.println("Zadejte ročník (Prvni, Druhy, Treti, Ctvrty, Paty, Sesty, Sedmy, Osmy, Devaty):");
                                    String rocnikStr = scanner.nextLine().toUpperCase(); // Převedeme vstup na velká písmena
                                    if (Arrays.stream(Rocnik.values()).anyMatch(r -> r.name().equals(rocnikStr))) {
                                        knihovna.pridejUcebnici(nazev, seznamAutoru, rokVydani, jeDostupna, rocnikStr);
                                        break; // Platný vstup, pokračujeme dál
                                    } else {
                                        System.out.println("Neplatná hodnota. Zadejte znovu:");
                                    }
                                }
                                break;
                            } else {
                                System.out.println("Neplatná hodnota. Zadejte znovu (Roman/Ucebnice):");
                            }
                        }
                        break; // Skončili jsme s přidáním knihy, opustíme smyčku pro odpověď na otázku o dostupnosti knihy
                    } else {
                        System.out.println("Neplatná hodnota. Zadejte znovu (ano/ne):");
                    }
                }
                break; // Skončili jsme s přidáním knihy, opustíme smyčku pro zadání typu knihy
            }
            break;




        case 2:
            // Upravit knihu
            System.out.println("Zadejte název knihy, kterou chcete upravit:");
            String upravitNazev = scanner.nextLine();

            // Zjistit, zda kniha existuje v knihovně
            if (!knihovna.existujeKniha(upravitNazev)) {
                System.out.println("Kniha s tímto názvem neexistuje.");
                break; // Ukončíme tento případ a vrátíme se zpět k výběru možností
            }

            System.out.println("Zadejte nového autora:");
            String novyAutor = scanner.nextLine();

            int novyRokVydani;
            while (true) {
                System.out.println("Zadejte nový rok vydání:");
                String rokVydaniStr = scanner.nextLine();
                try {
                    novyRokVydani = Integer.parseInt(rokVydaniStr);
                    break; // Platný vstup, opustíme smyčku
                } catch (NumberFormatException e) {
                    System.out.println("Neplatný formát. Zadejte rok vydání znovu.");
                }
            }

            System.out.println("Je kniha k dispozici? (ano/ne):");
            String odpoved1 = scanner.nextLine().toLowerCase(); // Převedeme vstup na malá písmena
            while (!odpoved1.equals("ano") && !odpoved1.equals("ne")) {
                System.out.println("Neplatná hodnota. Zadejte znovu (ano/ne):");
                odpoved1 = scanner.nextLine().toLowerCase(); // Přečtěme další vstup a znovu převedeme na malá písmena
            }
            boolean novaDostupnost = odpoved1.equals("ano");
            knihovna.upravKniha(upravitNazev, novyAutor, novyRokVydani, novaDostupnost);
            break;

            case 3:
                // Smazat knihu
                System.out.println("Zadejte název knihy, kterou chcete smazat:");
                String smazatNazev = scanner.nextLine();
                knihovna.smazKniha(smazatNazev);
                break;
            case 4:
                // Označit knihu jako vypůjčenou/vrácenou
                System.out.println("Zadejte název knihy:");
                String zmenitStavNazev = scanner.nextLine();
                
                // Zkontrolovat, zda kniha existuje
                if (!knihovna.existujeKniha(zmenitStavNazev)) {
                    System.out.println("Kniha s tímto názvem neexistuje.");
                    break; // Ukončíme tento případ a vrátíme se zpět k výběru možností
                }
                
                System.out.println("Je kniha půjčená? (ano/ne):");
                String pujcenoStr = scanner.nextLine().toLowerCase();
                boolean pujceno;
                if (pujcenoStr.equals("ano")) {
                    pujceno = true;
                } else if (pujcenoStr.equals("ne")) {
                    pujceno = false;
                } else {
                    System.out.println("Neplatná odpověď. Zadejte 'ano' nebo 'ne'.");
                    break; // Ukončíme tento případ a vrátíme se zpět k výběru možností
                }
                
                knihovna.zmenStavKniha(zmenitStavNazev, !pujceno);
                break;

            case 5:
                // Vypsat všechny knihy v abecedním pořadí
                List<Kniha> abecedniSeznam = knihovna.getSeznamAbecedne();
                if (abecedniSeznam.isEmpty()) {
                    System.out.println("Knihovna je prázdná.");
                } else {
                    System.out.println("Všechny knihy v abecedním pořadí:");
                    for (Kniha kniha : abecedniSeznam) {
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
                        System.out.println();
                    }
                }
                break;
            case 6:
                // Hledat knihu
                System.out.println("Zadejte název knihy:");
                String hledatNazev = scanner.nextLine();
                knihovna.hledejKniha(hledatNazev);
                break;
            case 7:
                // Vypsat všechny knihy od autora
                System.out.println("Zadejte jméno autora:");
                String autorJmeno = scanner.nextLine();
                knihovna.vypisKnihyPodleAutora(autorJmeno);
                break;
            case 8:
                // Vypsat všechny knihy podle žánru
                boolean validniZanr = false;
                do {
                    System.out.println("Zadejte žánr (Fantasy, Sci_fi, Detektivni, Romanticky, Horor):");
                    String zanrStr = scanner.nextLine().toLowerCase(); // Převedeme vstup na malá písmena a odstraníme mezery
                    try {
                        Zanr hledanyZanr = Zanr.valueOf(zanrStr.toUpperCase());
                        knihovna.vypisKnihyPodleZanru(zanrStr);
                        validniZanr = true; // Platný vstup, ukončíme smyčku
                    } catch (IllegalArgumentException e) {
                        System.out.println("Neplatný žánr. Zadejte znovu.");
                    }
                } while (!validniZanr);
                break;
            case 9:
                // Vypsat všechny vypůjčené knihy podle typu
                knihovna.vypisVypujceneKnihyPodleTypu();
                break;
            case 10:
                // Uložit knihu do souboru
                System.out.println("Zadejte název knihy, kterou chcete uložit do souboru:");
                String nazevUlozitDoSouboru = scanner.nextLine();
                knihovna.ulozKnihuDoSouboru(nazevUlozitDoSouboru);
                break;
            case 11:
                // Načíst knihu ze souboru
                System.out.println("Zadejte název souboru knihy k načtení:");
                String souborNazev = scanner.nextLine();
                Kniha nactenaKniha = knihovna.nactiKnihaZeSouboru(souborNazev);
                if (nactenaKniha != null) {
                    System.out.println("Kniha úspěšně načtena: " + nactenaKniha);
                } else {
                    System.out.println("Nepodařilo se načíst knihu.");
                }
                break;
            case 12:
                knihovna.ulozDoSQLDatabaze(); // Zavolání metody pro uložení do SQL databáze
                System.out.println("Veškeré informace byly uloženy do SQL databáze.");
                break;
            case 13:
                // Načíst z SQL databáze
                knihovna.nactiZSQLDatabaze();
                break;
            case 14:
                // Ukončit
                beh = false;
                break;
            default:
                System.out.println("Neplatná volba. Zadejte číslo od 1 do 14.");
        }
    }
    scanner.close();
    }
}

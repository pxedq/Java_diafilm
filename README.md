# Java_diafilm
```
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Main {
    public class Diafilm {
        public String cim;
        public int ev;
        public int kocka;
        public String szines;

        public Diafilm(String sor) {
            String[] s = sor.split(";");
            cim = s[0];
            ev = Integer.parseInt(s[1]);
            kocka = Integer.parseInt(s[2]);
            szines = s[3];
        }
    }

    private ArrayList<Diafilm> diafilmek = new ArrayList<>();

    public Main() {
        // --- 0. feladat ---
        betolt("diafilm.csv");
        System.out.printf("0) %d diafilm adata beolvasva\n", diafilmek.size());

        int nemSzinesDb = 0;
        for (Diafilm d : diafilmek) {
            if (d.szines.equals("N")) nemSzinesDb++;
        }
        System.out.printf("   Közülük %d még fekete-fehér\n", nemSzinesDb);

        // --- 1. feladat ---
        Diafilm legkorabbi = diafilmek.getFirst();
        for (Diafilm d : diafilmek) {
            if (d.ev < legkorabbi.ev) legkorabbi = d;
        }
        System.out.printf("1) A legrégebbi diafilm: %s (%d)\n", legkorabbi.cim, legkorabbi.ev);

        System.out.println("   De ugyanebben az évben készült még:");
        for (Diafilm d : diafilmek) {
            if (legkorabbi.ev == d.ev && d != legkorabbi) {
                System.out.printf("   - %s (%d)\n", d.cim, d.ev);
            }
        }

        // --- 2. feladat ---
        double sumKetezerElottiKockak = 0;
        double sumKetezerUtaniKockak = 0;
        int ketezerElottiDb = 0;

        for (Diafilm d : diafilmek) {
            if (d.ev < 2000) {
                sumKetezerElottiKockak += d.kocka;
                ketezerElottiDb++;
            } else {
                sumKetezerUtaniKockak += d.kocka;
            }
        }
        System.out.printf("2) A 2000 előtt készült diafilmek átlagos kockaszáma: %.1f\n", sumKetezerElottiKockak/ketezerElottiDb);
        System.out.printf("   A később készült diafilmeknél az áltag: %.1f\n", sumKetezerUtaniKockak/(diafilmek.size()-ketezerElottiDb));

        // --- 3. feladat ---
        TreeMap<Integer, Integer> evDarab = new TreeMap<>();
        for (Diafilm f : diafilmek) {
            int evtized = f.ev / 10;
            if (!evDarab.containsKey(evtized)) {
                evDarab.put(evtized, 1);
            }
            else {
                evDarab.put(evtized, evDarab.get(evtized)+1);
            }
        }
        System.out.printf("3) Az egyes évtizedekben készült diafilmek száma:\n");
        for (Integer evtized : evDarab.keySet()) {
            System.out.printf("   %d0-%d9 : %d darab\n", evtized, evtized, evDarab.get(evtized));
        }

        // --- 4. feladat ---
        TreeMap<Integer, Integer> eviKockak = new TreeMap<>();
        for (Diafilm d : diafilmek) {
            eviKockak.put(d.ev, eviKockak.getOrDefault(d.ev, 0) + d.kocka);
        }

        // Listába rendezzük az értékek (kockaszám) szerint csökkenőbe
        List<Map.Entry<Integer, Integer>> lista = new ArrayList<>(eviKockak.entrySet());
        lista.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        if (lista.size() >= 2) {
            System.out.printf("4) A legtöbb kocka (%d db) készítésének éve: %d\n", lista.get(0).getValue(), lista.get(0).getKey());
            System.out.printf("   A második legtöbb kocka (%d db) éve: %d\n", lista.get(1).getValue(), lista.get(1).getKey());
        }

        // --- 5. feladat ---
        PrintWriter ki = null;
        try {
            ki = new PrintWriter(new File("200x.txt"), "UTF-8");
            for (Diafilm d : diafilmek) {
                if (d.ev >= 2000 && d.ev <= 2009) {
                    ki.printf("%s;%d;%d;%s\n", d.cim, d.ev, d.kocka, d.szines);
                }
            }
            System.out.println("5) A 200x évben megjelent diák adatai elmentve (200x.txt)");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (ki != null) ki.close();
        }
    }

    private void betolt(String fajlnev) {
        Scanner be = null;
        try {
            be = new Scanner(new File(fajlnev), "UTF-8");
            be.nextLine();
            while (be.hasNextLine()) {
                diafilmek.add(new Diafilm(be.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (be != null) be.close();
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
```
### diafilm.csv
```
Cím;Év;Kocka;Színes
Bolondfalva;1958;27;I
Dia Dani űrutazása;1958;32;I
Odüsszeusz kalandjai;1958;60;I
Az égig érő fa;1962;38;I
Böbe baba és a horgászbot;2000;28;I
Egy ceruza kalandjai;1960;32;I
...
```
## Feladat
```
 A diafilm.csv fájl különböző diafilmek adatait tartalmazza, pontosvesszővel
 elválasztva, utf-8 kódolással. VIGYÁZAT, az első sor fejléc!
 Hozzunk létre egy Diafilm nevű projektet és oldjuk meg a következő feladatokat!

 0) Olvassuk be a fájl adatait egy megfelelő adatszerkezetbe,
    és jelenítsük meg a beolvasott adatok számát!.....................(2p)
    Számoljuk meg és írjuk ki a fekete-fehér diafilmek számát!........(1p)
 1) Írjuk ki a legrégebbi diafilm címét és készítésének évét!.........(1p)
    Írjuk ki mely diafilmek készültek még ugyanebben az évben,........(1p)
    a már kiírt diafilmen kívűl!......................................(1p)
 2) Határozzuk meg a 2000 előtti diafilmek átlagos kockaszámát!.......(1p)
    Írjuk ki ezt az átlagot a 2000 vagy utána készülteknél is!........(1p)
 3) Készísünk statisztikát az évtizedenkénti diafilmek számából!......(2p)
 4) Írjuk ki melyik évben készült a legtöbb kocka!....................(2p)
    Írjuk ki melyik évben készült a második legtöbb kocka!............(1p)
 5) Írjuk ki a 200x.txt fájlba a mintának megfelelően
    a 200x évtizedben készült diafilmek adatait!......................(2p)

 Minta:
 0) 705 diafilm adata beolvasva
    Közülük 141 még fekete-fehér
 1) A legrégebbi diafilm: A cár és a madár (1950)
    De ugyanebben az évben készült még:
    - Mese az aranykakasról (1950)
    - Buksi (1950)
    - Az öntelt veréb (1950)
 2) A 2000 előtt készült diafilmek átlagos kockaszáma: 38,7
    A később készült diafilmeknél az áltag: 29,4
 3) Az egyes évtizedekben készült diafilmek száma:
    1950-1959 : 254 darab
    1960-1969 : 126 darab
    1970-1979 : 118 darab
    1980-1989 : 181 darab
    1990-1999 : 15 darab
    2000-2009 : 6 darab
    2010-2019 : 5 darab
 4) A legtöbb kocka (3053 db) készítésének éve: 1957
    A második legtöbb kocka (2016 db) éve: 1958
 5) A 200x évben megjelent diák adatai elmentve (200x.txt)

 200x.txt:
 Böbe baba és a horgászbot;2000;28;I
 Két kis bocs meg a róka;2000;30;I
 Kismarkoló;2006;30;I
 Kisvonat a Mohavölgyben;2004;29;I
 Sün Balázs;2009;38;I
 A szépséges királykisasszony;2009;23;I
```

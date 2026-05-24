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
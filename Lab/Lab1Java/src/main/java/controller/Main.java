package controller;

import model.FamilyMember;
import model.FamilyMemberComparator;
import java.util.*;

public class Main {

    public static void main(String[] args){
        FamilyMember piotrek = new FamilyMember("Piotrek", 21, 1.8);
        FamilyMember kinga = new FamilyMember("Kinga", 19, 1.54);
        FamilyMember ania = new FamilyMember("Ania", 8, 1.2);
        FamilyMember alex = new FamilyMember("Alex", 1, 0.8);
        FamilyMember lee = new FamilyMember("Lee", 11, 1.3);
        FamilyMember adam = new FamilyMember("Adam", 80, 2.1);
        FamilyMember ewa = new FamilyMember("Ewa", 78,  1.8);
        FamilyMember bogdan = new FamilyMember("Bogdan", 20, 2.21);
        FamilyMember alicja = new FamilyMember("Alicja", 100, 2.0);
        FamilyMember burek = new FamilyMember("Burek", 1,0.25);

        FamilyMemberComparator comparator = new FamilyMemberComparator();

        alicja.addFamilyMember(adam);
        alicja.addFamilyMember(ewa);
        alicja.addFamilyMember(lee);

        piotrek.addFamilyMember(kinga);
        piotrek.addFamilyMember(ania);

        adam.addFamilyMember(alex);
        adam.addFamilyMember(piotrek);

        alex.addFamilyMember(burek);

        //args[1] = "sorted";

        Set<FamilyMember> familySet = null;

        if(args.length ==0){
            familySet = new HashSet<>();
        }else if(args[0].equals("sorted")){
            familySet = new TreeSet<>();
        }else if(args[0].equals("alt")){
            familySet = new TreeSet<>(comparator);
        }
        familySet.add(piotrek);
        familySet.add(ania);
        familySet.add(kinga);
        familySet.add(alex);
        familySet.add(lee);
        familySet.add(adam);
        familySet.add(ewa);
        familySet.add(bogdan);
        familySet.add(alicja);
        familySet.add(burek);
        //drukowanie wszystkich z uczniami (Każdy element tylko raz)
        System.out.println("rekurencyjna struktura");
        for (FamilyMember m : familySet){
            if(!m.hasParent(familySet)) {
                m.printMember();
            }
        }
        System.out.println();
        // drukowanie wszytkich bez czlonkow rodziny ( w kolejnosci sortowania)
        for(FamilyMember m : familySet){
            System.out.println(m);
        }
        System.out.println();
        Map<FamilyMember, Integer> statmap = getStatistics(familySet, args[0], comparator);
        for(FamilyMember m : statmap.keySet()){
            System.out.println(m + " -----> Liczba potomków: " + statmap.get(m));
        }


    }

    /**
     *
     * @param mageSet - zbiór obiektów Mage na którym wywołana ma być metoda
     * @param sorted - tryb sortowania: alt - alternatywna kolejnosc (comparator), sorted - naturalna kolejnosc (compareTo)
     * @param cmp - comparator
     * @return - hashMap w formie Mage: ilosc podwładnych
     */
    public static Map<FamilyMember, Integer> getStatistics(Set<FamilyMember> mageSet, String sorted, FamilyMemberComparator cmp){

        Map<FamilyMember, Integer> result = null;
        if(sorted.equals("alt")){
            result = new TreeMap<>(cmp);
        }else if (sorted.equals("Sorted")){
            result = new TreeMap<>();
        }else{
            result = new HashMap<>();
        }
        for(FamilyMember m : mageSet){
            int stat = m.countChildren();
            result.put(m,stat);
        }
        return result;
    }

}

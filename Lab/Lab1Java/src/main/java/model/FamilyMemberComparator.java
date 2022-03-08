package model;

import java.util.Comparator;

public class FamilyMemberComparator implements Comparator<FamilyMember> {
    /**
     *  name - > height -> age
     * @param m1
     * @param m2
     * @return
     */
    public int compare(FamilyMember m1, FamilyMember m2){

        int ageDifference = m1.getAge() - m2.getAge();
        int nameDifference = m1.getName().toLowerCase().compareTo(m2.getName().toLowerCase());
        double heightDifference = m1.getHeight() - m2.getHeight();

        if(nameDifference != 0){
            return nameDifference;
        }else if(heightDifference != 0){
            return (int)heightDifference;
        } else if (ageDifference != 0){
            return ageDifference;
        }else{
            return 0;
        }
    }
}

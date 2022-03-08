package model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FamilyMember implements Comparable<FamilyMember>{
    private String name;
    private int age;
    private double height;
    private Set<FamilyMember> children;

    public FamilyMember(String newName, int newAge, double newHeight){
        name = newName;
        age = newAge;
        height = newHeight;
        children = new HashSet<FamilyMember>();
    }
    public int compareTo(FamilyMember other){
        /**
         * age - > name ->height
         */
        int nameDifference = name.toLowerCase().compareTo(other.name.toLowerCase());
        int ageDifference = age - other.age;
        double heightDifference = height - other.height;

        if(ageDifference != 0){
            return ageDifference;
        }else if(nameDifference != 0){
            return nameDifference;
        }else if(heightDifference != 0){
            return (int)heightDifference;
        }else{
            return 0;
        }

    }
    public void addFamilyMember(FamilyMember newFamilyMember){
        children.add(newFamilyMember);
    }
    public Set<FamilyMember> getChildren(){
        return children;
    }
    public int getAge(){
        return age;
    }
    public double getHeight(){
        return height;
    }
    public boolean hasParent(Set<FamilyMember> family){
        for (FamilyMember m : family){
            for(FamilyMember a : m.getChildren()){
                if(a.equals(this)){
                    return true;
                }
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setChildren(Set<FamilyMember> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == this){
            return true;
        }
        if(this.getClass() != obj.getClass()){
            return false;
        }

        FamilyMember other = (FamilyMember)obj;

        if(other.height == height && other.name.equals(name) && other.age == age){
            /**
             * porównywanie zbiorów - jeżeli zawierają tych samych dzieci to True
             */
            if(other.children.containsAll(children)){
                return true;
            }
        }
        return false;
    }


    @Override
    public int hashCode() {
        int part_one = Objects.hash(name, age, height);
        int part_two =0;
        for(FamilyMember m : children){
            part_two += m.hashCode();
        }
        return part_one + part_two;
    }


    @Override
    public String toString(){
        return "FamilyMember: name - " + name + ", age - " + age + ", height - " + height;
    }
    public void printMember(){
        printMember(1);
    }

    public void printMember(int currentDepth){

        for(int i =0; i < currentDepth; i++)
            System.out.print("-");
        System.out.println(this);
        if(children.size() != 0){
            for(FamilyMember a : children) {
                a.printMember(currentDepth+1);
            }
        }
    }
    public int countChildren(){
        int result =0;
        for(FamilyMember a : children){
            result++;
            result += a.countChildren();
        }
        return result;
    }

}

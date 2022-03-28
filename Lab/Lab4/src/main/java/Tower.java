

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Towers")
public class Tower
{
    @Id
    @Column(name="name")
    private String name;

    @Column(name="height")
    private int height;

    @OneToMany(mappedBy="name")
    private List<Mage> mages;

    public Tower(){
        name = "";
        height =0;
        mages = new ArrayList<>();
    }

    public Tower(String name ,int height){
        this.name = name;
        this.height = height;
        mages = new ArrayList<>();
    }

    public void addMage(Mage mage){
        mages.add(mage);
    }

    public void removeMage(Mage mage){
        mages.remove(mage);
    }
    @Override
    public String toString() {
        String result = "Tower{" +
                "name='" + name + '\'' +
                ", height=" + height +
                "}, mages:\n";
        for(Mage m : mages){
            result += "\t"+ m.toString() + "\n";
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Mage> getMages() {
        return mages;
    }

    public void setMages(List<Mage> mages) {
        this.mages = mages;
    }
}

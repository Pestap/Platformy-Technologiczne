

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

    @OneToMany
    @Column(name="mages")
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

    @Override
    public String toString() {
        return "Tower{" +
                "name='" + name + '\'' +
                ", height=" + height +
                '}';
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

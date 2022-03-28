import javax.persistence.*;

@Entity
@Table(name="Mages")
public class Mage {

    @Id
    @Column(name="name")
    private String name;

    @Column(name="level")
    private int level;

    @ManyToOne
    private Tower tower;

    public Mage(String name, int level, Tower tower){
        this.name = name;
        this.level = level;
        this.tower = tower;

    }
    public Mage(){
        this.name = null;
        this.level =0;
        this.tower = null;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public Tower getTower(){
        return tower;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {

        String result = "Mage{'name='" + name + ", level="+level;

        if (tower == null){
            result += "}";
        }else{
            result += ", tower='"+tower.getName()+"'}";
        }
        return result;
    }
}

public class Mage {
    private int level;
    private String name;

    public Mage(String name, int level){
        this.level = level;
        this.name = name;
    }

    public int getLevel(){
        return level;
    }

    public String getName(){
        return name;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return "Mage: " + name + ", " + level;
    }

    @Override
    public boolean equals(Object other){
        if(other == this){
            return true;
        }

        if(other.getClass() != this.getClass()){
            return false;
        }

        if(this.name == ((Mage) other).name && this.level == ((Mage) other).level){
            return true;
        }
        return false;
    }


}

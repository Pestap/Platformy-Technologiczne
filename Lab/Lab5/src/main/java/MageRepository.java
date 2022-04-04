import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class MageRepository {
    private Collection<Mage> collection;

    public MageRepository(){
        this.collection = new ArrayList<>();
    }

    public Optional<Mage> find(String name){
        Optional<Mage> result = Optional.empty();
        for(Mage m : collection){
            if(m.getName() == name){
                result = Optional.of(m);
            }
        }
        return result;
    }

    public void delete(String name) throws IllegalArgumentException{
        Mage toDelete = null;
        for(Mage m : collection){
            if(m.getName() == name){
                toDelete = m;
                break;
            }
        }
        if(toDelete != null){
            collection.remove(toDelete);
        }else{
            throw new IllegalArgumentException("Nastąpiła próba usunięia nieistniejącego elementu.");
        }
    }

    public void save(Mage mage) throws IllegalArgumentException{
        boolean alreadyExists = false;
        for(Mage m : collection){
            if(m.getName().equals(mage.getName())){
                alreadyExists=true;
                break;
            }
        }
        if(!alreadyExists){
            collection.add(mage);
        }else{
            throw new IllegalArgumentException("Nastąpiła próba dodania elementu, który już istnieje.");
        }

    }

}

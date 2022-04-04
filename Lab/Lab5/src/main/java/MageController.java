import java.util.Optional;

public class MageController {
    private MageRepository repository;

    public MageController(MageRepository repository){
        this.repository = repository;
    }

    public String find(String name){
        Optional<Mage> result = repository.find(name);
        if(result.isEmpty()){
            return "Not found";
        }else{
            return result.get().toString();
        }
    }

    public String delete(String name){
        try{
            repository.delete(name);
            return "Done";
        }catch (IllegalArgumentException ex){
            return "Not found";
        }
    }

    public String save(String name, int level){
        try{
            repository.save(new Mage(name,level));
            return "Done";
        }catch (IllegalArgumentException ex){
            return "Bad request";
        }
    }

}

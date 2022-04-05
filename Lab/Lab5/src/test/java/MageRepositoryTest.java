import org.junit.Before;
import org.junit.Test;
import java.util.Optional;
import static org.junit.Assert.*;


public class MageRepositoryTest {
    MageRepository repository = null;
    @Before
    public void init(){
        repository = new MageRepository();
        repository.save(new Mage("Mage1", 10));
        repository.save(new Mage("Mage2", 15));
        repository.save(new Mage("Mage3", 20));
    }

    @Test(expected = Test.None.class)
    public void saveNonExistentTest(){
        repository.save(new Mage("Gandalf", 10));
        repository.save(new Mage("Dumbleadore",6));
        repository.save(new Mage("Merlin", 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveAlreadyExistentTest(){
        repository.save(new Mage("Gandalf", 10));
        repository.save(new Mage("Dumbleadore",6));
        repository.save(new Mage("Gandalf", 5));
    }

    @Test
    public void findExistentTest(){
        repository.save(new Mage("Gandalf", 10));
        Optional<Mage> result = repository.find("Gandalf");
        assertEquals(result.get().getName(),"Gandalf");
    }

    @Test
    public void findNonExistentTest(){
        Optional<Mage> result = repository.find("Non-exsistent");
        assertTrue(result.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteNonExistentTest(){
        repository.delete("Non-existent");
    }

    @Test(expected = Test.None.class)
    public void deleteExistaneTest(){
        repository.save(new Mage("Gandalf", 10));
        repository.delete("Gandalf");
    }

}
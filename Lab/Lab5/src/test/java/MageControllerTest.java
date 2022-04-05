import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.*;

public class MageControllerTest {
    MageController controller = null;
    MageRepository repository = null;
    @Before
    public void init() {

        repository = Mockito.mock(MageRepository.class);

        Mockito.when(repository.find("Non-existent")).thenReturn(Optional.empty());
        Mockito.when(repository.find("Existent")).thenReturn(Optional.of(new Mage("Existent", 10)));

        Mockito.doThrow(new IllegalArgumentException()).when(repository).save(new Mage("Existent", 10));
        Mockito.doNothing().when(repository).save(new Mage("Non-existent", 10));

        Mockito.doThrow(new IllegalArgumentException()).when(repository).delete("Non-existent");
        Mockito.doNothing().when(repository).delete("Existent");

        controller = new MageController(repository);

    }

    @Test
    public void deleteNonExistentTest(){
        String result = controller.delete("Non-existent");
        assertEquals(result, "Not found");
    }

    @Test
    public void deleteExistentTest(){
        String result = controller.delete("Exitent");
        assertEquals(result, "Done");
    }

    @Test
    public void findNonExistentTest(){
        String result = controller.find("Non-existent");
        assertEquals(result, "Not found");
    }

    @Test
    public void findExistentTest(){
        String result = controller.find("Existent");
        assertEquals(result, (new Mage("Existent", 10).toString()));
    }

    @Test
    public void saveNonExistentTest(){
        String result = controller.save("Non-existent", 10);
        assertEquals(result, "Done");
    }

    @Test
    public void saveExistentTest(){
        String result = controller.save("Existent", 10);
        assertEquals(result, "Bad request");
    }
}
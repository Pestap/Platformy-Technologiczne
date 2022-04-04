import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class MageControllerTest {
    MageController controller = null;
    @Before
    public void init() {
        controller = Mockito.mock(MageController.class);
        Mockito.when(controller.delete("Gandalf")).thenReturn("Done");
        Mockito.when(controller.delete("Non-existent")).thenReturn("Not found");
        Mockito.when(controller.find("Non-existent")).thenReturn("Not found");
        Mockito.when(controller.find("Merlin")).thenReturn((new Mage("Merlin", 10)).toString());
        Mockito.when(controller.save("Merlin", 10)).thenReturn("Done");
        Mockito.when(controller.save("Existent", 10)).thenReturn("Bad request");
    }

    @Test
    public void deleteNonExistentTest(){
        String result = controller.delete("Non-existent");
        assertEquals(result, "Not found");
    }

    @Test
    public void deleteExistentTest(){
        String result = controller.delete("Gandalf");
        assertEquals(result, "Done");
    }

    @Test
    public void findNonExistentTest(){
        String result = controller.find("Non-existent");
        assertEquals(result, "Not found");
    }

    @Test
    public void findExistentTest(){
        String result = controller.find("Merlin");
        assertEquals(result, (new Mage("Merlin", 10).toString()));
    }

    @Test
    public void saveNonExistentTest(){
        String result = controller.save("Merlin", 10);
        assertEquals(result, "Done");
    }

    @Test
    public void saveExistentTest(){
        String result = controller.save("Existent", 10);
        assertEquals(result, "Bad request");
    }
}
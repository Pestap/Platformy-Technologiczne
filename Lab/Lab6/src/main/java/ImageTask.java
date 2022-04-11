import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Stream;

public class ImageTask extends RecursiveAction {
    private Stream<Pair<String, BufferedImage>> imagePair;

    public ImageTask(Stream<Pair<String, BufferedImage>> imagePair){
        this.imagePair = imagePair;
    }
    @Override
    public void compute(){

    }
}

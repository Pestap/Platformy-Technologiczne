import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveTask;

public class ImageTask extends RecursiveTask<Pair<String, BufferedImage>> {
    private Pair<String, BufferedImage> imagePair;

    public ImageTask(Pair<String, BufferedImage> imagePair){
        this.imagePair = imagePair;
    }
    @Override
    public Pair<String, BufferedImage> compute(){
        BufferedImage image = imagePair.getRight();
        String name = imagePair.getLeft();

        for(int i =0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int rgb = image.getRGB(i, j);

                Color color = new Color(rgb);
                int red = color.getRed();
                int blue = color.getBlue();
                int green = color.getGreen();
                Color outColor = new Color(red, blue, green);
                int outRgb = outColor.getRGB();
                image.setRGB(i, j, outRgb);
            }
        }
        Pair<String, BufferedImage> pairOut = Pair.of(name, image);
        return pairOut;
    }
}

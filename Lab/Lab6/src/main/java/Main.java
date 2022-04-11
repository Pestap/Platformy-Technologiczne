import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        List<Path> files = null;
        Path source = Path.of("Photos");
        try (Stream<Path> stream = Files.list(source)) {
            files = stream.collect(Collectors.toList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        Stream<Path> pathStream = files.stream();

        Stream<Pair<String, BufferedImage>> imageStream = pathStream.map(path ->{
            String name = path.getFileName().toString();
            BufferedImage img = null;
            try {
                img = ImageIO.read(path.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Pair<String, BufferedImage> pair = Pair.of(name,img);
            return pair;
        });

        List<Pair<String, BufferedImage>> list = imageStream.toList();
        Stream<Pair<String, BufferedImage>> outputStream = imageStream.map(pair->{
           String name = pair.getLeft();
           BufferedImage img = pair.getRight();
           for(int i =0; i < img.getHeight(); i++){
               for (int j = 0; j < img.getWidth(); j++) {
                   int rgb = img.getRGB(i, j);
                   Color color = new Color(rgb);
                   int red = color.getRed();
                   int blue = color.getBlue();
                   int green = color.getGreen();
                   Color outColor = new Color(red, blue, green);
                   int outRgb = outColor.getRGB();
                   img.setRGB(i, j, outRgb);
               }
           }
            try {
                ImageIO.write(img, "jpg", new File("Output\\" + name));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Pair<String, BufferedImage> out = Pair.of(name, img);
            return out;
        });

        List<Pair<String, BufferedImage>> list2 = outputStream.toList();
        /*outputStream.forEach(pair->{
            BufferedImage img = pair.getRight();
            String name = pair.getLeft();
            try {
                ImageIO.write(img, "jpg", new File("Output\\" + name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/





    }

}

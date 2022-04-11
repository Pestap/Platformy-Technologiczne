import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {


        //====================================================================
        //strumien sekwencyjnie
        //====================================================================
        long time = System.currentTimeMillis();

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

        imageStream.forEach(pair ->{
            String name = pair.getLeft();
            BufferedImage img = pair.getRight();

            transformImage(img);

            File output = new File("Output\\"+name);
            try {
                ImageIO.write(img, "jpg", output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        long timeElapsed = System.currentTimeMillis() - time;

        System.out.println("Czas trwania: " + timeElapsed/1000.0 + " sekund");

        //====================================================================
        //strumien równoległy
        //====================================================================


        time = System.currentTimeMillis();

        List<Path> filesParallel = null;
        Path sourceParallel = Path.of("Photos");
        try (Stream<Path> stream = Files.list(source)) {
            filesParallel = stream.collect(Collectors.toList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //System.out.println(Runtime.getRuntime().availableProcessors());
        ForkJoinPool own = new ForkJoinPool(6000);

        Stream<Path> pathStreamParalel = filesParallel.stream().parallel();
        try {
            own.submit(() -> pathStreamParalel.map(path ->{
                String name = path.getFileName().toString();
                BufferedImage img = null;
                try {
                    img = ImageIO.read(path.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Pair<String, BufferedImage> pair = Pair.of(name,img);
                return pair;
            }).forEach(pair-> {
                String name = pair.getLeft();
                BufferedImage img = pair.getRight();

                transformImage(img);

                File output = new File("OutputP\\"+name);
                try {
                    ImageIO.write(img, "jpg", output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            })).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        own.shutdown();

        long timeElapsedParallel = System.currentTimeMillis() - time;
        System.out.println("Czas trwania: " + timeElapsedParallel/1000.0 + " sekund");

    }

    public static void transformImage(BufferedImage img){
        for(int i =0; i < img.getWidth();i++ ){
            for(int j =0; j < img.getHeight(); j++){
                int rgb = img.getRGB(i, j);
                Color color = new Color(rgb);
                int red = color.getRed();
                int blue = color.getBlue();
                int green = color.getGreen();
                Color outColor = new Color(red, blue, green);
                int outRgb = outColor.getRGB();
                img.setRGB(i,j,outRgb);
            }
        }
    }

}

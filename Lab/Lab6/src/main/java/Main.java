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


        files.stream()
                .map(path -> readImage(path))
                .map(pair -> transformImage(pair))
                .forEach(pair -> saveImage(pair, "Output"));

        long timeElapsed = System.currentTimeMillis() - time;

        System.out.println("Czas trwania: " + timeElapsed/1000.0 + " sekund");

        //====================================================================
        //strumien równoległy
        //====================================================================


        time = System.currentTimeMillis();

        List<Path> filesParallel = null;
        Path sourceParallel = Path.of("Photos");
        try (Stream<Path> stream = Files.list(sourceParallel)) {
            filesParallel = stream.collect(Collectors.toList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //System.out.println(Runtime.getRuntime().availableProcessors());
        ForkJoinPool own = new ForkJoinPool(6);

        //Stream<Path> pathStreamParalel = filesParallel.stream().parallel();
        try {
            List<Path> finalFilesParallel = filesParallel;
            own.submit(() -> finalFilesParallel.stream().parallel()
                    .map(path -> readImage(path))
                    .map(pair -> transformImage(pair))
                    .forEach(pair-> saveImage(pair, "OutputP") )).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        own.shutdown();

        long timeElapsedParallel = System.currentTimeMillis() - time;
        System.out.println("Czas trwania: " + timeElapsedParallel/1000.0 + " sekund");

    }

    public static Pair<String, BufferedImage> readImage(Path path){
        String name = path.getFileName().toString();
        BufferedImage img = null;
        try {
            img = ImageIO.read(path.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pair<String, BufferedImage> pair = Pair.of(name,img);
        return pair;
    }

    public static Pair<String, BufferedImage> transformImage(Pair<String, BufferedImage> pair){
        BufferedImage img = pair.getRight();
        String name = pair.getLeft();
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

        Pair<String, BufferedImage> result = Pair.of(name, img);
        return result;
    }

    public static void saveImage(Pair<String, BufferedImage> pair, String folderName){
        String name = pair.getLeft();
        BufferedImage img = pair.getRight();
        File output = new File(folderName + "\\" + name);
        try {
            ImageIO.write(img, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package sarxosexamples;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;


public class GetBitmapFromCamera {

        public static void main(String[] args) throws IOException {
                Webcam webcam = Webcam.getDefault();
                webcam.setViewSize(new Dimension(1024, 768));
                webcam.open();
                ImageIO.write(webcam.getImage(), "JPG", new File(System.currentTimeMillis() + ".jpg"));
                webcam.close();
        }
}
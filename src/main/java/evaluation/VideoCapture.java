package evaluation;

import com.github.sarxos.webcam.Webcam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Closeable;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 26/12/13
 * Time: 16:00
 * To change this template use File | Settings | File Templates.
 */
public class VideoCapture implements Closeable{

    private Webcam _webcam;

    public interface ImageCapturedListener {
        void imageCaptured(Image i);
    }

    public  VideoCaptureThread startVideoCapture(ImageCapturedListener l){
        VideoCaptureThread ret = new VideoCaptureThread(l);
        ret.start();
        return ret;
    }

    private static int _seq = 0;

    private class VideoCaptureThread extends Thread{
        private final ImageCapturedListener _listener;
        private boolean _stopASAP;

        public VideoCaptureThread( ImageCapturedListener listener ){
            _listener = listener;
            setName( "VideoCaptureThread" + _seq );
            setPriority(Thread.MIN_PRIORITY);
            _seq++;
        }

        public void stopASAP(){
            log( "please stop ASAP");
            _stopASAP = true;
        }

        @Override
        public void run() {
            while( !_stopASAP ){
                Image i = captureImage();
                _listener.imageCaptured(i);
            }
            log( "end of run");
        }
    }

    private static void log(String s) {
        System.out.println( s );
    }

    public Webcam webcam(){
        if( _webcam == null ){
            _webcam = Webcam.getDefault();
            _webcam.setViewSize(new Dimension(1024, 768));
            _webcam.open();
        }
        return _webcam;
    }

    public Image captureImage(){
        return webcam().getImage();
    }

    @Override
    public void close(){
        if( _webcam != null ){
            _webcam.close();
        }
        _webcam = null;
    }

    public static void main( String[] args ){
        final JFrame frame = new JFrame( "Video" );

        final VideoCapture nvc = new VideoCapture();
        ImageCapturedListener listener = new ImageCapturedListener(){
            @Override
            public void imageCaptured(final Image i) {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            Container p = frame.getContentPane();
                            Dimension size = p.getSize();
                            Graphics g2d = p.getGraphics();
                            g2d.drawImage(i, 0, 0, size.width, size.height, null);
                            g2d.drawLine(0,0,size.width,size.height);
                            g2d.dispose();
                            log("image drawn");
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        };

        frame.setSize(200,200);
        frame.setVisible(true);
        final VideoCaptureThread vct = nvc.startVideoCapture(listener);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                vct.stopASAP();
                frame.setVisible(false);
                frame.dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                nvc.close();
                System.exit(0);
            }
        });
    }
}

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class HelloCV {
        JFrame frame;
        JLabel label;
        
        public HelloCV() {
            frame = new JFrame();
            label = new JLabel();
        }
    
        public static Image toBufferedImage(Mat m) {
            int type = BufferedImage.TYPE_BYTE_GRAY;
            if ( m.channels() > 1 ) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }
            int bufferSize = m.channels()*m.cols()*m.rows();
            byte [] b = new byte[bufferSize];
            m.get(0,0,b); 
            BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
            final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(b, 0, targetPixels, 0, b.length);  
            return image;
        }
        
        public void init() {
            this.frame.setLayout(new FlowLayout());
            this.frame.setSize(1000,1000);
            this.frame.setVisible(true);
            this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.frame.setContentPane(label);
        }
        
        public void putFrame(ImageIcon icon) {
            this.label.setIcon(icon);
            this.label.repaint();
        }
        
        public static void main(String[] args){
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                
                HelloCV helloCV = new HelloCV();
                helloCV.init();
                
                VideoCapture videoCapture = new VideoCapture(0);
                Mat mat = new Mat();
                int framecount = 0;
                
                while(true) {
                    boolean success = videoCapture.read(mat);
                    framecount ++;
                    System.out.printf("frame: %d", framecount);
                    if (success != true)
                    {   
                        break;
                    }
                    Image image = toBufferedImage(mat);
                    ImageIcon icon=new ImageIcon(image);
                    helloCV.putFrame(icon);
                }
                videoCapture.release();
        }
}

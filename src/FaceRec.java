
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Johnny
 */
public class FaceRec {
    
    
    //Global variables
    JFrame mainFrame = new JFrame();
    JLabel camLabel = new JLabel();
    Mat out = new Mat();
    
    
    VideoCapture CamSource = null; 
    CascadeClassifier face= new CascadeClassifier(FaceRec.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
    CascadeClassifier eye = new CascadeClassifier(FaceRec.class.getResource("haarcascade_eye.xml").getPath().substring(1));

    MatOfRect faces = new MatOfRect();
    MatOfRect eyes = new MatOfRect();

    
    
    FaceRec(){
        
        
        CamSource = new VideoCapture(0);        //Webcam video source
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );   // load native opencv library
        
        //create GUI components to display face detections
        createFrame();
        
        
        while(true){
            
            
        if (CamSource.grab()) {
                        try {
                            CamSource.retrieve(out);
                           
                            face.detectMultiScale(out, faces);
                            eye.detectMultiScale(out, eyes);

                            
                            for (Rect rect : faces.toArray()) {
                               
                                Core.rectangle(out, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                                        new Scalar(0, 255,0));
                                
                            
                            }
                             for (Rect rect : eyes.toArray()) {
                               // System.out.println("ttt");
                                Core.rectangle(out, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                                        new Scalar(0, 255,0));
                                
                            
                            }
                            
                            
                            BufferedImage img = Mat2BufferedImage(out);
                            camLabel.setIcon(new ImageIcon(img));
                            mainFrame.pack();
                        } catch (Exception ex) {
                            System.out.println("Error");
                        }
                    }
        
        
        }
    }
    public void createFrame(){
        //Create and set up the window.
      
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      
      mainFrame.getContentPane().add(camLabel, BorderLayout.CENTER);
      
      //Display the window.
      mainFrame.pack();
      mainFrame.setVisible(true);
      
      //press Q to quit application
      mainFrame.addKeyListener(new KeyListener() {
    		public void keyPressed(KeyEvent arg0) {
    			if (arg0.getKeyCode() == KeyEvent.VK_Q)
    				System.exit(0);	
    		}
    		public void keyReleased(KeyEvent arg0) {	
    		}
    		public void keyTyped(KeyEvent arg0) {
    		}
      });
    }
     public static BufferedImage Mat2BufferedImage(Mat m)
   {
	// source: http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
	// Fastest code
	// The output can be assigned either to a BufferedImage or to an Image

	    int type = BufferedImage.TYPE_BYTE_GRAY;
	    if ( m.channels() > 1 ) {
	        type = BufferedImage.TYPE_3BYTE_BGR;
	    }
	    int bufferSize = m.channels()*m.cols()*m.rows();
	    byte [] b = new byte[bufferSize];
	    m.get(0,0,b); // get all the pixels
	    BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
	    final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	    System.arraycopy(b, 0, targetPixels, 0, b.length);  
	    return image;

	}

	
}



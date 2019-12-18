import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bird {
    private static AffineTransformOp op1;
    private static AffineTransformOp op2;
    private static AffineTransformOp op3;
    private static AffineTransformOp op4;
    private static AffineTransformOp op5;
    private static int counter;

    public static Image Bird1;
    public static Image Bird2;

    public static BufferedImage Bird1V2;
    public static BufferedImage Bird2V2;


    private static String Bird1Path = "./images/pixil-frame-0-7.png";
    private static String Bird2Path = "./images/pixil-frame-0-8.png";

    public static void SetupPlayer(){
        try {
            Bird1 = ImageIO.read(new File(Bird1Path));
            Bird2 = ImageIO.read(new File(Bird2Path));
            Bird2V2 = toBufferedImage(Bird2);
            Bird1V2 = toBufferedImage(Bird1);
        }
        catch (IOException e){
            System.out.print("Error 404: " +
                    "File not found");
        }
        double rotationRequired1 = Math.toRadians(-30);
        double rotationRequired2 = Math.toRadians(-15);
        double rotationRequired3 = Math.toRadians(15);
        double rotationRequired4 = Math.toRadians(30);
        double rotationRequired5 = Math.toRadians(60);
        double locationX = Bird1.getWidth(null) / 2;
        double locationY = Bird1.getHeight(null) / 2;
        AffineTransform tx1 = AffineTransform.getRotateInstance(rotationRequired1, locationX, locationY);
        op1 = new AffineTransformOp(tx1, AffineTransformOp.TYPE_BILINEAR);
        AffineTransform tx2 = AffineTransform.getRotateInstance(rotationRequired2, locationX, locationY);
        op2 = new AffineTransformOp(tx2, AffineTransformOp.TYPE_BILINEAR);
        AffineTransform tx3 = AffineTransform.getRotateInstance(rotationRequired3, locationX, locationY);
        op3 = new AffineTransformOp(tx3, AffineTransformOp.TYPE_BILINEAR);
        AffineTransform tx4 = AffineTransform.getRotateInstance(rotationRequired4, locationX, locationY);
        op4 = new AffineTransformOp(tx4, AffineTransformOp.TYPE_BILINEAR);
        AffineTransform tx5 = AffineTransform.getRotateInstance(rotationRequired5, locationX, locationY);
        op5 = new AffineTransformOp(tx5, AffineTransformOp.TYPE_BILINEAR);
    }

    public static void DrawBird(Graphics g, int x, int y, Control control){
        Image i;
        if (op1 == null){
            SetupPlayer();
        }
        if( control.state < - 0.5){
            if ((counter / 20) % 2 == 0) {
                if(control.state > 1){
                    i = op2.filter(Bird1V2,null);
                }
                else {
                    i = op1.filter(Bird1V2, null);
                }
            }
            else{
                if(control.state > 1){
                    i = op2.filter(Bird2V2,null);
                }
                else {
                    i = op1.filter(Bird2V2, null);
                }
            }
            counter++;
        }
        else if((control.state >= -0.5)&&(control.state <= 0.5)){
            i = Bird1;
        }
        else if((control.state > 0.5)&&(control.state <= 1)){
            i = op3.filter(Bird1V2,null);
        }
        else if((control.state > 1)&&(control.state <= 2)){
            i = op4.filter(Bird1V2,null);
        }
        else{
            i = op5.filter(Bird1V2,null);
        }
        g.drawImage(i, x,y,null);

    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }
}

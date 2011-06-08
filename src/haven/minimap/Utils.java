package haven.minimap;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import haven.Resource;
import haven.Tex;
import haven.TexI;

public class Utils {
    private static final int markersz = 14;
    
    public static BufferedImage generatemarkerimg(String resname) {
        BufferedImage orig = Resource.loadimg(resname);
        int w = orig.getWidth();
        int h = orig.getHeight();
        int sz = w > h ? w : h; // non-scaled size
        double scale = markersz < sz ? (double)markersz / sz : 1;
        int nsz = (int)(sz * scale); // new scaled size
        int nw = (int)(w * scale);
        int nh = (int)(h * scale);
        BufferedImage scaled = new BufferedImage(nsz + 4, nsz + 4, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // icon background
        g.setColor(Color.GRAY);
        g.fillOval(0, 0, scaled.getWidth() - 1, scaled.getHeight() - 1);
        g.setColor(Color.DARK_GRAY);
        g.drawOval(0, 0, scaled.getWidth() - 1, scaled.getHeight() - 1);
        // place herb icon in the center
        g.translate((scaled.getWidth() - nw) / 2, (scaled.getHeight() - nh) / 2);
        AffineTransform xform = AffineTransform.getScaleInstance(scale, scale);
        g.drawImage(orig, xform, null);
        g.dispose();
//        try {
//            javax.imageio.ImageIO.write(scaled, "PNG", new java.io.File(resname.replace("/", "_") + ".png"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return scaled;
    }
    
    public static Tex generatemarkertex(String resname) {
        return new TexI(generatemarkerimg(resname));
    }
}

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class ParticulaImage extends Particula {

	BufferedImage img = null;
	 
	public ParticulaImage(float X, float Y, int vel, double angulo, BufferedImage img,int tempoDeVida) {
		super(X, Y, vel, angulo, Color.white, tempoDeVida);
		
		this.img = img;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void DesenhaSe(Graphics2D dbg, int MapX, int MapY) {
		// TODO Auto-generated method stub
//		dbg.setColor(new Color(cor.getRed(),cor.getGreen(),cor.getBlue(),((255*(tempoDeVida-timerVida))/tempoDeVida)));
//		
//		dbg.fillRect((int)X-1-MapX, (int)Y-1-MapY, 3, 3);
		
		double prop = ((tempoDeVida-timerVida)-0.5f/(double)tempoDeVida);
		if(prop < 0 || prop > 1) prop = (double)0.5f;
		double propinv = 1.0-prop;
		double scale = 0.1+propinv*2;
		
		AffineTransform trans = dbg.getTransform();
		Composite comp = dbg.getComposite();
			
		
			dbg.translate(x-MapX, y-MapY);
			dbg.scale(scale, scale);
		
			dbg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)prop));
			
			dbg.drawImage(img, null, -(img.getWidth()/2), -(img.getHeight()/2));
		
			
		dbg.setComposite(comp);
		dbg.setTransform(trans);
	}

}

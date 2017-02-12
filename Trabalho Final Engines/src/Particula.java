import java.awt.Color;
import java.awt.Graphics2D;


public class Particula extends Sprite {

	int vel = 100;
	double angulo = 0;
	float velX = 0;
	float velY = 0;
	
	int raio = 1;
	
	int tempoDeVida = 100;
	int timerVida = 0;
	
	Color cor = Color.white;
	
	int corint = 0;
	double constvida = 0;
	
	public Particula(float X,float Y,int vel,double angulo, Color cor,int tempoDeVida) {
		super();
		
		this.vel = vel;
		this.raio = raio;
		this.angulo = angulo;
		this.x = X;
		this.y = Y;
		this.tempoDeVida = tempoDeVida;
		
		this.cor = cor;
		
		
		velX = (float)(vel*Math.cos(angulo));
		velY = (float)(vel*Math.sin(angulo));
		
		vivo = true;
		
		corint = cor.getRGB()&0x00ffffff;
		
		constvida = 1/(double)tempoDeVida;
		
	}

	@Override
	public void SimulaSe(int diftime) {
		// TODO Auto-generated method stub
		
		timerVida +=diftime;
		
		x+=velX*diftime/1000.0f;
		y+=velY*diftime/1000.0f;
		
		if(timerVida>tempoDeVida){
			vivo = false;
		}
	}

	@Override
	public void DesenhaSe(Graphics2D dbg, int MapX, int MapY) {
		// TODO Auto-generated method stub
//		int alpha = (int)((255*(tempoDeVida-timerVida))*constvida);
//		dbg.setColor(new Color(corint|alpha<<24,true));
		
		dbg.setColor(new Color(cor.getRed(),cor.getGreen(),cor.getBlue(),((255*(tempoDeVida-timerVida))/tempoDeVida)));
		
		dbg.fillRect((int)x-1-MapX, (int)y-1-MapY, 3, 3);
	}

	@Override
	public boolean mouseEstaEmCima(int mouseX, int mouseY) {
		return false;
	}

}

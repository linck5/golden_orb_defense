import java.awt.Color;
import java.awt.Graphics2D;


public class Projetil extends Sprite {
	
	int vel = 100;
	double angulo = 0;
	float velX = 0;
	float velY = 0;
	
	int raio = 1;
	
	int dano = 10;
	
	int alvoX, alvoY;
	
	float quantoJaAvancou = 0;
	float quantoTemQueAvancar = 0;
	
	Object pai = null;
	
	public Projetil(float X,float Y,int vel,double angulo, int raio, int dano,Object pai) {
		super();
		
		this.vel = vel;
		this.raio = raio;
		this.dano = dano;
		this.angulo = angulo;
		this.x = X;
		this.y = Y;
		
		this.pai = pai;
		
		velX = (float)(vel*Math.cos(angulo));
		velY = (float)(vel*Math.sin(angulo));
		
		vivo = true;
	}
	
	public Projetil(float X,float Y,int vel,double angulo, int raio, int dano,Object pai, int mouseX, int mouseY) {
		super();
		
		this.vel = vel;
		this.raio = raio;
		this.dano = dano;
		this.angulo = angulo;
		this.x = X;
		this.y = Y;
		
		this.pai = pai;
		
		velX = (float)(vel*Math.cos(angulo));
		velY = (float)(vel*Math.sin(angulo));
		
		alvoX = mouseX; 
		alvoY = mouseY;
		
		quantoTemQueAvancar = Math.abs(alvoX - x + GamePanel.instance.mapa.MapX) + Math.abs(alvoY - y + GamePanel.instance.mapa.MapY);
		
		vivo = true;
	}

	@Override
	public void SimulaSe(int diftime) {
		// TODO Auto-generated method stub
		float xold = x;
		float yold = y;
		
		x+=velX*diftime/1000.0f;
		y+=velY*diftime/1000.0f;
		
		quantoJaAvancou += Math.abs(velX*diftime/1000.0f) + Math.abs(velY*diftime/1000.0f);
		
		if(GamePanel.instance.pers == pai){
			boolean passouEmX = false, passouEmY = false;
			
			if(velX > 0){
				passouEmX = alvoX < x;
			}
			else{
				passouEmX = alvoX >= x;
			}
			if(velY > 0){
				passouEmY = alvoY + GamePanel.instance.mapa.MapY < y;
			}
			else{
				passouEmY = alvoY + GamePanel.instance.mapa.MapY >= y;
			}
			
			vivo = !(passouEmX && passouEmY);
		}
		
		int wmundo = (GamePanel.instance.mapa.Largura*16)-2;
		int hmundo = (GamePanel.instance.mapa.Altura*16)-2;
		
		if(x<0){
			x = 0;
			vivo = false;
		}
		if(x>wmundo){
			x = wmundo;
			vivo = false;
		}
		
		if(y<0){
			y = 0;
			vivo = false;
		}
		if(y>hmundo){
			y = hmundo;
			vivo = false;
		}
		
		
		double arcang = Math.PI/8;
		
		if(quantoJaAvancou > 35){
			Particula part = new ParticulaImage(
				(x+raio/2) - velX/20,
				(y+raio/2) - velY/20,
				(int)((vel/2)*GamePanel.rnd.nextDouble()),
				angulo-Math.PI-arcang+GamePanel.rnd.nextDouble()*2*arcang,
				GamePanel.fumaca,
				200
			);
			GamePanel.instance.listaDeParticulas.add(part);
		}
		
		
		
		
		
		if(colidecenario()){
			x = xold;
			y = yold;
			vivo = false;
			
			//double arcang = Math.PI/8;
			
			for(int i = 0; i < 40; i++){
			//Particula part = new Particula(x, y,(int)((vel/2)*GamePanel.rnd.nextDouble()), angulo-Math.PI-arcang+GamePanel.rnd.nextDouble()*2*arcang, Color.white, 600);
				//Particula part = new ParticulaImage(x, y,(int)((vel/2)*GamePanel.rnd.nextDouble()), angulo-Math.PI-arcang+GamePanel.rnd.nextDouble()*2*arcang, GamePanel.fumaca, 600);
				//GamePanel.instance.listaDeParticulas.add(part);
			}
		}
		
		Personagem pers = null;
		if((pers=colidecircular())!=null && quantoJaAvancou > quantoTemQueAvancar*0.66f){
			x = xold;
			y = yold;
			
			pers.vida-=dano;
			if(pers.vida<=0){
				pers.vivo = false;
			}
			
			vivo = false;
		}
		
		if(pai == GamePanel.instance.pers && quantoJaAvancou > quantoTemQueAvancar) vivo = false;
	}

	@Override
	public void DesenhaSe(Graphics2D dbg, int MapX, int MapY) {
		// TODO Auto-generated method stub
		if(quantoJaAvancou > 20){
			
			float correcaoDeAltura = Math.abs((1 - quantoJaAvancou/quantoTemQueAvancar) * 15);
			System.out.println((int)Math.abs(((quantoJaAvancou/quantoTemQueAvancar) * 75) + 50));
			int alpha = (int)Math.abs(((quantoJaAvancou/quantoTemQueAvancar) * 74) + 50);
			if(alpha > 255) alpha = 255;
			dbg.setColor(new Color(20,20,20,alpha));
			dbg.fillOval(
				(int)x - GamePanel.instance.mapa.MapX, 
				(int)y + (int)(85 * Math.abs(( 1 - quantoJaAvancou/quantoTemQueAvancar)) - (int)(correcaoDeAltura)) - GamePanel.instance.mapa.MapY, 
				10, 
				10
			);
			
			dbg.setColor(new Color(255, 255, 200, 90));
			dbg.fillOval((int)(x-velX/25-1-MapX), (int)(y-velY/25-1-MapY), raio, raio);
		
			dbg.setColor(new Color(255, 255, 150, 130));
			dbg.fillOval((int)(x-velX/50-1-MapX), (int)(y-velY/50-1-MapY), raio, raio);
			
			dbg.setColor(new Color(255, 255, 100, 170));
			dbg.fillOval((int)(x-velX/75-1-MapX), (int)(y-velY/75-1-MapY), raio, raio);
			
			dbg.setColor(new Color(255, 255, 50, 210));
			dbg.fillOval((int)(x-velX/100-1-MapX), (int)(y-velY/100-1-MapY), raio, raio);
			
			dbg.setColor(new Color(250, 250, 0, 255));
			dbg.fillOval((int)x-1-MapX, (int)y-1-MapY, raio, raio);
		}
	}
	
	public Personagem colidecircular(){
		for(int i = 0; i < GamePanel.instance.listaDePersonagens.size();i++){
			Personagem pers = (Personagem)GamePanel.instance.listaDePersonagens.get(i);
			
			if(pers!=pai){
				float difx = x-(pers.x+pers.charw/2);
				float dify = y-(pers.y+pers.charh/2);
				
				float r2 = (raio+pers.raio)*(raio+pers.raio);
				
				if(r2>(difx*difx+dify*dify)){
					return pers;
				}
			}
		}
		return null;
	}
	
	public boolean colidecenario(){
		int bx = ((int)(x))>>4;// /16
		int by = ((int)(y))>>4;// /16
		
		
		if(GamePanel.instance.mapa.mapa2[by][bx]>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseEstaEmCima(int mouseX, int mouseY) {
		return false;
	}

}

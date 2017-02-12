import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class Personagem extends Sprite implements Cloneable {


	int vel = 100;
	float velX = 0;
	float velY = 0;
	BufferedImage charset = null;
	
	int charw = 32;
	int charh = 48;
	
	int frame = 0;
	int frametimer = 0;
	int tempoentreframes = 200;
	
	int anim = 0;
	
	int tipo = 0;
	int charx = 0;
	int chary = 0;
	
	int raio = 8;
	
	int dinheiro = 0;
	
	int vidaMaxima = 500;
	int vida = vidaMaxima;
	
	int ataque = 1;
	
	int vidas = 5;
	
	float difXdaTorre, difYdaTorre;
	
	Rectangle boundingBox = new Rectangle();
	
	public Personagem() {
		vida = 999999;
	}
	
	public Personagem(float x,float y,BufferedImage charset,int tipo) {
		this.x = x;
		this.y = y;
		this.charset = charset;
		
		switch(tipo){
		case 1: 
			vidaMaxima = 1250; 
			break;
		case 2:
			vidaMaxima = 8000;
			vida = vidaMaxima;
			ataque = 6;
			break;
		case 3:
			vidaMaxima = 8000;
			vida = vidaMaxima;
			ataque = 6;
			break;
		}
		
		this.tipo = tipo;
		charx = (this.tipo%4)*(charw*3);
		chary = (this.tipo/4)*(charh*4);
		
		vivo = true;
	}

	@Override
	public void SimulaSe(int diftime) {
		
		if(this != GamePanel.instance.pers){
			if(vida <= 0){
				GamePanel.instance.pers.dinheiro += 45;
				if(tipo == 2 || tipo == 3){
					GerenciadorDeInimigos.inimigosAzuisMortos++;
				}
				vivo = false;
			}
			if(y + charh > GamePanel.instance.mapa.Altura * 16 - 2){
				GamePanel.instance.pers.vidas--;
				FeedbackHUD.show("Inimigo passou!  Vidas restantes: " + GamePanel.instance.pers.vidas, Color.RED);
				vivo = false;
			}
			
		}
		else{
			vida = 999999999;
			vivo = true;
		}
		
		frametimer+=diftime;
		
		float xold = x;
		float yold = y;
		
		
		x+=velX*diftime/1000.0f;
		y+=velY*diftime/1000.0f;
		
		
		int wmundo = (GamePanel.instance.mapa.Largura*16)-32;
		int hmundo = (GamePanel.instance.mapa.Altura*16)-48;
		
		if(x<0){
			x = 0;
			velX = -velX;
		}
		if(x>wmundo){
			x = wmundo;
			velX = -velX;
		}
		if(y<0){
			y = 0;
			velY = -velY;
		}
		if(y>hmundo){
			y = hmundo;
			velY = -velY;
		}
		
		if(colidecenario()){
			x = xold;
			y = yold;
			velX = -velX;
			velY = -velY;
		}
		
		
		if(colidecircular()){
			//x = xold;
			//y = yold;
		}
		if(this != GamePanel.instance.pers){
			for(Sprite b: GamePanel.instance.listaDeBarreiras){
				if(
					!((Barreira)b).posicionando &&
					x + charw > b.x &&
					x < b.x + ((Barreira)b).width &&
					y + charh >= b.y - 3 &&
					y <= b.y + ((Barreira)b).height
				){
					x = xold;
					y = yold;
					((Barreira)b).vida -= ataque;
				}
			}
			
			for(Sprite t: GamePanel.instance.listaDeTorres){
				if(
					!((Torre)t).posicionando &&
					x + charw > t.x &&
					x < t.x + ((Torre)t).width &&
					y + charh >= t.y - 3 &&
					y <= t.y + ((Torre)t).height
				){
					x = xold;
					y = yold;
					((Torre)t).vida -= ataque;
				}
			}
		}
		
		int yOffset = 3, xOffset = 8;
		boundingBox.x = (int)x + xOffset - GamePanel.instance.mapa.MapX;
		boundingBox.y = (int)y + yOffset - GamePanel.instance.mapa.MapY;
		boundingBox.width = charw - xOffset * 2;
		boundingBox.height = charh - yOffset;
		
		if(this != GamePanel.instance.pers){
			frame = (frametimer/tempoentreframes)%4;
			if(frame == 3) frame = 1;
		}
		else{
			frame = (frametimer/tempoentreframes)%3;
		}
	}

	@Override
	public void DesenhaSe(Graphics2D dbg,int MapX,int MapY){
		
		
		
		if(charx <= 2 && chary <=3){
			dbg.translate((charw + 9), 7);
			dbg.rotate(Math.toRadians(90),(int)(x-MapX) , (int)(y-MapY));
			
			dbg.drawImage(
				charset,(int)(x-MapX), (int)(y-MapY), (int)(x-MapX)+charw, (int)(y-MapY)+charh,
				charx+ frame*charw,chary+ anim*charh,charx+ frame*charw+charw, chary + anim*charh+charh, null
			);
			
			dbg.rotate(Math.toRadians(-90),(int)(x-MapX), (int)(y-MapY));
			dbg.translate(-(charw + 9), -7);
		}
		else{
			dbg.drawImage(charset,(int)(x-MapX), (int)(y-MapY), (int)(x-MapX)+charw, (int)(y-MapY)+charh,charx+ frame*charw,chary+ anim*charh,charx+ frame*charw+charw, chary + anim*charh+charh, null);
		}
		
		if(
			mouseEstaEmCima(
				GamePanel.instance.MouseX + GamePanel.instance.mapa.MapX, 
				GamePanel.instance.MouseY + GamePanel.instance.mapa.MapY
			) ||
			GamePanel.instance.mostrarInfo
		){
			if(this != GamePanel.instance.pers){
				dbg.setColor(Color.WHITE);
				dbg.fillRect((int)x - MapX, (int)y + charh + 5 - MapY, (int)(charw * ((float)vida / vidaMaxima)), 3);
			}
			
			//dbg.setColor(Color.red);
			//dbg.drawRect((int)boundingBox.x, (int)boundingBox.y, boundingBox.width, boundingBox.height);
		}
		
	}
	
	public boolean colidecircular(){
		for(int i = 0; i < GamePanel.instance.listaDePersonagens.size();i++){
			Personagem pers = (Personagem)GamePanel.instance.listaDePersonagens.get(i);
			
			if(pers!=this){
				float difx = (x+charw/2)-(pers.x+pers.charw/2);
				float dify = (y+charh/2)-(pers.y+pers.charh/2);
				
				float r2 = (raio+pers.raio)*(raio+pers.raio);
				
				if(r2>(difx*difx+dify*dify)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean colidecenario(){
		int bx = ((int)(x+16))>>4;// /16
		int by = ((int)(y+36))>>4;// /16
		
		
		if(GamePanel.instance.mapa.mapa2[by][bx]>0){
			return true;
		}
		return false;
	}
	
	@Override
	public Personagem clone() throws CloneNotSupportedException{
		return (Personagem)(super.clone());
	}

	@Override
	public boolean mouseEstaEmCima(int mouseX, int mouseY) {
		return
				mouseX > x &&
				mouseX < x + charw &&
				mouseY > y &&
				mouseY < y + charh;
	}

}

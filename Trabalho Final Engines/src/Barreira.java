import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class Barreira extends Sprite {
	
	public int
	width = 150,
	height = 25,
	vidaMaxima = 20000,
	vida = vidaMaxima,
	regen = 1,
	aumentoDeRegen = 2,
	aumentoDeVida = 20000,
	maxUpgrade = 5,
	upgrade = 1;
	
	public static int
	custo = 200,
	custoUpgrade = 300;
	
	public boolean
	posicionando = true,
	sobrepostoEmOutraEstrutura = false;
	
	BufferedImage
	barreira = GamePanel.instance.barreira1,
	barreiraQuebrada = GamePanel.instance.barreira2;
	
	public Rectangle boundingBox = new Rectangle((int)x, (int)y, width, height);
	
	public Barreira(){
		vivo = true;
	}

	@Override
	public void SimulaSe(int diftime) {
		
		if(vida <= 0) vivo = false;
		else if(vida <= vidaMaxima) vida += regen;
		
		if(posicionando){
			
			x = GamePanel.instance.MouseX + GamePanel.instance.mapa.MapX - width/2;
			y = GamePanel.instance.MouseY + GamePanel.instance.mapa.MapY - height/2;
			
			sobrepostoEmOutraEstrutura = false;
			for(Sprite b: GamePanel.instance.listaDeBarreiras){
				if(boundingBox.intersects(((Barreira)b).boundingBox) && (Barreira)b != this){
					sobrepostoEmOutraEstrutura = true;
				}
			}
			for(Sprite t: GamePanel.instance.listaDeTorres){
				if(boundingBox.intersects(((Torre)t).boundingBox)){
					sobrepostoEmOutraEstrutura = true;
				}
			}
					
			
		}else{
			
		}
		
		boundingBox.x = (int)x - GamePanel.instance.mapa.MapX;
		boundingBox.y = (int)y - GamePanel.instance.mapa.MapY;
		boundingBox.width = width;
		boundingBox.height = height;

	}

	@Override
	public void DesenhaSe(Graphics2D dbg, int MapX, int MapY) {
		Color c = dbg.getColor();
		Composite comp = dbg.getComposite();
		if(posicionando){
			if(sobrepostoEmOutraEstrutura){
				dbg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
				dbg.setColor(new Color(255,0,0,160));
				dbg.fillRect((int)x - MapX, (int)y - MapY, width, height);
			}
			else{
				dbg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
			}
		}
		
		//dbg.fillRect((int)x - MapX, (int)y - MapY, width, height);
		
		
		if(vida >= 10000){
			dbg.drawImage(
				barreira,
				(int)x - MapX, (int)y - MapY,
				(int)x - MapX + width, (int)y - MapY + height,
				0, 0,
				800, 150,
				null
			);
		}
		else{
			dbg.drawImage(
				barreiraQuebrada,
				(int)x - MapX, (int)y - MapY,
				(int)x - MapX + width, (int)y - MapY + height,
				0, 0,
				800, 150,
				null
			);
		}
		
		if(
			mouseEstaEmCima(
				GamePanel.instance.MouseX + GamePanel.instance.mapa.MapX, 
				GamePanel.instance.MouseY + GamePanel.instance.mapa.MapY
			) ||
			GamePanel.instance.mostrarInfo ||
			GamePanel.menuDeContexto.mouseEstaEmCima(
				GamePanel.instance.MouseX, 
				GamePanel.instance.MouseY
			)
		){
			dbg.setColor(Color.WHITE);
			dbg.fillRect((int)x - MapX, (int)y + height + 5 - MapY, (int)(width * ((float)vida / vidaMaxima)), 3);
			
			dbg.setColor(Color.WHITE);
			dbg.fillRect((int)x - MapX, (int)y + height + 5 - MapY, (int)(width * ((float)vida / vidaMaxima)), 3);
			dbg.drawString(upgrade < 5? "up "+upgrade: "max", (int)x - MapX, (int)y - 5 - MapY);
		}
		
		//dbg.setColor(Color.red);
		//dbg.drawRect((int)boundingBox.x, (int)boundingBox.y, boundingBox.width, boundingBox.height);
		dbg.setComposite(comp);
		dbg.setColor(c);
	}

	@Override
	public boolean mouseEstaEmCima(int mouseX, int mouseY) {
		return
			mouseX > x &&
			mouseX < x + width &&
			mouseY > y &&
			mouseY < y + height;
	}

}

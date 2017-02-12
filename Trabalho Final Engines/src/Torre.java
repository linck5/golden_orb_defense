import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;


public class Torre extends Sprite {
	
	public int
	width = 25,
	height = 25,
	vidaMaxima = 5000,
	vida = vidaMaxima,
	range = 200,
	dano = 35,
	timerTiro = 0,
	intervaloDeTiroInicial = 400,
	intervaloDeTiro = intervaloDeTiroInicial,
	velocidadeDoTiro = 200,
	upgrade = 1,
	maxUpgrade = 5,
	aumentoDeDano = 35,
	aumentoDeRange = 135,
	aumentoDeASPD = 150;
	
	public static int
	custo = 250,
	custoUpgrade = 400;
	
	public double
	angulo = 0;
	
	public boolean
	posicionando = true,
	sobrepostoEmOutraEstrutura = false;
	
	public Rectangle boundingBox = new Rectangle((int)x, (int)y, width, height);
	
	public Torre(){
		vivo = true;
	}

	@Override
	public void SimulaSe(int diftime) {
		
		if(vida <= 0) vivo = false;
		
		if(posicionando){
			
			x = GamePanel.instance.MouseX + GamePanel.instance.mapa.MapX - width/2;
			y = GamePanel.instance.MouseY + GamePanel.instance.mapa.MapY - height/2;
			
			sobrepostoEmOutraEstrutura = false;
			for(Sprite t: GamePanel.instance.listaDeTorres){
				if(boundingBox.intersects(((Torre)t).boundingBox) && (Torre)t != this){
					sobrepostoEmOutraEstrutura = true;
				}
			}
			for(Sprite b: GamePanel.instance.listaDeBarreiras){
				if(boundingBox.intersects(((Barreira)b).boundingBox)){
					sobrepostoEmOutraEstrutura = true;
				}
			}
					
			
		}else{
			
			Personagem personagemAoAlcanceComMenosHP = new Personagem();
			
			for(Sprite p: GamePanel.instance.listaDePersonagens){
				if(p == GamePanel.instance.pers) continue;
				float difx = ( x + width/2 ) - ( p.x + ((Personagem)p).charw/2 );
				float dify = ( y + height/2 ) - ( p.y + ((Personagem)p).charh/2 );
				
				float r2 = (range/2+((Personagem)p).raio)*(range/2+((Personagem)p).raio);
				
				if(r2>(difx*difx+dify*dify) && timerTiro >= intervaloDeTiro){
					
					if(((Personagem)p).vida < personagemAoAlcanceComMenosHP.vida){
						personagemAoAlcanceComMenosHP = (Personagem)p;
					}
					else if(
							((Personagem)p).vida == personagemAoAlcanceComMenosHP.vida &&
							difx + dify < personagemAoAlcanceComMenosHP.difXdaTorre + personagemAoAlcanceComMenosHP.difYdaTorre
					){
						personagemAoAlcanceComMenosHP = (Personagem)p;
					}
						
				}
			}
			
			if(personagemAoAlcanceComMenosHP.vida < 999999){
				
				timerTiro = 0;
				
				float dx = (personagemAoAlcanceComMenosHP.x + personagemAoAlcanceComMenosHP.charw/2) - (x + width/2);
				float dy = (personagemAoAlcanceComMenosHP.y + personagemAoAlcanceComMenosHP.charh - 5) - (y + height/2);
				
				angulo = Math.atan2(dy, dx);
				
				int raioDoProjetil = 10;
			
				Projetil proj = new Projetil(
					x + width/2 - raioDoProjetil/2, 	//x origem
					y + height/2 - raioDoProjetil/2,	//y origem
					velocidadeDoTiro,				//velocidade
					angulo,				//angulo radianos
					raioDoProjetil,				//raio do projetil
					dano,				//dano do projetil
					this			//pai do projetil
				);
				GamePanel.instance.listaDeProjeteis.add(proj);
			
			}
			
		}
		
		timerTiro += diftime;
		
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
		else{
			dbg.setColor(Color.GRAY);
		}
		
		//dbg.fillRect((int)x - MapX, (int)y - MapY, width, height);
		dbg.drawImage(
			GamePanel.instance.torreBase,
			(int)x - MapX, (int)y - MapY,
			(int)x - MapX + width, (int)y - MapY + height,
			0, 0,
			100, 100,
			null
		);
		dbg.rotate(angulo, x - MapX + width/2, y - MapY + width/2);
		dbg.drawImage(
			GamePanel.instance.torreArma1,
			(int)x - MapX, (int)y - MapY,
			(int)x - MapX + width, (int)y - MapY + height,
			0, 0,
			100, 100,
			null
		);
		dbg.rotate(-angulo, x - MapX + width/2, y - MapY + width/2);
		dbg.drawImage(
			GamePanel.instance.torreTopo,
			(int)x - MapX, (int)y - MapY,
			(int)x - MapX + width, (int)y - MapY + height,
			0, 0,
			100, 100,
			null
		);
		dbg.rotate(angulo, x - MapX + width/2, y - MapY + width/2);
		dbg.drawImage(
			GamePanel.instance.torreArma2,
			(int)x - MapX, (int)y - MapY,
			(int)x - MapX + width, (int)y - MapY + height,
			0, 0,
			100, 100,
			null
		);
		dbg.rotate(-angulo, x - MapX + width/2, y - MapY + width/2);
		
		
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
			dbg.drawOval((int)x - MapX - range/2 + width/2, (int)y - MapY - range/2 + height/2, range, range);
			
			dbg.setColor(Color.WHITE);
			dbg.fillRect((int)x - MapX, (int)y + height + 5 - MapY, (int)(width * ((float)vida / vidaMaxima)), 3);
			dbg.drawString(upgrade < 5? "up "+upgrade: "max", (int)x - MapX, (int)y - 5 - MapY);
		}
		
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
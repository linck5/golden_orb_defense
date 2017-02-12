import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class Mouse {
	
	public static int x, y, button;
	public static boolean pressing1, pressing2, pressing3;
	public static MouseMotionListener mml;
	public static MouseListener ml;
	
	public static void createListeners(){
		
		mml = new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {

				x = e.getX();
				y = e.getY();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {

				x = e.getX();
				y = e.getY();
			}
		};
		
		
		
		ml = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				switch(arg0.getButton()){
					case MouseEvent.BUTTON1: pressing1 = false; 
					for(Sprite b: GamePanel.menuDeContexto.listaDeBotoes){
						if(
							b.mouseEstaEmCima(
								GamePanel.instance.MouseX,
								GamePanel.instance.MouseY
							)
						){
							System.out.println(Math.random());
							GamePanel.instance.menuDeContexto.executaAcaoDoBotao(b);
						}
					}
					break;
					
					case MouseEvent.BUTTON2: pressing2 = false; break;
					
					case MouseEvent.BUTTON3:
						pressing3 = false; 
						
						int 
						mouseX = GamePanel.instance.MouseX,
						mouseY = GamePanel.instance.MouseY;
						
						for(Sprite t: GamePanel.instance.listaDeTorres){
							if(
								t.mouseEstaEmCima(
									mouseX + GamePanel.instance.mapa.MapX, 
									mouseY + GamePanel.instance.mapa.MapY
								)
							){
								GamePanel.menuDeContexto.estado = GamePanel.menuDeContexto.estado.TORRE;
								GamePanel.menuDeContexto.pai = t;
								Mouse.criaMenuDeContextoDentroDosLimites();
							}
						}
						for(Sprite b: GamePanel.instance.listaDeBarreiras){
							if(
								b.mouseEstaEmCima(
									mouseX + GamePanel.instance.mapa.MapX, 
									mouseY + GamePanel.instance.mapa.MapY
								)
							){
								GamePanel.menuDeContexto.estado = GamePanel.menuDeContexto.estado.BARREIRA;
								GamePanel.menuDeContexto.pai = b;
								Mouse.criaMenuDeContextoDentroDosLimites();
							}
						}
						
						break;
				}
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				switch(arg0.getButton()){
					case MouseEvent.BUTTON1: pressing1 = true; break;
					case MouseEvent.BUTTON2: pressing2 = true; break;
					case MouseEvent.BUTTON3: pressing3 = true; break;
				}
				

				for(Sprite b: GamePanel.instance.listaDeBarreiras){
					if(((Barreira)b).posicionando && !((Barreira)b).sobrepostoEmOutraEstrutura){
						for(Sprite p: GamePanel.instance.listaDePersonagens){
							if(((Personagem)p).boundingBox.intersects(((Barreira)b).boundingBox) && p != GamePanel.instance.pers){
								if(((Personagem)p).tipo == 2 || ((Personagem)p).tipo == 3){
									GerenciadorDeInimigos.inimigosAzuisMortos++;
								}
								((Personagem)p).vivo = false;
								GamePanel.instance.pers.dinheiro += 20;
							}
						}
						((Barreira)b).posicionando = false;
						GamePanel.instance.pers.dinheiro -= Barreira.custo;
						FeedbackHUD.show("Barreira adquirida. Dinheiro gasto:" + Barreira.custo, Color.YELLOW);
					}
				}
				for(Sprite t: GamePanel.instance.listaDeTorres){
					if(((Torre)t).posicionando && !((Torre)t).sobrepostoEmOutraEstrutura){
						for(Sprite p: GamePanel.instance.listaDePersonagens){
							if(((Personagem)p).boundingBox.intersects(((Torre)t).boundingBox) && p != GamePanel.instance.pers){
								if(((Personagem)p).tipo == 2 || ((Personagem)p).tipo == 3){
									GerenciadorDeInimigos.inimigosAzuisMortos++;
								}
								((Personagem)p).vivo = false;
								GamePanel.instance.pers.dinheiro += 20;
							}
						}
						((Torre)t).posicionando = false;
						GamePanel.instance.pers.dinheiro -= Torre.custo;
						FeedbackHUD.show("Torre adquirida. Dinheiro gasto:" + Torre.custo, Color.YELLOW);
					}
				}
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {

			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
		};
		
	}
	public static void criaMenuDeContextoDentroDosLimites(){
		int 
		offset = 5,
		mouseX = GamePanel.instance.MouseX,
		mouseY = GamePanel.instance.MouseY;
		
		if(mouseX < GamePanel.instance.PWIDTH - GamePanel.menuDeContexto.width){
			if(mouseY < GamePanel.instance.PHEIGHT - GamePanel.menuDeContexto.height){
				GamePanel.menuDeContexto.x = mouseX - offset;
				GamePanel.menuDeContexto.y = mouseY - offset;
			}
			else{
				GamePanel.menuDeContexto.x = mouseX - offset;
				GamePanel.menuDeContexto.y = mouseY - offset - GamePanel.menuDeContexto.height;
			}
		}
		else{
			if(mouseY < GamePanel.instance.PHEIGHT - GamePanel.menuDeContexto.height){
				GamePanel.menuDeContexto.x = mouseX - offset - GamePanel.menuDeContexto.width;
				GamePanel.menuDeContexto.y = mouseY - offset;
			}
			else{
				GamePanel.menuDeContexto.x = mouseX - offset - GamePanel.menuDeContexto.width;
				GamePanel.menuDeContexto.y = mouseY - offset - GamePanel.menuDeContexto.height;
			}
		}
	}
}

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;
import javax.swing.text.html.ListView;

import java.io.*;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Random;
import java.awt.image.*;

import javax.imageio.ImageIO;


public class GamePanel extends JPanel implements Runnable
{
	
public static GamePanel instance = null;
	
public final int PWIDTH = 640;
public final int PHEIGHT = 480;
private Thread animator;
private boolean running = false;
private boolean gameOver = false;
public boolean haAlgumaBarreiraSendoPosicionada = false;
public boolean haAlgumaTorreSendoPosicionada = false;
public boolean mostrarInfo = false;
public boolean mouseEstaEmCimaDeAlgumaEstrutura = false;

private BufferedImage dbImage;

public static BufferedImage fumaca = null;

private Graphics2D dbg;


int FPS,SFPS;
int fpscount;

public static Random rnd = new Random();

BufferedImage imagemcharsets;
BufferedImage imagefundo;
BufferedImage barreira1;
BufferedImage barreira2;
BufferedImage torreBase;
BufferedImage torreTopo;
BufferedImage torreArma1;
BufferedImage torreArma2;
BufferedImage moeda;
BufferedImage vida;
BufferedImage botaoComprar;
BufferedImage botaoComprarDesabilitado;
BufferedImage enemyKilledIco;

boolean LEFT, RIGHT,UP,DOWN,FIRE;


int MouseX,MouseY;

Personagem pers = null;

ArrayList<Sprite> listaDePersonagens = new ArrayList<Sprite>();

ArrayList<Sprite> listaDeProjeteis = new ArrayList<Sprite>();

ArrayList<Sprite> listaDeParticulas = new ArrayList<Sprite>();

ArrayList<Sprite> listaDeBarreiras = new ArrayList<Sprite>();

ArrayList<Sprite> listaDeTorres = new ArrayList<Sprite>();

ArrayList<Sprite> listaDeRemocaoDeSprites = new ArrayList<Sprite>();





TileMap mapa = null;

BufferedImage tileset = null;

public int intervaloDeTiro = 100;

public double erroDePrecisao = Math.PI/72;

public float velocidadeTiro = 500;

public static GerenciadorDeEventos eventos;

public static GerenciadorDeInimigos gerenciadorDeInimigos = new GerenciadorDeInimigos();

public static MenuDeContexto menuDeContexto = new MenuDeContexto();

public static FeedbackHUD feedbackHUD = new FeedbackHUD();

public GamePanel()
{
	instance = this;
	
	setBackground(Color.white);
	setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

	// create game components
	setFocusable(true);

	requestFocus(); // JPanel now receives key events
	
	Mouse.createListeners();
	addMouseMotionListener(Mouse.mml);
	addMouseListener(Mouse.ml);
	
	if (dbImage == null){
		dbImage = new BufferedImage(PWIDTH, PHEIGHT,BufferedImage.TYPE_INT_ARGB);
		if (dbImage == null) {
			System.out.println("dbImage is null");
			return;
		}else{
			dbg = (Graphics2D)dbImage.getGraphics();
		}
	}	
	
	
	// Adiciona um Key Listner
	addKeyListener( new KeyAdapter() {
		public void keyPressed(KeyEvent e)
			{ 
				int keyCode = e.getKeyCode();
				
				if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A){
					LEFT = true;
				}
				if(keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D){
					RIGHT = true;
				}
				if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W){
					UP = true;
				}
				if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S){
					DOWN = true;
				}
				if(keyCode == KeyEvent.VK_E){
					if(!haAlgumaBarreiraSendoPosicionada && !haAlgumaTorreSendoPosicionada)
						if(pers.dinheiro >= Barreira.custo){
							listaDeBarreiras.add(new Barreira());
						}
						else{
							feedbackHUD.show("Sem dinheiro. Custo: " + Barreira.custo, Color.RED);
						}
				}
				if(keyCode == KeyEvent.VK_Q){
					if(!haAlgumaTorreSendoPosicionada && !haAlgumaBarreiraSendoPosicionada){
						if(pers.dinheiro >= Torre.custo){
							listaDeTorres.add(new Torre());
						}
						else{
							feedbackHUD.show("Sem dinheiro. Custo: " + Torre.custo, Color.RED);
						}
					}
				}
				if(keyCode == KeyEvent.VK_R){
					mostrarInfo = true;
				}
				if(keyCode == KeyEvent.VK_M){
					pers.dinheiro += 200;
				}
				if(keyCode == KeyEvent.VK_ESCAPE){
					for(Sprite b: listaDeBarreiras)
						if(((Barreira)b).posicionando)
							listaDeRemocaoDeSprites.add(b);
					for(Sprite t: listaDeTorres)
						if(((Torre)t).posicionando)
							listaDeRemocaoDeSprites.add(t);
				}
			}
		@Override
			public void keyReleased(KeyEvent e ) {
				int keyCode = e.getKeyCode();
				
				if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A){
					LEFT = false;
				}
				if(keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D){
					RIGHT = false;
				}
				if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W){
					UP = false;
				}
				if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S){
					DOWN = false;
				}
				if(keyCode == KeyEvent.VK_R){
					mostrarInfo = false;
				}
				
			}
	});
	
	addMouseMotionListener(new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent e) {

			MouseX = e.getX();
			MouseY = e.getY();
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {

			MouseX = e.getX();
			MouseY = e.getY();
		}
	});
	
	
	
	addMouseListener(new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent arg0) {
			FIRE = false;
		}
		
		@Override
		public void mousePressed(MouseEvent arg0) {

			FIRE = true;
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
	});
	
	try {
		BufferedImage temp = ImageIO.read( getClass().getResource("Imagem 675.jpg") );
		imagefundo = new BufferedImage(temp.getWidth(),temp.getHeight(),BufferedImage.TYPE_INT_ARGB);
		imagefundo.getGraphics().drawImage(temp, 0, 0, null);
		
		
		imagemcharsets = ImageIO.read( getClass().getResource("teste.png") );
		
		tileset =  ImageIO.read( getClass().getResource("Bridge.png") );
		
		fumaca =  ImageIO.read( getClass().getResource("fumaca3.png") );
		
		
		temp = ImageIO.read( getClass().getResource("Barreira1.png") );
		barreira1 = new BufferedImage(temp.getWidth(),temp.getHeight(),BufferedImage.TYPE_INT_ARGB);
		barreira1.getGraphics().drawImage(temp, 0, 0, null);
		
		temp = ImageIO.read( getClass().getResource("Barreira2.png") );
		barreira2 = new BufferedImage(temp.getWidth(),temp.getHeight(),BufferedImage.TYPE_INT_ARGB);
		barreira2.getGraphics().drawImage(temp, 0, 0, null);
		
		temp = ImageIO.read( getClass().getResource("TorreArma1.png") );
		torreArma1 = new BufferedImage(temp.getWidth(),temp.getHeight(),BufferedImage.TYPE_INT_ARGB);
		torreArma1.getGraphics().drawImage(temp, 0, 0, null);
		
		temp = ImageIO.read( getClass().getResource("TorreArma2.png") );
		torreArma2 = new BufferedImage(temp.getWidth(),temp.getHeight(),BufferedImage.TYPE_INT_ARGB);
		torreArma2.getGraphics().drawImage(temp, 0, 0, null);
		
		temp = ImageIO.read( getClass().getResource("TorreBase.png") );
		torreBase = new BufferedImage(temp.getWidth(),temp.getHeight(),BufferedImage.TYPE_INT_ARGB);
		torreBase.getGraphics().drawImage(temp, 0, 0, null);
		
		temp = ImageIO.read( getClass().getResource("TorreTopo.png") );
		torreTopo = new BufferedImage(temp.getWidth(),temp.getHeight(),BufferedImage.TYPE_INT_ARGB);
		torreTopo.getGraphics().drawImage(temp, 0, 0, null);
		
		temp = ImageIO.read( getClass().getResource("moeda.png") );
		moeda = new BufferedImage(temp.getWidth(),temp.getHeight(),BufferedImage.TYPE_INT_ARGB);
		moeda.getGraphics().drawImage(temp, 0, 0, null);
		
		temp = ImageIO.read( getClass().getResource("vida.png") );
		vida = new BufferedImage(temp.getWidth(),temp.getHeight(),BufferedImage.TYPE_INT_ARGB);
		vida.getGraphics().drawImage(temp, 0, 0, null);
		
		temp = ImageIO.read( getClass().getResource("botaoComprar.png") );
		botaoComprar = new BufferedImage(temp.getWidth(),temp.getHeight(),BufferedImage.TYPE_INT_ARGB);
		botaoComprar.getGraphics().drawImage(temp, 0, 0, null);
		
		temp = ImageIO.read( getClass().getResource("botaoComprarDesabilitado.png") );
		botaoComprarDesabilitado = new BufferedImage(temp.getWidth(),temp.getHeight(),BufferedImage.TYPE_INT_ARGB);
		botaoComprarDesabilitado.getGraphics().drawImage(temp, 0, 0, null);
		
		temp = ImageIO.read( getClass().getResource("enemyKilledIco.png") );
		enemyKilledIco = new BufferedImage(temp.getWidth(),temp.getHeight(),BufferedImage.TYPE_INT_ARGB);
		enemyKilledIco.getGraphics().drawImage(temp, 0, 0, null);

		
		temp = null;
	}
	catch(IOException e) {
		System.out.println("Load Image error:");
	}

	 
	 mapa = new TileMap(tileset, 40, 30);
	 mapa.AbreMapa("mapa1.map");
	
	 pers = new Personagem(200,150,imagemcharsets,0);

	 listaDePersonagens.add(pers);
	
	 
	 eventos = new GerenciadorDeEventos(this.getClass().getResourceAsStream("/eventos.csv"));
 
//	 listaDePersonagens


	//System.out.println(""+imagefundo);//imagefundo.getType());

} // end of GamePanel()

public void addNotify()
{
	super.addNotify(); // creates the peer
	startGame(); // start the thread
}

private void startGame()
// initialise and start the thread
{
	if (animator == null || !running) {
		animator = new Thread(this);
		animator.start();
	}
} // end of startGame()

public void stopGame()
// called by the user to stop execution
{ running = false; }


public void run()
/* Repeatedly update, render, sleep */
{
	running = true;
	
	long DifTime,TempoAnterior;
	
	int segundo = 0;
	DifTime = 0;
	TempoAnterior = System.currentTimeMillis();
	
	while(running) {
	
		gameUpdate((int)DifTime); // game state is updated
		gameRender(); // render to a buffer
		paintImmediately(0, 0, 640, 480); // paint with the buffer
	
		try {
			Thread.sleep(0); // sleep a bit
		}	
		catch(InterruptedException ex){}
		
		DifTime = System.currentTimeMillis() - TempoAnterior;
		TempoAnterior = System.currentTimeMillis();
		
		if(segundo!=((int)(TempoAnterior/1000))){
			FPS = SFPS;
			SFPS = 1;
			segundo = ((int)(TempoAnterior/1000));
		}else{
			SFPS++;
		}
	
	}
System.exit(0); // so enclosing JFrame/JApplet exits
} // end of run()

int timerfps = 0;
int timerTiro = 0;
private void gameUpdate(int diftime)
{ 
	for(Sprite s: listaDeRemocaoDeSprites){
		for(Sprite p: listaDePersonagens) if(p == s){ listaDePersonagens.remove(p); break;}
		for(Sprite pr: listaDeProjeteis) if(pr == s){ listaDeProjeteis.remove(pr); break;}
		for(Sprite pa: listaDeParticulas) if(pa == s){ listaDeParticulas.remove(pa); break;}
		for(Sprite b: listaDeBarreiras) if(b == s){ listaDeBarreiras.remove(b); break;}
		for(Sprite t: listaDeTorres) if(t == s){ listaDeTorres.remove(t); break;}
	}
	listaDeRemocaoDeSprites.clear();
	
	
	mouseEstaEmCimaDeAlgumaEstrutura = false;
	for(Sprite t: listaDeTorres){
		if(t.mouseEstaEmCima(MouseX + mapa.MapX, MouseY + mapa.MapY)){
			mouseEstaEmCimaDeAlgumaEstrutura = true;
			break;
		}
	}
	for(Sprite b: listaDeBarreiras){
		if(b.mouseEstaEmCima(MouseX + mapa.MapX, MouseY + mapa.MapY)){
			mouseEstaEmCimaDeAlgumaEstrutura = true;
			break;
		}
	}
	
	
	
	timerTiro+=diftime;
	if(UP){
		pers.velY = -pers.vel;
		pers.anim = 3;
	}else if(DOWN){
		pers.velY = pers.vel;
		pers.anim = 0;
	}else{
		pers.velY = 0;
	}
	
	if(LEFT){
		pers.velX =-pers.vel;
		pers.anim = 1;
	}else if(RIGHT){
		pers.velX = pers.vel;
		pers.anim = 2;
	}else{
		pers.velX = 0;
	}
	

	
	if(FIRE&&timerTiro>intervaloDeTiro){
		//TODO
		float dx = (MouseX+mapa.MapX) - (pers.x+16);
		float dy = (MouseY+mapa.MapY) - (pers.y+24);
		
		Double ang = Math.atan2(dy, dx);
		ang = ang -erroDePrecisao + rnd.nextDouble()*2*erroDePrecisao;
		
		Projetil proj = new Projetil(pers.x+16, pers.y+24, (int)velocidadeTiro, ang, 10, 100,pers, MouseX, MouseY);
		listaDeProjeteis.add(proj);

		if(timerTiro>intervaloDeTiro*2){
			timerTiro=0;
		}else{
			timerTiro-=intervaloDeTiro;
		}
	}
	
//	pers.SimulaSe(diftime);

	
	for(int i = 0; i < listaDePersonagens.size();i++){
		Sprite pers = listaDePersonagens.get(i);
		pers.SimulaSe(diftime);
		if(pers.vivo==false){
			listaDePersonagens.remove(i);
			i--;
		}
	}
	
	for(int i = 0; i < listaDeProjeteis.size();i++){
		Sprite proj = listaDeProjeteis.get(i);
		proj.SimulaSe(diftime);
		if(proj.vivo==false){
			listaDeProjeteis.remove(i);
			i--;
		}
	}
	
	for(int i = 0; i < listaDeParticulas.size();i++){
		Sprite part = listaDeParticulas.get(i);
		part.SimulaSe(diftime);
		if(part.vivo==false){
			listaDeParticulas.remove(i);
			i--;
		}
	}
	
	haAlgumaBarreiraSendoPosicionada = false;
	for(int i = 0; i < listaDeBarreiras.size();i++){
		Sprite barr = listaDeBarreiras.get(i);
		barr.SimulaSe(diftime);
		if(((Barreira)barr).posicionando){
			haAlgumaBarreiraSendoPosicionada = true;
		}
		if(barr.vivo==false){
			listaDeBarreiras.remove(i);
			i--;
		}
	}
	
	haAlgumaTorreSendoPosicionada = false;
	for(int i = 0; i < listaDeTorres.size();i++){
		Sprite torr = listaDeTorres.get(i);
		torr.SimulaSe(diftime);
		if(((Torre)torr).posicionando){
			haAlgumaTorreSendoPosicionada = true;
		}
		if(torr.vivo==false){
			listaDeTorres.remove(i);
			i--;
		}
	}
	
	menuDeContexto.SimulaSe(diftime);
	feedbackHUD.SimulaSe(diftime);
	
	
	if(!mouseEstaEmCimaDeAlgumaEstrutura && !menuDeContexto.mouseEstaEmCima(MouseX, MouseY)){
		menuDeContexto.estado = menuDeContexto.estado.OCULTO;
	}

	eventos.simulaSe(diftime);
	gerenciadorDeInimigos.simulaSe(diftime);
	
	mapa.Posiciona((int)(pers.x-PWIDTH/2),(int)(pers.y-PHEIGHT/2));
	
}

private void gameRender()
// draw the current frame to an image buffer
{
	
	mapa.DesenhaSe(dbg);
	
	dbg.setColor(new Color(20,20,20,25));
	dbg.fillOval((int)pers.x + 7 - mapa.MapX, (int)pers.y + 85 - mapa.MapY, 23, 23);
	dbg.setColor(new Color(20,20,20,10));
	dbg.fillOval((int)pers.x + 10 - mapa.MapX, (int)pers.y + 88 - mapa.MapY, 17, 17);
	
	for(int i = 0; i < listaDeBarreiras.size();i++){
		listaDeBarreiras.get(i).DesenhaSe(dbg,mapa.MapX,mapa.MapY);
	}
	
	for(int i = 0; i < listaDeTorres.size();i++){
		listaDeTorres.get(i).DesenhaSe(dbg,mapa.MapX,mapa.MapY);
	}
	
	for(int i = 0; i < listaDePersonagens.size();i++){
		listaDePersonagens.get(i).DesenhaSe(dbg,mapa.MapX,mapa.MapY);
	}
	
	for(int i = 0; i < listaDeProjeteis.size();i++){
		listaDeProjeteis.get(i).DesenhaSe(dbg,mapa.MapX,mapa.MapY);
	}
	
	for(int i = 0; i < listaDeParticulas.size();i++){
		listaDeParticulas.get(i).DesenhaSe(dbg,mapa.MapX,mapa.MapY);
	}
	
	menuDeContexto.DesenhaSe(dbg, mapa.MapX, mapa.MapY);
	
	eventos.desenhaSe(dbg);
	
	pers.DesenhaSe(dbg,mapa.MapX,mapa.MapY);
	
	feedbackHUD.DesenhaSe(dbg, mapa.MapX, mapa.MapY);
	
	dbg.setColor(new Color(5,5,5,200));	
	//dbg.drawString("FPS: "+FPS+" MouseX: "+MouseX+" MouseY: "+MouseY, 10, 10);	
	//dbg.drawString("LEFT: "+LEFT+" RIGHT "+RIGHT+" UP "+UP+" DOWN "+DOWN, 10, 30);	
	
	dbg.drawImage(
		moeda,
		PWIDTH - 60, PHEIGHT - 30,
		PWIDTH - 60 + 18, PHEIGHT - 30 + 18,
		0, 0,
		20, 20,
		null
	);
	dbg.drawString("x"+pers.dinheiro, PWIDTH - 40, PHEIGHT - 15);
	
	dbg.drawImage(
		enemyKilledIco,
		PWIDTH - 55, 10,
		PWIDTH - 55 + 20, 10 + 20,
		0, 0,
		20, 20,
		null
	);
	dbg.drawString("x"+GerenciadorDeInimigos.inimigosAzuisMortos, PWIDTH - 33, 26);

	dbg.drawImage(
		vida,
		10, PHEIGHT - 30,
		10 + 18, PHEIGHT - 30 + 18,
		0, 0,
		20, 20,
		null
	);
	dbg.drawString("x"+pers.vidas, 30, PHEIGHT - 15);
	
	if(pers.vidas < 1){
		dbg.setColor(new Color(30,30,30,255));
		dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
		dbg.setColor(Color.ORANGE);
		dbg.drawString("GAME OVER", PWIDTH/2 - 30, PHEIGHT/2);
	}
	if(GerenciadorDeInimigos.inimigosAzuisMortos > 100){
		dbg.setColor(new Color(30,30,30,255));
		dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
		dbg.setColor(Color.ORANGE);
		dbg.drawString("VOCÊ GANHOU", PWIDTH/2 - 30, PHEIGHT/2);
	}
}


public void paintComponent(Graphics g)
{
	super.paintComponent(g);
	if (dbImage != null)
		g.drawImage(dbImage, 0, 0, null);
}


public static void main(String args[])
{
	GamePanel ttPanel = new GamePanel();

  // create a JFrame to hold the timer test JPanel
  JFrame app = new JFrame("Swing Timer Test");
  app.getContentPane().add(ttPanel, BorderLayout.CENTER);
  app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  app.pack();
  app.setResizable(false);  
  app.setVisible(true);
} // end of main()

} // end of GamePanel class


import java.awt.Color;
import java.awt.Graphics2D;


public class FeedbackHUD extends Sprite {
	
	public static int 
	timer = 0,
	tempoAparecendo = 3000;
	
	public static String mensagem = "";
	public static Color cor = Color.WHITE;
	

	@Override
	public void SimulaSe(int diftime) {
		
		if(!mensagem.equals("")){
			timer += diftime;
			if(timer >= tempoAparecendo){
				mensagem = "";
				timer = 0;
			}
		}

	}

	@Override
	public void DesenhaSe(Graphics2D dbg, int MapX, int MapY) {
		
		if(!mensagem.equals("")){
			dbg.setColor(new Color(160,160,160,180));
			dbg.fillRect(21, 12, 300, 16);
			dbg.setColor(cor);
			dbg.drawString(mensagem, 25, 25);
		}
	}

	@Override
	public boolean mouseEstaEmCima(int mouseX, int mouseY) {
		return false;
	}
	
	public static void show(String s, Color c){
		mensagem = s;
		cor = c;
	}

}

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;


public class MenuDeContexto extends Sprite {
	
	public enum Estado {
        OCULTO(0), TORRE(1), BARREIRA(2);
        private int value;

        private Estado(int value) {
            this.value = value;
        }
	};
	
	private static class Botao extends Sprite{
		
		public enum Acao {
			NENHUMA(0),
	        T_DANO(11), T_RANGE(12), T_ASPD(13),
	        B_REGEN(21), B_VIDA(22);
	        private int value;

	        private Acao(int value) {
	            this.value = value;
	        }
		};
		
		public static int
		width = 20,
		height = 20;
		
		public Acao acao = Acao.NENHUMA;
		
		public Botao(Acao a){
			acao = a;
		}

		@Override
		public void SimulaSe(int diftime) {}
		
		public void executaAcao(Sprite estrutura){
			switch(acao){
			case T_DANO:
				if(((Torre)estrutura).upgrade < ((Torre)estrutura).maxUpgrade){
					if(GamePanel.instance.pers.dinheiro >= Torre.custoUpgrade){
						GamePanel.instance.pers.dinheiro -= Torre.custoUpgrade;
						FeedbackHUD.show("Upgrade feito. Dinheiro gasto: " + Torre.custoUpgrade, Color.YELLOW);
						((Torre)estrutura).dano += ((Torre)estrutura).aumentoDeDano;
						((Torre)estrutura).upgrade++;
					}
					else{
						FeedbackHUD.show("Sem dinheiro. Custo: " + Torre.custoUpgrade, Color.RED);
					}
				}
				break;
			case T_RANGE:
				if(((Torre)estrutura).upgrade < ((Torre)estrutura).maxUpgrade){
					if(GamePanel.instance.pers.dinheiro >= Torre.custoUpgrade){
						GamePanel.instance.pers.dinheiro -= Torre.custoUpgrade;
						FeedbackHUD.show("Upgrade feito. Dinheiro gasto: " + Torre.custoUpgrade, Color.YELLOW);
						((Torre)estrutura).range += ((Torre)estrutura).aumentoDeRange;
						((Torre)estrutura).velocidadeDoTiro += ((Torre)estrutura).aumentoDeRange * 2.5;
						((Torre)estrutura).upgrade++;
					}
					else{
						FeedbackHUD.show("Sem dinheiro. Custo: " + Torre.custoUpgrade, Color.RED);
					}
				}
				break;
			case T_ASPD:
				if(((Torre)estrutura).upgrade < ((Torre)estrutura).maxUpgrade){
					if(GamePanel.instance.pers.dinheiro >= Torre.custoUpgrade){
						GamePanel.instance.pers.dinheiro -= Torre.custoUpgrade;
						FeedbackHUD.show("Upgrade feito. Dinheiro gasto: " + Torre.custoUpgrade, Color.YELLOW);
						((Torre)estrutura).intervaloDeTiro -= ((Torre)estrutura).aumentoDeASPD;
						((Torre)estrutura).upgrade++;
					}
					else{
						FeedbackHUD.show("Sem dinheiro. Custo: " + Torre.custoUpgrade, Color.RED);
					}
				}
				break;
			case B_REGEN:
				if(((Barreira)estrutura).upgrade < ((Barreira)estrutura).maxUpgrade){
					if(GamePanel.instance.pers.dinheiro >= Barreira.custoUpgrade){
						GamePanel.instance.pers.dinheiro -= Barreira.custoUpgrade;
						FeedbackHUD.show("Upgrade feito. Dinheiro gasto: " + Barreira.custoUpgrade, Color.YELLOW);
						((Barreira)estrutura).regen += ((Barreira)estrutura).aumentoDeRegen;
						((Barreira)estrutura).upgrade++;
					}
					else{
						FeedbackHUD.show("Sem dinheiro. Custo: " + Barreira.custoUpgrade, Color.RED);
					}
				}
				break;
			case B_VIDA:
				if(((Barreira)estrutura).upgrade < ((Barreira)estrutura).maxUpgrade){
					if(GamePanel.instance.pers.dinheiro >= Barreira.custoUpgrade){
						GamePanel.instance.pers.dinheiro -= Barreira.custoUpgrade;
						FeedbackHUD.show("Upgrade feito. Dinheiro gasto: " + Barreira.custoUpgrade, Color.YELLOW);
						((Barreira)estrutura).vidaMaxima += ((Barreira)estrutura).aumentoDeVida;
						((Barreira)estrutura).upgrade++;
					}
					else{
						FeedbackHUD.show("Sem dinheiro. Custo: " + Barreira.custoUpgrade, Color.RED);
					}
				}
				break;
			}
		}

		@Override
		public void DesenhaSe(Graphics2D dbg, int MapX, int MapY) {
			
			
			dbg.drawImage(
				MenuDeContexto.pai != null &&(
					MenuDeContexto.pai.getClass().getName().equals(Torre.class.getName()) &&
					((Torre)MenuDeContexto.pai).upgrade == ((Torre)MenuDeContexto.pai).maxUpgrade
					||
					MenuDeContexto.pai.getClass().getName().equals(Barreira.class.getName()) &&
					((Barreira)MenuDeContexto.pai).upgrade == ((Barreira)MenuDeContexto.pai).maxUpgrade
				)
				? GamePanel.instance.botaoComprarDesabilitado: GamePanel.instance.botaoComprar,
				(int)x,
				(int)y,
				(int)x + width,
				(int)y + height,
				0,
				0,
				20,
				20,
				null
			);
			
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
	
	public static int
	width = 125,
	height = 0;

	public static Sprite pai;
	public static Estado estado = Estado.OCULTO;
	public static ArrayList<Botao> listaDeBotoes = new ArrayList<Botao>();

	public Botao
	maisDano = new Botao(Botao.Acao.T_DANO),
	maisRange = new Botao(Botao.Acao.T_RANGE),
	maisASPD = new Botao(Botao.Acao.T_ASPD),
	maisHP = new Botao(Botao.Acao.B_VIDA),
	maisRegen = new Botao(Botao.Acao.B_REGEN);
	
	public MenuDeContexto(){
		
	}

	@Override
	public void SimulaSe(int diftime) {
		
		int 
		mapX = GamePanel.instance.mapa.MapX,
		mapY = GamePanel.instance.mapa.MapY;
		
		switch(estado){
		case OCULTO:
			height = 0;
			listaDeBotoes.clear();
			break;
		case TORRE:
			maisDano.x = x + width - maisDano.width - 2;
			maisDano.y = y + 2;
			maisRange.x = x + width - maisDano.width - 2;
			maisRange.y = y + maisRange.width + 4;
			maisASPD.x = x + width - maisDano.width - 2;
			maisASPD.y = y + (maisASPD.width * 2 + 6);
			
			if(listaDeBotoes.size() < 3){
				listaDeBotoes.clear();
				listaDeBotoes.add(maisDano);
				listaDeBotoes.add(maisRange);
				listaDeBotoes.add(maisASPD);
			}
			
			height = listaDeBotoes.size() * (2 + Botao.height) + 2;
			break;
		case BARREIRA:
			maisHP.x = x + width - maisDano.width - 2;
			maisHP.y = y + 2;
			maisRegen.x = x + width - maisDano.width - 2;
			maisRegen.y = y + maisRange.width + 4;
			
			if(listaDeBotoes.size() < 2){
				listaDeBotoes.clear();
				listaDeBotoes.add(maisHP);
				listaDeBotoes.add(maisRegen);
			}
			
			height = listaDeBotoes.size() * (2 + Botao.height) + 2;
		}

	}

	@Override
	public void DesenhaSe(Graphics2D dbg, int MapX, int MapY) {
		dbg.setColor(new Color(175,175,175,200));
		dbg.fillRect(
			(int)x, 
			(int)y, 
			width, 
			height
		);
		dbg.setColor(Color.WHITE);
		if(pai != null && estado != estado.OCULTO){
			if(pai.getClass().getName().equals(Torre.class.getName())){
				dbg.drawString("Dano: ", x + 8, y + 16);
				dbg.drawString("Alcance: " , x + 8, y + 16 + 22);
				dbg.drawString("Cadência: ", x + 8, y + 16 + 44);
				
				dbg.drawString("" + ((Torre)pai).dano, x + 75, y + 16);
				dbg.drawString("" + ((Torre)pai).range, x + 75, y + 16 + 22);
				dbg.drawString("" + Math.abs(((Torre)pai).intervaloDeTiro - ((Torre)pai).intervaloDeTiroInicial - 80), x + 75, y + 16 + 44);
			}
			else if(pai.getClass().getName().equals(Barreira.class.getName())){
				dbg.drawString("HP: ", x + 8, y + 16);
				dbg.drawString("Regen: " , x + 8, y + 16 + 22);
				
				dbg.drawString("" + ((Barreira)pai).vidaMaxima / 100, x + 75, y + 16);
				dbg.drawString("" + ((Barreira)pai).regen, x + 75, y + 16 + 22);
			}
			
		}
		
		for(Botao b: listaDeBotoes) b.DesenhaSe(dbg, MapX, MapY);
	}

	@Override
	public boolean mouseEstaEmCima(int mouseX, int mouseY) {
		return
			mouseX > x &&
			mouseX < x + width &&
			mouseY > y &&
			mouseY < y + height;
	}
	
	public void executaAcaoDoBotao(Sprite b){
		((Botao)b).executaAcao(pai);
	}

}


public class GerenciadorDeInimigos {
	
	private int
	timer = 0,
	tempoInicialDeSpawn = 3000,
	tempoDeSpawn = tempoInicialDeSpawn,
	inimigosCriados = 0;
	
	
	private boolean
	primeiroSpawn = true;
	
	public static int
	inimigosAzuisMortos = 0;
	
	
	public GerenciadorDeInimigos(){
		
	}
	
	public void simulaSe(int diftime){
		
		timer += diftime;
		
		int wmundo = GamePanel.instance.mapa.Largura*16;
		int hmundo = GamePanel.instance.mapa.Altura*16;
		
		if(primeiroSpawn) tempoDeSpawn = 5000;
		
		if(timer >= tempoDeSpawn){
			
			int tipo = 5;
			if(inimigosCriados < 125){
				tipo = GamePanel.instance.rnd.nextInt(4) + 4;
			}
			else{
				if(inimigosCriados < 500){
					for(int i = 0; i < 3; i++){
						tipo = GamePanel.instance.rnd.nextInt(7) + 1;
						if(tipo != 2 && tipo != 3) break;
					}
					
				}
				else{
					tipo = GamePanel.instance.rnd.nextInt(7) + 1;
				}
			}
			
			
			Personagem per = new Personagem(
					 GamePanel.instance.rnd.nextInt(wmundo-32),
					 0,
					 GamePanel.instance.imagemcharsets,
					 tipo
			);
			per.velX = 0;
			per.velY = (int)((Math.random() * 25) + 50);
			
			int i = 0;
			while(per.colidecircular()||per.colidecenario()){
				per.x = GamePanel.instance.rnd.nextInt(wmundo-32);
				per.y = 0;
				if(i > 300) break;
				i++;
			}
		
			if(i <= 300) GamePanel.instance.listaDePersonagens.add(per);
			
			if(primeiroSpawn){
				primeiroSpawn = false;
				tempoDeSpawn = tempoInicialDeSpawn;
			}
			inimigosCriados++;
			
			tempoDeSpawn -= 20;
			if(tempoDeSpawn < 650) tempoDeSpawn = 650;
			timer = 0;
		}
	}
}

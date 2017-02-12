
public class Evento {
	
	boolean ativo = false;
	
	int codigo = -1;
	int tipo = -1;
	
	int Pvar = -1;
	int Pop = -1;
	int Pvalor = -1;
	
	int x = -1;
	int y = -1;
	int xdest = -1;
	int ydest = -1;
	
	int Avar = -1;
	int Aop = -1;
	int Avalor = -1;
	
	public Evento() {
		// TODO Auto-generated constructor stub
	}
	public Evento(String linha) {
		String strs[] = linha.split(";");
		codigo = Integer.parseInt(strs[0]);
		tipo = Integer.parseInt(strs[1]);
		Pvar = Integer.parseInt(strs[2]);
		Pop = Integer.parseInt(strs[3]);
		Pvalor = Integer.parseInt(strs[4]);
		x = Integer.parseInt(strs[5]);
		y = Integer.parseInt(strs[6]);
		xdest = Integer.parseInt(strs[7]);
		ydest = Integer.parseInt(strs[8]);
		Avar = Integer.parseInt(strs[9]);
		Aop = Integer.parseInt(strs[10]);
		Avalor = Integer.parseInt(strs[11]);
	}
	
	public void testaAtivo(){
		if(Pvar<0||Pvar>=100){
			ativo = true;
			return;
		}
		
		switch (Pop) {
		case 0:
			ativo = GerenciadorDeEventos.variaveis[Pvar] == Pvalor;
			break;
		case 1:
			ativo = GerenciadorDeEventos.variaveis[Pvar] > Pvalor;
			break;
		case 2:
			ativo = GerenciadorDeEventos.variaveis[Pvar] < Pvalor;
			break;	

		default:
			break;
		}
	}
	
	public void executaAcao(){
		if(Avar<0||Avar>=100){
			ativo = true;
			return;
		}
		
		switch (Aop) {
		case 0:
			GerenciadorDeEventos.variaveis[Avar] = Avalor;
			break;
		case 1:
			GerenciadorDeEventos.variaveis[Avar] += Avalor;
			break;
		case 2:
			GerenciadorDeEventos.variaveis[Avar] -= Avalor;
			break;	

		default:
			break;
		}
	}
}

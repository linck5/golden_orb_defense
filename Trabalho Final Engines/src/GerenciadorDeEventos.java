import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.text.GapContent;


public class GerenciadorDeEventos {
	
	public static ArrayList<Evento> listaDeEventos = new ArrayList<Evento>();
	
	public static int variaveis[] = new int[100];
	
	public GerenciadorDeEventos() {
		// TODO Auto-generated constructor stub
	}
	public GerenciadorDeEventos(InputStream in) {
		 BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
		 
		 String line = "";
		 
		 try {
			while((line=bfr.readLine())!=null){
				 if(line.charAt(0)!='#'){
					 Evento ev = new Evento(line);
					 listaDeEventos.add(ev);
				 }
			 }
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void simulaSe(int diftime){

		for(int i = 0; i < listaDeEventos.size();i++){
			Evento ev = listaDeEventos.get(i);
			ev.testaAtivo();
			
			if(ev.ativo){
				int bx = ((int)(GamePanel.instance.pers.x+16))>>4;// /16
				int by = ((int)(GamePanel.instance.pers.y+36))>>4;// /16
	
			
				if(ev.x==bx&&ev.y==by){
					GamePanel.instance.pers.x = ev.xdest<<4; // vezes 16
					GamePanel.instance.pers.y = ev.ydest<<4; // vezes 16
					
					ev.executaAcao();
				}
			}
		}
		
	}
	
	public void desenhaSe(Graphics2D dbg){
		for(int i = 0; i < listaDeEventos.size();i++){
			Evento ev = listaDeEventos.get(i);
			if(ev.ativo){
				dbg.setColor(Color.YELLOW);
			}else{
				dbg.setColor(Color.RED);
			}
			int evx = ev.x <<4; // vezes 16
			int evy = ev.y <<4; // vezes 16
			
			dbg.drawRect(evx-GamePanel.instance.mapa.MapX, evy-GamePanel.instance.mapa.MapY, 16, 16);
		}
	}
	
}

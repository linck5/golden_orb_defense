import java.awt.Graphics2D;


public abstract class Sprite implements Cloneable {
	float x;
	float y;
	
	boolean vivo;
	
	public abstract void SimulaSe(int diftime);
	public abstract void DesenhaSe(Graphics2D dbg,int MapX,int MapY);
	public abstract boolean mouseEstaEmCima(int mouseX, int mouseY);
	
	@Override
	public Sprite clone() throws CloneNotSupportedException{
		return (Sprite)(super.clone());
	}
}

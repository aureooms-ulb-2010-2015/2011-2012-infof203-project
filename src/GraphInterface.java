import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.*;


public class GraphInterface extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private static int midle = 350;
	private static double angle = Math.PI;

	private static int _x = 0;
	private static int _y = midle;
	
	public Graphics2D g2;
	private Graph _graph;
	
	public GraphInterface(Graph g){
		this._graph = g;
	}
	
	public void paintComponent(Graphics g){
		
		g2 = (Graphics2D) g;
		g2.setFont(new Font("Helvetica",Font.BOLD,14));
		g2.setStroke(new BasicStroke(1.0f));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		for(int i=0;i<_graph.order();i++){
			this.paintNode(_graph.getNode(i));
		}
		for(int i=0;i<Arc.getNumberOfArcs();i++){
			this.paintArc(Arc.getArc(i));
		}
		
	}
	
	public void paintNode(Node node){
		int x=_x;
		int y=_y;
		g2.setPaint(new Color(255, 255, 255));
		g2.fill(new Ellipse2D.Double(x,y,50,50));
		g2.setPaint(node.color);
		g2.draw(new Ellipse2D.Double(x,y,50,50));
		g2.drawString(node.getName(),x+20,y+30);
		node.x=x+25;
		node.y=y+25;
		angle=(angle+(2*Math.PI/this._graph.order()))%(2*Math.PI);
		_x=(int) (midle+Math.cos(angle)*midle);
		_y=(int) (midle+Math.sin(angle)*midle);
	}
	public void paintArc(Arc arc){
		int xNL=arc.getLeftNode().x;
		int xNR=arc.getRightNode().x;
		int yNL=arc.getLeftNode().y;
		int yNR=arc.getRightNode().y;
		float X=(xNL-xNR);
		float Y=(yNL-yNR);
		double angle=Math.atan(X/Y);
		double sin=Math.sin(angle)*25;
		double cos=Math.cos(angle)*25;
		double lineSize1 = Math.hypot((xNL-sin)-(xNR+sin),(yNL-cos)-(yNR+sin));
		double lineSize2 = Math.hypot((xNL+sin)-(xNR-sin),(yNL+cos)-(yNR-sin));
		//g2.setPaint((arc.token)?new Color(200, 10, 10) :new Color(0, 0, 0));
		g2.setPaint(arc.color);
		
		double mult[] = new double[2];
		mult[0] = 0;
		mult[1] = 1;
		if( Y != 0 ){
			mult[0] = Math.sin(-X/Y);
			mult[1] = Math.cos(-X/Y);
		}
		int signs[] = new int[2];
		signs[0] = mult[0]>0?1:-1;
		signs[1] = mult[1]>0?1:-1;

		mult[0] = signs[0]*Math.pow(Math.abs(mult[0])+0.5,0.2);
		mult[1] = signs[1]*Math.pow(Math.abs(mult[1])+0.5,0.2);
		
		g2.drawString(""+arc.getWeight(), (int) ((xNR+X/2)+mult[0]*15),(int) ((yNR+Y/2)+mult[1]*15));
		if(lineSize1<lineSize2){	
			g2.draw(new Line2D.Double(xNL-sin,yNL-cos,xNR+sin,yNR+cos));
		}
		else{
			g2.draw(new Line2D.Double(xNL+sin,yNL+cos,xNR-sin,yNR-cos));
		}
	}
}
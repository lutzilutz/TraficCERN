package elements;

import main.Network;

public class RoundAbout extends Road {

	public RoundAbout(Network n, int length) {
		super(n, length);
		this.n = n;
		this.getRoadCells().get(this.getLength()-1).setNextCell(this.getRoadCells().get(0));
		this.getRoadCells().get(0).setPreviousCell(this.getRoadCells().get(this.getLength()-1));
	}
	public void connectTo(Road r, int i) {
		this.getRoadCells().get(i).setOutCell(r.getRoadCells().get(0));
		r.getRoadCells().get(0).setPreviousCell(getRoadCells().get(i));
	}
	public void setPositionFrom(Road r) {
		this.setX((int) (r.getX()+(r.getLength()*n.getCellWidth() + n.getCellWidth()/2 + this.getLength()*n.getCellWidth()/(2*Math.PI) )*Math.sin(2*Math.PI*r.getDirection()/360)));
		this.setY((int) (r.getY()-(r.getLength()*n.getCellHeight() + n.getCellHeight()/2 + this.getLength()*n.getCellHeight()/(2*Math.PI))*Math.cos(2*Math.PI*r.getDirection()/360) ));
	}
}

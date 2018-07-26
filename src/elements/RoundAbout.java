package elements;

import network.Network;

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
		double x = (int) (r.getX());
		double y = (int) (r.getY());
		double last = 0;
		for (int j=0 ; j<r.getReorientations().size() ; j++) {
			// Last segment
			if (j == r.getReorientations().size()-1) {
				x += n.getCellWidth() * (r.getLength()+0*0.5-r.getReorientations().get(j).getX()) * Math.sin(2*Math.PI*r.getReorientations().get(j).getY()/360.0);
				y += - n.getCellWidth() * (r.getLength()+0*0.5-r.getReorientations().get(j).getX()) * Math.cos(2*Math.PI*r.getReorientations().get(j).getY()/360.0);
			}
			// All others
			else {
				x += n.getCellWidth() * (r.getReorientations().get(j+1).getX()-last) * Math.sin(2*Math.PI*r.getReorientations().get(j).getY()/360.0);
				y += - n.getCellWidth() * (r.getReorientations().get(j+1).getX()-last) * Math.cos(2*Math.PI*r.getReorientations().get(j).getY()/360.0);
				last = r.getReorientations().get(j+1).getX();
			}
		}
		
		//x = (int) (r.getX() + n.getCellWidth() * (r.getReorientations().get(1).getX()-r.getReorientations().get(0).getX()) * Math.sin(2*Math.PI*r.getReorientations().get(0).getY()/360.0 + Math.PI));
		//y = (int) (r.getY());
		
		this.setX(x);
		this.setY(y);
	}
}

package elements;

public class RoundAbout extends Road {

	public RoundAbout(int length) {
		super(length);
		this.getRoadCells().get(this.getLength()-1).setNextCell(this.getRoadCells().get(0));;
	}

}
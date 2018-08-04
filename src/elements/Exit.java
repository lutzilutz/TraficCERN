package elements;

public class Exit implements Cloneable {
	
	private String name;
	private int position;
	
	public Exit(String name, int position) {
		this.name = name;
		this.position = position;
	}
	
	public Exit clone( ) {
		Exit e = null;
		try {
			e = (Exit) super.clone();
		} catch(CloneNotSupportedException cnse) {
			cnse.printStackTrace(System.err);
		}
		
		return e;
	}
	
	public void print() {
		System.out.print("(" + this.position + ", " + this.name + ") ");
	}
	
	public void println() {
		System.out.println("(" + this.position + ", " + this.name + ") ");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}

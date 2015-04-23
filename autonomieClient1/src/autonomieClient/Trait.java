package autonomieClient;

import java.awt.Point;
import java.io.Serializable;

public 	class Trait implements Serializable {
	Point p1,p2;
	public Trait(Point p1, Point p2){
		this.p1=p1; this.p2=p2;
	}
}
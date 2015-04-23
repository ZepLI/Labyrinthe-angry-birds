package autonomieClient;

import java.io.Serializable;

public class Position implements Serializable {
	public int i,j;
	public Position(int i, int j){
		this.i=i;
		this.j=j;
	}

}
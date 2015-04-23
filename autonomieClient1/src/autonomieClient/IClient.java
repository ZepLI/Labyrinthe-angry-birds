package autonomieClient;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IClient extends Remote{
	
	//public void updateAffichage() throws RemoteException;
	public void updateJoueurPos(Position[] joueursPos) throws RemoteException;
	public void updateLabyrinthe (boolean[][] traitsHorizontaux, boolean[][] traitsVerticaux, int dimen, Position positionPiece) throws RemoteException;
	public void commenceLeJeu() throws RemoteException;

}

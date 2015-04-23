package autonomieServeur;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import autonomieClient.*;

public interface IServeur extends Remote {
	public int getNbJoueurs () throws RemoteException;
	public int getNbJoueursEnregistres() throws RemoteException;
	public void setPos(int joueurID, Position pos) throws RemoteException;
	public void enregistrer(IClient client, int joueur)throws RemoteException;
	public void demarrerJeu() throws RemoteException;
}
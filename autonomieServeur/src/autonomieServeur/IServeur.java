package autonomieServeur;


import java.rmi.Remote;
import java.rmi.RemoteException;
import autonomieClient.*;
interface IServeur extends Remote {
	//����ֻ�����¶���һ��"Э��"���ÿͻ��˿��Ըı����˵Ĳ��������ܴ����ã���
	public int getNbJoueurs () throws RemoteException;
	public int getNbJoueursEnregistres() throws RemoteException;
	public void setPos(int joueurID, Position pos) throws RemoteException;
	public void enregistrer(IClient client, int joueur)throws RemoteException;
	public void demarrerJeu() throws RemoteException;
}
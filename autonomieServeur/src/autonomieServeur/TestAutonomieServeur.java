package autonomieServeur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;

import javax.swing.*;

public class TestAutonomieServeur {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			Serveur serveur = new Serveur();
			LocateRegistry.createRegistry(6666);
			Naming.rebind("//127.0.0.1:6666/serveur", serveur);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception lors du lancement du serveur");
			e.printStackTrace();
		} catch(MalformedURLException e){
			System.out.println("Exception lors du lancement du serveur : URL pas correct");
		}
		System.out.println("Serveur démarré!");

	}

}

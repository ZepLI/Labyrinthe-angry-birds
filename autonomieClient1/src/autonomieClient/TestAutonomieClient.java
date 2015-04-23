package autonomieClient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;

import javax.swing.*;

import autonomieServeur.IServeur;


public class TestAutonomieClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String nomserveur = "//127.0.0.1:6666/serveur";
			IServeur serveur = (IServeur) Naming.lookup(nomserveur);
	
			// On doit également contrôler ce qui est affiché en bas du panel
			//	en fonction du déroulement du jeu
			// donc on doit donner la référence de jpBas au jpHaut
			JPanel jpBas = new JPanel();
			JeuPanel jpHaut =  new JeuPanel(serveur, jpBas);
			System.out.println("Client "+jpHaut.getJoueurID()+ " connecté!");
			JFrame f = new JFrame("Labyrinthe Joueur N° " + jpHaut.getJoueurID());
			JPanel jp = (JPanel) f.getContentPane();
			jp.add("North",jpHaut);
			jp.add("Center",jpBas);	
			
			f.pack();
	        f.setLocationRelativeTo(null);
			f.setVisible(true);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("(MalformedURLException擦！");
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("RemoteException擦！");
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			System.out.println("NotBoundException擦！");
			e.printStackTrace();
		}
		
	}

}


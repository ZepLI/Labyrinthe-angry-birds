package autonomieServeur;

import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import autonomieClient.*;
class Serveur extends UnicastRemoteObject implements IServeur { 
	int jpLongueur = 600;
	int nbJoueurs = 2;
	int nbEnregistres = 0;
	int longueurPanel = 600; // Longueur du panel
	private int dimen ; // Dimension du labyrinte 
    boolean [][] traitsVerticaux;
	boolean [][] traitsHorizontaux;
	public Position[] joueursPos = new Position[nbJoueurs];
    private Position positionPiece;
	private LabyrintheGene labyrinthe;
	private List<IClient> clients;
	
	Serveur() throws RemoteException { 
		dimen = 15;
		clients = new ArrayList<IClient>();
		nouveauLaby();
		}

	
	public void nouveauLaby(){   //ֻ������һ���µ�labyrinthe ��Ҫ�ػ���
		labyrinthe = new LabyrintheGene(dimen);
		labyrinthe.genererLabyRandom();
		traitsVerticaux=labyrinthe.traitsVerticaux;
		traitsHorizontaux=labyrinthe.traitsHorizontaux;

       for(int k = 0; k<nbJoueurs; k++){
	   joueursPos[k] = new Position( (int)((dimen-1)/(nbJoueurs-1))*k,0);
       }
  
       // Positionnement de la pi��ce d'or
       positionPiece = new Position(dimen/2, dimen-1); // Coin sup��rieur droit du labyrinthe
	}
	
	/**
	 * M��thode qui sert �� mettre �� jour la position des oiseaux
	 * dans les jeuPanels de tous les joueurs
	 */
	private void updateJoueursPos(){
		try {
			for(IClient client : clients){   
				if(client !=null) client.updateJoueurPos(joueursPos);
				}
				} catch (RemoteException e) {
			System.out.println("Erreur in updateJoueursPos");
			e.printStackTrace();
		}
	}
	
/**
 * Méthode qui sert à mettre à jour les informations du labyrinthe
 * au début de chaque partie
 */
	private void updateTousLabyrinthes(){
		
		try {
			for(IClient client : clients){
				if(client != null) client.updateLabyrinthe(traitsHorizontaux, traitsVerticaux, dimen, positionPiece);; //Si au d��part y a qu un joueur, alors on update la position d'un seul joueur
	          }
			} catch (RemoteException e) {
			System.out.println("Erreur dans updateTousLabyrinthes");
			e.printStackTrace();
		}
	}
	
	// Getters et Setters
	public void setPos(int joueurID, Position posClient)  //posClient a ��t�� s��rialis�� et pass�� �� la m��thode du serveur par valeur!!
			throws RemoteException {
		// TODO Auto-generated method stub
		joueursPos [joueurID-1] = posClient;
		updateJoueursPos();
		
	// On v��rifie si l'un des joueurs a gagn��
	if( (posClient.i == positionPiece.i) && (posClient.j == positionPiece.j) ) {
		dimen++;
		nouveauLaby(); // G��n��ration d'un nouveau labyrinthe
		updateTousLabyrinthes(); // Notification des clients
		updateJoueursPos(); // Mise �� jour des positions des joueurs
		}
	}

	@Override
	public void enregistrer(IClient client, int joueurID) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Nom : "+joueurID);
		clients.add(client);
		nbEnregistres++;
		System.out.println("Joueur N° " + joueurID + " enregistre");
		updateTousLabyrinthes();
		updateJoueursPos(); // C'est le serveur qui décide de la position initiale
		for(IClient iclient : clients) 
			if( iclient != null)
				iclient.updateJoueurPos(null); // Mise à jour des autres clients déjà connectés
	}
	
	@Override
	public void demarrerJeu() throws RemoteException {
		for(IClient iclient : clients) 
			iclient.commenceLeJeu();
	}
	
	@Override
	public int getNbJoueurs() throws RemoteException {
		// TODO Auto-generated method stub
		return nbJoueurs;
	}
	
	@Override
	public int getNbJoueursEnregistres() throws RemoteException {
		return nbEnregistres;
	}
		
	}

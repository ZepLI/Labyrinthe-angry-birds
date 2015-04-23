package autonomieClient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import autonomieServeur.*;
public class JeuPanel extends JPanel implements IClient, KeyListener{
	private int longueur = 600 ;
	private boolean [][] traitsVerticaux;
	private boolean [][] traitsHorizontaux;
	private int dimen;
	private int niveau = 1;
	private int score = 0;
	private JLabel niveauLabel, scoreLabel;
	private Point[][] points; 
	private IServeur serveur;
	private Position[] joueursPos;
	private Position positionPiece;
	private List<Trait> traits;
	private Image[] imOiseau;
	private Image imgPiece;
	private JLabel attenteLabel;
	private boolean onjoue = false;
	private int joueurID = 0; //Pour un test

	private int nbJoueurs = 1;  // On le met à 1 à défaut, à initialiser dans le constructeur
	private int nbJoueursEnregistres = 1; // Valeur par défaut
	private JPanel jpBas;

	JeuPanel( IServeur serveur, JPanel jpBas){
		super();
		this.jpBas = jpBas;
		this.serveur = serveur;
		this.setFocusable(true);
		try { 
			UnicastRemoteObject.exportObject(this); }//因为这里的stub已经手动建立了（根据Remote接口建立的）
		catch (Exception ex) {
			System.out.println("Exception in UnicasteRemoteObjet.exportObject");
			ex.printStackTrace();
		}
		try {
			System.out.println("Demande d'enregistrement auprès du serveur");
			serveur.enregistrer(this, joueurID);
			nbJoueurs = serveur.getNbJoueurs();
			nbJoueursEnregistres = serveur.getNbJoueursEnregistres();
			this.joueurID = nbJoueursEnregistres; // Id attribué par le serveur 
			init(); // appelée après la récupération du nombre de joueurs enregistres
			
			if( nbJoueursEnregistres == nbJoueurs )
				serveur.demarrerJeu(); // Déblocage des mouvements des joueurs si tous les joueurs sont présents
		}
		catch (RemoteException ex) {
			System.out.println("Remote exception : Enregistrer");
			ex.printStackTrace();
		}
		this.setPreferredSize(new Dimension(longueur,longueur+5-longueur/(dimen+2)));
		addKeyListener(this);
	}

	private void init(){
		Toolkit tk = Toolkit.getDefaultToolkit();
		imOiseau = new Image[nbJoueurs];
		for(int k=0; k<nbJoueurs;k++){
			String path = "image/Oiseau" + new Integer(k+1).toString() + ".png";
			imOiseau[k] = tk.getImage(path);
		}		// Image associée à la pièce d'or

		imgPiece = tk.getImage("image/piece.png");
		attenteLabel = new JLabel("En attente des joueurs",SwingConstants.CENTER);
		attenteLabel.setSize(longueur, longueur/(dimen+2)*3/2);
		//		label.setVerticalTextPosition(SwingConstants.TOP);
		jpBas.add(attenteLabel);
	}
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		dessineLabyrinthe(g);
		dessinerJoueurs(g);
		dessinerPiece( g ); 

	}
	private void dessineLabyrinthe(Graphics g){
		for(Trait trait : traits)  g.drawLine(trait.p1.x, trait.p1.y, trait.p2.x, trait.p2.y);
	}


	private void dessinerJoueurs(Graphics g){
		for (int k=0; k<nbJoueursEnregistres; k++){
			Image image = imOiseau[k];
			Position pos = joueursPos[k];
			Point point = points[pos.i][pos.j];
			//			g.setColor(Color.BLUE);
			//			g.fillRect(point.x+longueur/((dimen+2)*4),point.y+longueur/((dimen+2)*4),longueur/((dimen+2)*2),longueur/((dimen+2)*2));
			g.drawImage(image,point.x,point.y,longueur/(dimen+2),longueur/(dimen+2),this);
		}
	}

	private void dessinerPiece( Graphics g ) {
		Point point = points[positionPiece.i][positionPiece.j];
		g.drawImage(imgPiece,point.x,point.y,longueur/(dimen+2),longueur/(dimen+2),this);
	}

	/**
	 * Qui genere "points" qui est la liste de points 
	 * @param 
	 */
	private void setPoints(){  //_dimen est la dimension du labyrinthe

		points = new Point [dimen+1][dimen+1];
		for(int m=0;m<dimen+1;m++)
			for(int n = 0;n<dimen+1;n++)
				points[m][n] = new Point((n+1)*longueur/(dimen+2),(m+1)*longueur/(dimen+2));
	}
	/** 
	 * Rendre la liste de traits selon les traitsverticaux 
	 *  et traits horizontaux acquis de depuis le joueur. * 
	 */

	private void setTraitsDuLaby(){ 
		setPoints();
		traits = new ArrayList<Trait>();

		for(int m =0; m<dimen; m++)
			for(int n =0; n<dimen+1;n++) {
				if(traitsVerticaux[m][n]) 
					traits.add(new Trait(points[m][n],points[m+1][n]));}
		for(int m =0; m<dimen+1; m++)
			for(int n =0; n<dimen;n++) {
				if(traitsHorizontaux[m][n]) 
					traits.add(new Trait(points[m][n],points[m][n+1]));
			}	
	}	


	@Override
	public void updateJoueurPos(Position[] joueursPos) throws RemoteException {
		// TODO Auto-generated method stub
		if( joueursPos != null )
			this.joueursPos = joueursPos;
		else
			nbJoueursEnregistres = serveur.getNbJoueursEnregistres(); // En attente, mise à jour du nombre de joueurs enregistrés
		repaint();
	}

	@Override
	public void updateLabyrinthe (boolean[][] traitsHorizontaux, 
			boolean[][] traitsVerticaux, int dimen,Position positionPiece) throws RemoteException {

		// TODO Auto-generated method stub
		this.traitsHorizontaux = traitsHorizontaux;
		this.traitsVerticaux = traitsVerticaux;
		this.dimen = dimen;
		this.positionPiece = positionPiece;
		setTraitsDuLaby();
		//repaint();
	}

	@Override
	public void commenceLeJeu() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Démarrage effectif: déblocage des mouvements des joueurs");
		onjoue = true;
		niveauLabel = new JLabel("Niveau : " + niveau, SwingConstants.CENTER);
		scoreLabel = new JLabel("Points : " + score, SwingConstants.CENTER);
		
		attenteLabel.setText("");
		jpBas.add("West", niveauLabel);
		jpBas.add("East",scoreLabel);
		jpBas.repaint();
		System.out.println("Joueur : " + joueurID + " a commencé");
	}

	@Override
	public void keyPressed(KeyEvent arg0){
		// TODO Auto-generated method stub
		if(!onjoue) return; 
		Position pos = joueursPos[joueurID-1];
		int i =pos.i;
		int j =pos.j;
		try {
			switch(arg0.getKeyCode()){
			case KeyEvent.VK_UP :
				if(traitsHorizontaux[i][j]==false) {
					pos.i--;
					serveur.setPos(joueurID, pos);
				}
				break;
			case KeyEvent.VK_DOWN :
				if(traitsHorizontaux[i+1][j]==false) {
					pos.i++;
					serveur.setPos(joueurID, pos);
				}
				break;
			case KeyEvent.VK_LEFT :
				if(traitsVerticaux[i][j]==false) {
					pos.j--;
					serveur.setPos(joueurID, pos);
				}
				break;
			case KeyEvent.VK_RIGHT :
				if(traitsVerticaux[i][j+1]==false) {
					pos.j++;
					serveur.setPos(joueurID, pos);
				}
				break;
			}
		}catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("Erreur in Keypressed");
			e.printStackTrace();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}	

	public int getLongueur(){
		return longueur;
	}
	
	public int getJoueurID() {
		return joueurID;
	}

	public void setJoueurID(int joueurID) {
		this.joueurID = joueurID;
	}
}





/**
 * 15/03/2015
 * 
 * Classe destin¨¦e ¨¤ la g¨¦neration d'un labyrinthe al¨¦atoire 
 * de taille "dimen" changeable.
 * 
 */
package autonomieServeur;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.List;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import autonomieClient.*;
class LabyrintheGene {
    boolean [][] traitsVerticaux;
	boolean [][] traitsHorizontaux;
	int dimen;
	int laby[][];   //utilise dans la methode genererLabyRandom(), pour la representation de l'etat de chaque case
	ArrayList<Position> voisins;  //utilise dans la methode genererLabyRandom()
	
	public LabyrintheGene(int dimen ){
		  this.dimen =dimen;
          initialiserCarreaux(dimen);   // Pour mettre tout a true
	      }
    
	private void initialiserCarreaux(int i){  // i etant la dimension
		traitsVerticaux = new boolean [i][i+1];
		for(int m =0; m<i; m++)
			for(int n =0; n<i+1;n++) 
				traitsVerticaux[m][n]=true;
		traitsHorizontaux = new boolean[i+1][i]; 
		for(int m =0; m< i+1; m++)
			for(int n =0; n<i;n++) 
				traitsHorizontaux[m][n]=true;
	}
	

	public void genererLabyRandom(){
	    laby = new int[dimen][dimen]; 
	    ArrayList<Position> parcours = new ArrayList<Position>(); 
		Position posInitiale = new Position(0,0);
		parcours.add(posInitiale);
		laby[0][0] = 1;
	    Position pos = posInitiale;
	    Random random = new Random();
        
	    boolean premierEssaie = true;
	    
        while(premierEssaie||pos!= posInitiale){
        	//if(pos== posInitiale) System.out.println("²Ù£¡");
            //System.out.println("Position est "+ pos.i+" "+pos.j);
        	premierEssaie=false;
    	    voisins = new ArrayList<Position>();
            setVoisins(pos);

	        if(voisins.size()>0) {
		    	int index =random.nextInt(voisins.size());
		    	Position _pos = voisins.get(index);
		    	// traitement de la prochaine etape
		    	if(pos.i-_pos.i==1) {
		    		traitsHorizontaux[pos.i][pos.j] = false;  //Enlever le trait
		    		laby[_pos.i][_pos.j]=1;
		    		//System.out.println("Trait horizontal enleve : " + pos.i+" "+ pos.j);
		    	}
		    	if(pos.i-_pos.i==-1) {
		    		traitsHorizontaux[_pos.i][_pos.j] = false;  //Enlever le trait
		    		laby[_pos.i][_pos.j]=1;
		    		//System.out.println("Trait horizontal enleve : " + _pos.i+" "+ _pos.j);
		    	}
		    	if(pos.j-_pos.j==1) {
		    		traitsVerticaux[pos.i][pos.j] = false;  //Enlever le trait
		    		laby[_pos.i][_pos.j]=1;
		    		//System.out.println("Trait vertical enleve : " + pos.i+" "+ pos.j);
		    	}
		    	if(pos.j-_pos.j==-1) {
		    		traitsVerticaux[_pos.i][_pos.j] = false;  //Enlever le trait
		    		laby[_pos.i][_pos.j]=1;
		    		//System.out.println("Trait vertical enleve : " + _pos.i+" "+ _pos.j);
		    	}
		    	parcours.add(_pos);
		    	pos = _pos;
		    	}else {
		    	parcours.remove(parcours.size()-1);
		    	pos = parcours.get(parcours.size()-1);
		     }
        }   

	}
	private void setVoisins(Position pos)
	{
	    if(pos.i-1>=0 && pos.i-1<dimen && laby[pos.i-1][pos.j]==0)
		       voisins.add(new Position(pos.i-1,pos.j));
		if(pos.j-1>=0 && pos.j-1<dimen && laby[pos.i][pos.j-1]==0)
		       voisins.add(new Position(pos.i,pos.j-1));
        if(pos.i+1>=0 && pos.i+1<dimen && laby[pos.i+1][pos.j]==0)
		       voisins.add(new Position(pos.i+1,pos.j));
		if(pos.j+1>=0 && pos.j+1<dimen && laby[pos.i][pos.j+1]==0)
		       voisins.add(new Position(pos.i,pos.j+1));
	}
}




package main.gameObjects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import main.gfx.Sprite;

/**
 * cette classe représente les cartes chiffres du jeu : [0..9] x [BLEU,JAUNE,VERT,ROUGE]
 * @author Stoufa
 *
 */
public class CarteChiffre extends Carte {

    /**
     * la valeur de la carte, doit être un entier entre 0 et 9
     */
    private int valeur;

    /**
     * constructeur
     * @param couleur : la couleur de la carte
     * @param valeur : la valeur de la carte
     * @throws SlickException 
     */
    public CarteChiffre( Couleur couleur, int valeur ) throws SlickException {
        super( couleur );
        this.valeur = valeur;
        image = Sprite.get( valeur, couleur );
    }

    /**
     * permet de retourner une chaîne contenant la représentation de l'objet courant
     */
    @Override
    public String toString() {
        return "(" + couleur.getValeur() + "," + valeur + ")";
    }

    /**
     * @return true : si la carte courante et celle passée en paramétre sont compatibles ( çàd jouable )
     * @return false : sinon
     */
    @Override
    public boolean compatible( Carte carte ) {
        if ( carte instanceof CarteChiffre ) { //	CarteChiffre
            // même couleur ou même valeur ?
            return ( carte.couleur == couleur ) || ( ( (CarteChiffre) carte ).valeur == valeur ); // même couleur ou même valeur
        } else { // CarteSpecial
            // même couleur ?
            return carte.couleur == couleur; // même couleur
        }
    }

    @Override
    public void update( GameContainer container ) throws SlickException {
        // TODO Auto-generated method stub

    }

}

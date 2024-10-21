package main.gameObjects;

import java.util.ArrayList;

/**
 * cette classe représente la main de chaque joueur
 * @author Stoufa
 *
 */
public class Main {

    /**
     * l'ensemble des cartes dans la main
     */
    protected ArrayList<Carte> cartes;

    /**
     * constructeur
     */
    public Main() {
        cartes = new ArrayList<Carte>();
    }

    /**
     * permet d'ajouter une carte ŕ la main
     * @param carte : la carte ŕ ajouter
     */
    public void ajouter( Carte carte ) {
        cartes.add( carte );
    }

    /**
     * permet de retirer une carte de la main
     * @param num : l'indice de la carte ŕ retirer dans la liste
     * @return la carte retirée
     */
    public Carte retirer( int num ) {
        return cartes.remove( num );
    }

    /*
    public void retirer(Carte carte) {
    	cartes.remove(carte);
    }
    */

    /**
     * permet de retourner l'ensemble des cartes dans la main
     * @return l'ensemble des cartes dans la main
     */
    public ArrayList<Carte> getCartes() {
        return cartes;
    }

    /**
     * @return le nombre de cartes dans la main
     */
    public int nbCartes() {
        return cartes.size();
    }

    @Override
    public String toString() {
        String str = "";
        for ( Carte carte : cartes ) {
            str = str + carte.toString();
        }
        return str;
    }

    /**
     * removes the card from the hand
     * @param playedCard
     */
    public void retirer( Carte playedCard ) {
        cartes.remove( playedCard );
    }

}

package main.gameObjects;

import java.util.Random;

/**
 * cette classe présente les fonctionnalités communes entre la pioche et le talon
 * @author Stoufa
 *
 */
public class Pile extends Main {

    /**
     * générateur de valeurs aléatoires
     */
    protected Random rand = new Random();

    /**
     * permet de dépiler le sommet de la pile
     * @return la carte supprimée
     */
    public Carte depiler() {
        return cartes.remove( 0 );
    }

    /**
     * permet d'empiler une carte
     * @param carte : la carte à empiler
     */
    public void empiler( Carte carte ) {
        cartes.add( 0, carte );
    }

    /**
     * permet de mélanger la pile (shuffle) en utilisant l'algorithme : Mélange de Fisher-Yates
     * @see https://fr.wikipedia.org/wiki/M%C3%A9lange_de_Fisher-Yates
     * académiquement parlant, on ne peut pas mélanger une pile ! mais dans ce contexte, la pile n'est
     * pas une structure FIFO ( First In First Out ) ordinaire, on peut la mélanger
     * c'est une méthode commune entre les classes Pioche et Talon
     */
    public void melanger() {
        for ( int i = cartes.size() - 1; i > 0; i-- ) {
            // Permuter une carte aléatoire entre la première
            // et la dernière carte de la boucle
            int pick = rand.nextInt( i ); // entier aléatoire entre 0 et i - 1
            Carte randCard = cartes.get( pick );
            Carte lastCard = cartes.get( i );
            cartes.set( i, randCard );
            cartes.set( pick, lastCard );
        }
    }

    /**
     * @return une chaîne de caratères représentant la pile courante
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * @return le sommet de la pile
     */
    public Carte sommet() {
        if ( cartes.isEmpty() ) {
            return null;
        }
        return cartes.get( 0 ); // Retourne le sommet de la pile ( sans le supprimer )
    }

}

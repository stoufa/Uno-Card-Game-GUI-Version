package main.gameObjects;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import main.common.Debug;
import main.io.Audio;

/**
 * classe qui représente un joueur non-humain
 * On peut enrichir cette classe en implémentant des stratégies de jeu différentes : 
 * stratégie aufencife ( jouer les cartes +2 et +4 en premier ),
 * défencif ( jouer les cartes +2 et +4 en dernier ),
 * garder la couleur courante,
 * garder les cartes noirs à la fin, ....
 * @author Stoufa
 *
 */
public class Bot extends Joueur {

    Random random = new Random(); // utilisé pour simuler le comportement aléatoire du joueur

    public Bot( String pseudo, Pioche pioche, Talon talon ) {
        super( pseudo, pioche, talon );
        // TODO Auto-generated constructor stub
    }

    @Override
    public void render( Graphics g ) throws SlickException {
        super.render( g );
    }

    @Override
    public void update( GameContainer container ) throws SlickException {
        // TODO Auto-generated method stub
    }

    @Override
    public void jouerCarte() throws SlickException {
        // TODO choose randomly !
        System.out.println( "The bot have " + nbCartesJouables() + " playable cards" );
        Debug.log( "Waiting for the bot's move..." );
        try {
            Thread.sleep( 2000 ); // Thinks for 2 seconds
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        Carte carte = null;
        int nbEssais = 100;
        int indiceCarte = 0;
        do {
            if ( nbEssais > 0 ) {
                nbEssais--;
                indiceCarte = random.nextInt( main.cartes.size() );
            } else {
                indiceCarte = ( indiceCarte + 1 ) % main.cartes.size();
            }
            carte = main.cartes.get( indiceCarte );
            if ( !carte.jouable ) {
                Debug.log( "la carte " + carte + " n'est pas jouable" );
            }
        } while ( !carte.jouable );
        if ( carte instanceof CarteChiffre ) {
            // play the sound only for numerical cards !
            // special cards have special sound effects and we
            // don't want to mix them up !
            Audio.playSound( "clickSound" );
        }
        this.playedCard = carte; // saving the played card

        if ( playedCard.couleur == Couleur.NOIR ) {
            // On doit demander une couleur au joueur
            Debug.log( "Bot doit choisir une couleur..." );
            Couleur couleur = donnerCouleur();
            // Si elle est de couleur noir, on est sûr qu'elle est spéciale !
            ( (CarteSpecial) playedCard ).setCouleur( couleur );
            // Changer la couleur de l'arriére-plan
            // la couleur va être mis à jour automatiquement dans la méthode render()
            //Jeu.changeBackgroundColorTo(couleur);
            if ( ( (CarteSpecial) playedCard ).symbole == Symbole.JOKER ) {
                Audio.playSound( "wildSound" );
            }
        }

        main.retirer( playedCard ); // remove the card from the player's hand !
        talon.empiler( playedCard ); // add it to the discard pile
        Debug.log( pseudo + " a joué " + playedCard );
    }

    /**
     * @return chaîne décrivant le joueur en cours
     * le joueur est identifié par son pseudo
     */
    @Override
    public String toString() {
        return "[Bot] : " + pseudo;
    }

    private Couleur donnerCouleur() {
        Debug.log( "Waiting for bot's choice of color..." );
        String[] colorNames = { "Jaune", "Vert", "Bleu", "Rouge" };
        String selectedColor = null;
        try {
            Thread.sleep( 2000 ); // Thinks for 2 seconds
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        selectedColor = colorNames[random.nextInt( colorNames.length )];
        Debug.log( "Bot selected the color : " + selectedColor );
        return Couleur.getCouleur( selectedColor );
    }

}

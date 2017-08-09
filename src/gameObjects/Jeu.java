package main.gameObjects;

import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import org.lwjgl.LWJGLException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import main.Game;
import main.common.Config;
import main.common.Debug;
import main.gfx.Sprite;
import main.io.Audio;
import main.io.GetColorDialog;
import main.states.GameOverState;

/**
 * classe représentant le jeu, elle est responsable du déroulement du jeu suivie
 * du sens du jeu, de l'activation des effets spéciales, ...
 * 
 * @author Stoufa
 *
 */
public class Jeu {
    /**
     * la pioche
     */
    private Pioche               pioche;
    /**
     * le talon
     */
    private Talon                talon;
    /**
     * le nombre de joueurs ( doit être entre 2 et 4 )
     */
    private int                  nbJoueurs;
    /**
     * la liste des joueurs
     */
    private Joueur[]             joueurs;
    /**
     * le sens du jeu, peut avoir deux valeurs possibles : -1 de droite à
     * gauche, 1 de gauche à droite [0] + (1) -> [1] ... [1] + (-1) -> [0] ...
     */
    private int                  sens  = -1;                 // par défaut à gauche
    /**
     * l'indice du joueur courant, initialement le premier
     */
    static int                   tour  = 0;
    /**
     * l'objet Joueur à l'indice indiceJoueurCourant du tableau joueurs
     */
    public static Joueur         joueurCourant;
    /**
     * entier qui contient l'identifiant du joueur actuel
     */
    //public static int tour = 1;
    static Input                 input = null;
    /**
     * utilisé pour arrêter le jeu jusqu'a ce que le joueur clique sur une carte !
     */
    static CountDownLatch        countDownLatch;
    /**
     * utilisé pour attendre la couleur choisie par le joueur
     */
    public static CountDownLatch waitForDialogCountDownLatch;
    //	public static boolean clickReceived;
    //	public static boolean colorReceived;
    /**
     * dialogue du choix de la couleur
     */
    public static GetColorDialog dialog;

    /**
     * constructeur
     * 
     * @throws SlickException
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     */
    public Jeu() throws SlickException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        init();
    }

    /**
     * permet de démarrer le jeu
     * @param sbg 
     * @throws InterruptedException 
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     * @throws SlickException 
     * @throws LWJGLException 
     */
    public void lancer( StateBasedGame sbg ) throws InterruptedException, ClassNotFoundException,
            InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SlickException {
        Audio.playMusic();
        System.out.println( "=== Le jeu commence ===" );
        boolean effetSpecial = false; // cette variable permet de boucler à l'infini dans le cas des cartes spéciales
        while ( true ) { // boucle du jeu
            joueurCourant = joueurs[tour];
            System.out.println( "Tour de " + joueurCourant.pseudo );
            // effetSpecial : pour qu'on n'active pas l'effet plus qu'une fois !
            // par exemple : 2 joueurs le premier joue une carte spéciale, l'autre n'a pas de
            // cartes jouables, donc, il passe son tour, là, on ne doit pas réactiver l'effet spéciale
            // de la carte au sommet du talon
            if ( effetSpecial && talon.sommet() instanceof CarteSpecial ) { // le joueur précédant a joué une carte spéciale
                effetSpecial = false;
                if ( ( (CarteSpecial) talon.sommet() ).getSymbole() == Symbole.PASSER ) { // le joueur courant doit passer son tour
                    System.out.println( joueurCourant.pseudo + " doit passer son tour -> effet de la carte : "
                            + talon.sommet().toString() );
                    Audio.playSound( "skipSound" );
                    joueurSuivant();
                    continue;
                }
                if ( ( (CarteSpecial) talon.sommet() ).getSymbole() == Symbole.PLUS2 ) { // le joueur précédant a joué +2
                    // le joueur courant doit piocher 2 cartes
                    System.out.println( joueurCourant.pseudo
                            + " doit piocher 2 cartes et passer son tour -> effet de la carte " + talon.sommet() );
                    Audio.playSound( "plus2Sound" );
                    for ( int i = 0; i < 2; i++ ) {
                        joueurCourant.prendreCarte();
                    }
                    // et passer son tour
                    joueurSuivant();
                    continue;
                }
                if ( ( (CarteSpecial) talon.sommet() ).getSymbole() == Symbole.PLUS4 ) { // le joueur précédant a joué +4
                    // le joueur courant doit piocher 4 cartes
                    System.out.println( joueurCourant.pseudo
                            + " doit piocher 4 cartes et passer son tour -> effet de la carte " + talon.sommet() );
                    Audio.playSound( "plus4Sound" );
                    for ( int i = 0; i < 4; i++ ) {
                        joueurCourant.prendreCarte();
                    }
                    // et passer son tour
                    joueurSuivant();
                    continue;
                }
            }
            joueurCourant.jouerTour();
            Debug.log( "======== Fin du Tour ========" );
            // TODO : on doit mettre à jour les cartes des mains des joueurs aprés chaque tour ! ( jouabilité ! )
            updatePlayersHands();

            if ( joueurCourant.nbCartes() == 0 ) { // on teste si le joueur courant a vidé sa main
                System.out.println( joueurCourant.pseudo + " a gagné !" );
                Audio.playSound( "winSound" );
                break;
            }
            if ( joueurCourant.nbCartes() == 1 ) { // on teste si le joueur courant lui reste une seule carte ( UNO ! ) dans sa main
                System.out.println( joueurCourant.pseudo + " <UNO!>" );
                Audio.playSound( "unoSound" );
            }
            // On doit tester ici si le joueur a des doublons de la carte jouée TODO
            // On doit tester la carte inverser à ce niveau
            if ( joueurCourant.playedCard != null && talon.sommet() instanceof CarteSpecial ) { // le joueur courant a joué une carte spéciale
                // le test sur la carte jouée pour ne pas inverser le sens encore une fois dans le cas ou 
                // un joueur à inversé le sens et le joueur suivant n'a pas de cartes jouables ! sans ce 
                // test le sens va être inversé encore une fois !
                effetSpecial = true; // activer l'effet spécial
                if ( ( (CarteSpecial) talon.sommet() ).getSymbole() == Symbole.INVERSER ) {
                    // le joueur courant à inversé le sens
                    System.out.println( joueurCourant.pseudo + " a inversé le sens du jeu" );
                    Audio.playSound( "reverseSound" );
                    sens *= -1; // la valeur de sens est soit 1 soit -1, on multiplie par -1 pour changer
                    effetSpecial = false; // l'effet spécial est activé dans ce cas
                }
            }
            joueurSuivant();
            System.out.println();
        }
        System.out.println( "=== Fin du jeu ===" );
        Audio.stopMusic();
        // go to the game over state
        sbg.enterState( GameOverState.stateID );
    }

    /**
     * mise à jour de la jouabilité des cartes des joueurs
    * @param joueurCourant 
     */
    private void updatePlayersHands() {
        for ( Joueur joueur : joueurs ) { // Pour chaque joueur ...
            for ( Carte carte : joueur.main.cartes ) { // On parcourt ses cartes ...
                // Et on les met à jour ( jouabilité avec la carte du sommet du talon )
                carte.jouable = carte.compatible( talon.sommet() );
            }
        }
    }

    /**
     * permet de passer au joueur suivant selon le sens du jeu
     */
    private void joueurSuivant() {
        // avancer vers le joueur suivant
        tour += sens;
        // On doit vérifier si l'indice a dépasser les bornes du tableau
        if ( tour < 0 ) {
            tour += nbJoueurs;
            // exemple : 3 joueurs -> [ 0 , 1 , 2 ]
            // 0 + (-1) = -1 < 0 -> -1 + 3 -> 2 ( dernier joueur )
        }
        if ( tour > nbJoueurs - 1 ) { // indice >= nbJoueurs
            tour -= nbJoueurs;
            // exemple : 3 joueurs -> [ 0 , 1 , 2 ]
            // 2 + (1) = 3 > 2 -> 3 - 3 -> 0 ( premier joueur )
        }
    }

    public void update( GameContainer container ) throws SlickException {
        input = container.getInput();
        for ( int i = 0; i < joueurs.length; i++ ) { // mettre à jour l'état des joueurs
            joueurs[i].update( container );
        }
    }

    /**
     * permet d'afficher les mises à jour sur le jeu
     * 
     * @param g
     * @throws SlickException
     */
    public void render( Graphics g ) throws SlickException {
        changeBackgroundColorTo( talon.sommet().couleur );

        for ( int i = 0; i < joueurs.length; i++ ) { // affichage des joueurs
            joueurs[i].render( g );
        }

        pioche.render( g );
        talon.render( g );
    }

    public static void changeBackgroundColorTo( Couleur couleur ) throws SlickException {
        Image bgImage = Sprite.getBackground( couleur );
        if ( bgImage == null ) {
            Debug.err( "derniére carte jouée noire ?" );
            return;
        }
        bgImage.draw( 0, 0, Game.WIDTH, Game.HEIGHT );
    }

    public void init() throws SlickException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        //countDownLatch = new CountDownLatch(1);
        //waitForDialogCountDownLatch = new CountDownLatch(1);	// used to wait the dialog

        dialog = new GetColorDialog();
        //		dialog.setVisible(false);	// hidden by default

        Sprite.load();
        Config.load();
        pioche = new Pioche();
        pioche.melanger();
        talon = new Talon( pioche );

        // TODO : get nbJoueurs from server

        nbJoueurs = Integer.parseInt( Config.get( "nbJoueurs" ) );
        joueurs = new Joueur[nbJoueurs];
        Position positionsJoueurs[] = new Position[nbJoueurs];

        switch ( nbJoueurs ) {
        case 2:
            // we place the players face to face :)
            positionsJoueurs[0] = Position.BAS;
            positionsJoueurs[1] = Position.HAUT;
            break;
        case 3:
            // we place the players like so : right, left, down
            positionsJoueurs[0] = Position.BAS;
            positionsJoueurs[1] = Position.DROITE;
            positionsJoueurs[2] = Position.GAUCHE;
            break;
        case 4:
            // we place the players in the four edges of the screen, making a
            // cercle
            positionsJoueurs[0] = Position.BAS;
            positionsJoueurs[1] = Position.DROITE;
            positionsJoueurs[2] = Position.HAUT;
            positionsJoueurs[3] = Position.GAUCHE;
            break;
        default:
            break;
        }

        // 1er joueur Humain, les autres sont des Bots
        for ( int i = 0; i < nbJoueurs; i++ ) {
            //String pseudoJoueur = Config.get("j" + i);	// getting player names
            //joueurs[i] = new Humain(pseudoJoueur, pioche, talon);
            //joueurs[i] = new Bot(pseudoJoueur, pioche, talon);

            if ( i == 0 ) {
                // first player is humain
                String pseudoJoueur = JOptionPane.showInputDialog( "pseudo ? >> " );
                joueurs[i] = new Humain( pseudoJoueur, pioche, talon );
            } else {
                // the rest are bots
                joueurs[i] = new Bot( "Bot" + i, pioche, talon );
            }

            joueurs[i].position = positionsJoueurs[i];
            joueurs[i].id = i;
        }

        /*
        // Tous les joueurs sont des humains
        for (int i = 0; i < nbJoueurs; i++) {
        	String pseudoJoueur = JOptionPane.showInputDialog( String.format("pseudo (%d) ? >> ", i + 1) );
        	joueurs[i] = new Humain(pseudoJoueur, pioche, talon);
        	joueurs[i].position = positionsJoueurs[i];
        	joueurs[i].id = i;
        }
        */

        for ( int i = 0; i < joueurs.length; i++ ) { // Distirbution des cartes
            for ( int j = 0; j < 7; j++ ) { // Chaque joueur va prendre 7 cartes
                joueurs[i].prendreCarte();
            }
        }

    }

}

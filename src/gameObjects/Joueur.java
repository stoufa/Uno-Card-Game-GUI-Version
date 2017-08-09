package main.gameObjects;

import java.util.ArrayList;

import javax.swing.UnsupportedLookAndFeelException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.Game;
import main.gfx.Sprite;
import main.io.Audio;

/**
 * cette classe représente le joueur : ce qu'il a et ce qu'il peut faire, çàd ses propriétés et ses actions
 * @author Stoufa
 *
 */
public abstract class Joueur extends GameObject {

    /**
     * la main du joueur contenant toutes ses cartes
     */
    public Main     main;
    /**
     * le pseudo du joueur
     */
    public String   pseudo;
    /**
     * la pioche : la pile des cartes où le joueur peut prendre des cartes dans le cas où il n'a pas de cartes jouables
     */
    public Pioche   pioche;
    /**
     * le talon : la pile des cartes où les joueurs dépose leurs cartes
     */
    public Talon    talon;
    /**
     * la position du joueur sur l'écran : haut, bas, droite, gauche
     */
    public Position position;
    /**
     * identifiant du joueur
     */
    public int      id;
    /**
     * la carte jouée
     */
    public Carte    playedCard;

    /**
     * contructeur
     * @param pseudo : pseudo du joueur
     * @param pioche : la pioche
     * @param talon : le talon
     */
    public Joueur( String pseudo, Pioche pioche, Talon talon ) {
        this.pseudo = pseudo;
        this.pioche = pioche;
        this.talon = talon;
        main = new Main();
    }

    /**
     * permet de prender une carte de la pioche et l'ajouter à la main du joueur
     * @return la carte tirée
     */
    public Carte prendreCarte() {
        if ( pioche.nbCartes() == 0 ) { // la pioche est vide !
            // TODO : on peut ajouter une animation ici
            System.out.println( "La pioche est vide !" );
            // dans ce cas, on doit utiliser le talon ( sauf le sommet ) pour la populer de nouveau
            Carte sommetTalon = talon.depiler();
            while ( talon.nbCartes() != 0 ) { // cette boucle va vider le talon dans la pioche
                pioche.empiler( talon.depiler() );
            }
            pioche.melanger();
            talon.empiler( sommetTalon ); // remettre le sommet du talon
        }
        //Debug.log("pioche: " + pioche.nbCartes());
        Carte carte = pioche.depiler();
        main.ajouter( carte );
        return carte;
    }

    /**
     * cette méthode permet d'afficher les cartes dans la main du joueur courant
     * 2 cas possibles :
     * 		(1) nombre de cartes paire : on a n cartes, n / 2 sont font des rotations de t, 2t, 3t, ...
     * 		et les n / 2 font des rotation de -t, -2t, -3t, ... avec t l'angle de rotation qui va dépendre 
     * 		du nombre de cartes dans la main
     * 		(2) nombre de cartes impaire : on a n + 1 cartes, la carte au milieu reste tel quelle sans rotation
     * 		les n / 2 cartes à sa droite subissent des rotations de t, 2t, 3t, ..., (n / 2)t
     * 		et les n / 2 cartes à sa gauche subissent des rotations de -t, -2t, -3t, ...., -(n / 2)t
     * @throws SlickException 
     */
    public void afficherMain( Graphics g ) throws SlickException {
        ArrayList<Carte> cartes = main.getCartes();
        int indiceCarteMilieu = cartes.size() / 2;

        // le dessin / rendering des cartes doit être dans un sens fixé ( de gauche à droite )
        // pour utiliser le sens inverse dans la détéction du click

        float coefficientAngle;
        Carte carte;

        float initialX = JoueurPainter.getStartPosX( position ), initialY = JoueurPainter.getStartPosY( position );
        // TODO : l'angle et l'offset doivent être proportionelles au nombre de cartes
        float angle = 10;
        float offset = 30;
        if ( cartes.size() < 10 ) {
            angle = 10;
            offset = 30;
        } else {
            angle = 5;
            offset = 20;
        }

        //Image oldCardImage = null;
        Image hiddenCardImg = Sprite.getHiddenCard();

        if ( cartes.size() % 2 == 0 ) { // nombre de cartes paire (1)

            int x = indiceCarteMilieu - 1;
            coefficientAngle = -( 1 / 2 + x );
            /**
             * exemple : 4 cartes
             * indiceCarteMilieu	=	2
             * x					=	1		0		-1		-2
             * coefficientAngle		=	-3/2	-1/2	1/2		3/2
             */

            /**
             * Rendering				0	-> n - 1
             * Checking click events	n-1	-> 0
             */
            for ( int i = 0; i < cartes.size(); i++ ) {
                // [ 0 .. indiceCarteMilieu - 1 ] les cartes à gauche
                // [ indiceCarteMilieu .. cartes.size() - 1 ] les cartes à droite

                carte = cartes.get( i ); // so we don't have a nullPointerException !
                if ( carte == null ) {
                    continue;
                } // to prevent unpredicted nullpointer exceptions !
                if ( JoueurPainter.getOffsetAxis( position ) == 'x' ) {
                    carte.x = initialX + coefficientAngle * offset;
                    carte.y = initialY;
                } else { // y
                    carte.x = initialX;
                    carte.y = initialY + coefficientAngle * offset;
                }

                carte.jouable = carte.compatible( talon.sommet() );

                if ( this instanceof Humain ) {
                    //if (id == Jeu.tour) {

                    carte.rotate( JoueurPainter.getAngleCorrection( position ) * coefficientAngle * angle
                            + JoueurPainter.getAdditionalRotation( position ) );
                    carte.render( g );

                } else {

                    hiddenCardImg.setRotation( JoueurPainter.getAngleCorrection( position ) * coefficientAngle * angle
                            + JoueurPainter.getAdditionalRotation( position ) );
                    hiddenCardImg.draw( carte.x, carte.y );

                }

                x--;
                coefficientAngle = -( 1 / 2 + x );
            }

        } else { // nombre de cartes impaire (2)

            coefficientAngle = -indiceCarteMilieu;

            //for (int i = 0; i < indiceCarteMilieu; i++) {	// les cartes à gauche
            for ( int i = 0; i < cartes.size(); i++ ) {

                carte = cartes.get( i );
                if ( JoueurPainter.getOffsetAxis( position ) == 'x' ) {
                    carte.x = initialX + coefficientAngle * offset;
                    carte.y = initialY;
                } else { // y
                    carte.x = initialX;
                    carte.y = initialY + coefficientAngle * offset;
                }

                carte.jouable = carte.compatible( talon.sommet() );

                if ( this instanceof Humain ) {
                    //if (id == Jeu.tour) {

                    carte.rotate( JoueurPainter.getAngleCorrection( position ) * coefficientAngle * angle
                            + JoueurPainter.getAdditionalRotation( position ) );
                    carte.render( g );

                } else {

                    hiddenCardImg.setRotation( JoueurPainter.getAngleCorrection( position ) * coefficientAngle * angle
                            + JoueurPainter.getAdditionalRotation( position ) );
                    hiddenCardImg.draw( carte.x, carte.y );

                }

                //				carte = cartes.get(i);
                //				carte.jouable = carte.compatible(talon.sommet());

                //				if (id != Jeu.tour) {	// si ce n'est pas le tour du joueur, cacher ses cartes !
                //					oldCardImage = carte.image;	// saving the old card image ...
                //					carte.image = Sprite.getHiddenCard();
                //					carte.jouable = true;	// on doit cacher aussi si la carte est jouable ou pas !
                //				}

                //				if (JoueurPainter.getOffsetAxis(position) == 'x') {
                //					carte.x = initialX + coefficientAngle * offset;
                //					carte.y = initialY;
                //				} else {	// y
                //					carte.x = initialX;
                //					carte.y = initialY + coefficientAngle * offset;
                //				}

                //				carte.rotate(JoueurPainter.getAngleCorrection(position) * coefficientAngle * angle + JoueurPainter.getAdditionalRotation(position));
                //				carte.render(g);

                //				if (id != Jeu.tour)
                //					carte.image = oldCardImage;	// ... in order to return it back to the card after rendering !

                coefficientAngle++;
            }

            /*
            
            // la carte au milieu ( coefficientAngle == 0 )
            carte = cartes.get(indiceCarteMilieu);
            carte.jouable = carte.compatible(talon.sommet());
            
            if (id != Jeu.tour) {	// si ce n'est pas le tour du joueur, cacher ses cartes !
            	oldCardImage = carte.image;
            	carte.image = Sprite.getHiddenCard();
            	carte.jouable = true;	// on doit cacher aussi si la carte est jouable ou pas !
            }
            
            carte.x = initialX;
            carte.y = initialY;
            
            carte.rotate(JoueurPainter.getAngleCorrection(position) * coefficientAngle * angle + JoueurPainter.getAdditionalRotation(position));
            carte.render(g);
            
            if (id != Jeu.tour)
            	carte.image = oldCardImage;	// ... in order to return it back to the card after rendering !
            
            coefficientAngle++;
            
            for (int i = indiceCarteMilieu + 1; i < cartes.size(); i++) {	// les cartes à droite
            
            	carte = cartes.get(i);
            	carte.jouable = carte.compatible(talon.sommet());
            	
            	if (id != Jeu.tour) {	// si ce n'est pas le tour du joueur, cacher ses cartes !
            		oldCardImage = carte.image;
            		carte.image = Sprite.getHiddenCard();
            		carte.jouable = true;	// on doit cacher aussi si la carte est jouable ou pas !
            	}
            	
            	if (JoueurPainter.getOffsetAxis(position) == 'x') {
            		carte.x = initialX + coefficientAngle * offset;
            		carte.y = initialY;
            	} else {	// y
            		carte.x = initialX;
            		carte.y = initialY + coefficientAngle * offset;
            	}
            	
            	carte.rotate(JoueurPainter.getAngleCorrection(position) * coefficientAngle * angle + JoueurPainter.getAdditionalRotation(position));
            	carte.render(g);
            	
            	if (id != Jeu.tour)
            		carte.image = oldCardImage;	// ... in order to return it back to the card after rendering !
            
            	coefficientAngle++;
            }
            */

        }
    }

    /**
     * contrairement à nbCartesJouables(), cette fonction doit être publique
     * les autres joueurs peuvent voir combien vous avez de cartes dans la main
     * @return le nombre de cartes que posséde le joueur dans sa main
     */
    public int nbCartes() {
        return main.nbCartes();
    }

    /**
     * cette méthode permet d'afficher les cartes dans la main du joueur courant
     */
    public void afficherMain() {
        String str = "";
        ArrayList<Carte> cartes = main.getCartes();
        if ( cartes.isEmpty() ) {
            str = "[VIDE]";
        }
        for ( int i = 0; i < cartes.size(); ++i ) {
            Carte carte = cartes.get( i );
            str = str + i + ") " + carte.toString();
            if ( carte.compatible( talon.sommet() ) ) {
                str = str + " [Jouable]";
            }
            if ( i != cartes.size() - 1 ) { // if this isn't the last iteration
                str = str + "\n"; // add a new line
            }
        }
        System.out.println( str );
    }

    /**
     * cette méthode doit être privé ! seul le joueur doit connaître combien il a de cartes jouables !
     * @return le nombre de cartes jouables çàd : compatibles avec le sommet du talon 
     */
    protected int nbCartesJouables() {
        int n = 0;
        ArrayList<Carte> cartes = main.getCartes();
        if ( cartes.isEmpty() ) {
            return 0;
        }
        Carte sommetTalon = talon.sommet();
        for ( int i = 0; i < cartes.size(); ++i ) {
            Carte carte = cartes.get( i );
            if ( carte.compatible( sommetTalon ) ) {
                n++;
            }
        }
        return n;
    }

    /**
     * permet d'attendre un peu pour que l'utilisateur arrive à lire le message affiché
     */
    private void pause() {
        try {
            Thread.sleep( 3000 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    /**
     * permet au joueur de jouer son tour
     * @throws InterruptedException 
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     * @throws SlickException 
     */
    public void jouerTour() throws InterruptedException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException, SlickException { // TODO : ajouter le cas où on a des doublons ! on doit se débarasser de toutes les occurences de la carte jouée !
        playedCard = null;
        talon.afficherSommet();
        afficherMain(); // sur la console
        if ( nbCartesJouables() == 0 ) {
            System.out.println( "Vous n'avez pas de cartes jouables ! vous devez piocher !" );
            Audio.playSound( "noPlayableCardsSound" );
            pause();
            Carte c = prendreCarte();
            System.out.println( "La carte piochée est : " + c );
            if ( !c.compatible( talon.sommet() ) ) { // la carte récemment piochée n'est pas compatible avec le sommet du talon
                System.out.println(
                        "Pas de chance ! vous n'avez encore pas de cartes jouables, vous devez passer le tour" );
                System.out.println( "----------------------------------" );
                Audio.playSound( "hardLuckSound" );
                return; // passer le tour <=> quitter la méthode
            } else {
                afficherMain(); // sur la console
            }
        }
        jouerCarte();
        System.out.println( "----------------------------------" );
    }

    public abstract void jouerCarte() throws InterruptedException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException, SlickException;

    @Override
    public void render( Graphics g ) throws SlickException {
        this.afficherMain( g );
        // afficher le nom du joueur
        int xPos, yPos, angleRotation;
        final int offsetHautBas = 50, offsetDroiteGauche = 50;
        String playerName = this.pseudo + " (" + nbCartes() + ")";

        Color oldColor = g.getColor();
        int tour = Jeu.tour; // pour assurer que la valeur de Jeu.tour n'a pas été changé !
        if ( id == tour ) {
            // si c'est le joueur courant, on change la couleur de son nom pour indiquer le sens du jeu
            //oldColor = g.getColor();
            g.setColor( Color.black );
        }
        switch ( position ) {
        case BAS:
            g.drawString( playerName,
                    Game.WIDTH / 2 - g.getFont().getWidth( playerName ) / 2,
                    Game.HEIGHT - Carte.HEIGHT - offsetHautBas );
            break;
        case GAUCHE:
            xPos = Carte.HEIGHT + offsetDroiteGauche;
            yPos = Game.HEIGHT / 2 - g.getFont().getWidth( playerName ) / 2;
            angleRotation = 90;

            g.rotate( xPos, yPos, angleRotation ); // rotating
            g.drawString( playerName,
                    xPos,
                    yPos );
            g.rotate( xPos, yPos, -angleRotation ); // reset rotation
            break;
        case HAUT:
            xPos = Game.WIDTH / 2 + g.getFont().getWidth( playerName ) / 2;
            yPos = Carte.HEIGHT + offsetHautBas;
            angleRotation = 180;
            g.rotate( xPos, yPos, angleRotation ); // rotating
            g.drawString( playerName,
                    xPos,
                    yPos );
            g.rotate( xPos, yPos, -angleRotation ); // reset rotation
            break;
        case DROITE:
            xPos = Game.WIDTH - Carte.HEIGHT - offsetDroiteGauche;
            yPos = Game.HEIGHT / 2 + g.getFont().getWidth( playerName ) / 2; // -1/2 textWidth + textWidth
            angleRotation = 270;
            g.rotate( xPos, yPos, angleRotation ); // rotating
            g.drawString( playerName,
                    xPos,
                    yPos );
            g.rotate( xPos, yPos, -angleRotation ); // reset rotation
            break;
        default:
            break;
        }
        if ( id == tour ) {
            // si c'est le joueur courant, on change la couleur de son nom pour indiquer le sens du jeu
            g.setColor( oldColor );
        }
    }

}

/**
 * cette classe va être utilisé en tant que <<Utility>> pour aider
 * la classe Joueur dans le dessin / render du joueur
 * @author Stoufa
 *
 */
class JoueurPainter {

    public static final int offsetHautBas = 10, offsetDroiteGauche = 10;

    public static float getStartPosX( Position position ) {
        switch ( position ) {
        case BAS:
            return Game.WIDTH / 2 - Carte.WIDTH / 2;
        case DROITE:
            return Game.WIDTH - Carte.HEIGHT + offsetDroiteGauche;
        case HAUT:
            return Game.WIDTH / 2 - Carte.WIDTH / 2;
        case GAUCHE:
            return 25 + offsetDroiteGauche; // 25 pour ne pas compter les bordures de la fenêtre
        }
        return 0;
    }

    public static float getStartPosY( Position position ) {
        switch ( position ) {
        case BAS:
            return Game.HEIGHT - Carte.HEIGHT - offsetHautBas;
        case DROITE:
            return Game.HEIGHT / 2 - Carte.WIDTH / 2;
        case HAUT:
            //return 0 - Carte.WIDTH / 2;
            return offsetHautBas;
        case GAUCHE:
            return Game.HEIGHT / 2 - Carte.WIDTH / 2;
        }
        return 0;
    }

    public static float getAdditionalRotation( Position position ) {
        switch ( position ) {
        case BAS:
            return 0;
        case DROITE:
            return -90;
        case HAUT:
            return 180;
        case GAUCHE:
            return 90;
        }
        return 0;
    }

    public static char getOffsetAxis( Position position ) {
        switch ( position ) {
        case BAS:
        case HAUT:
            return 'x';
        case DROITE:
        case GAUCHE:
            return 'y';
        }
        return 0;
    }

    public static int getAngleCorrection( Position position ) {
        switch ( position ) {
        case BAS:
        case GAUCHE:
            return 1;
        case HAUT:
        case DROITE:
            return -1;
        }
        return 0;
    }
}

package main.gameObjects;

import java.awt.Point;
import java.util.concurrent.CountDownLatch;

import javax.swing.UnsupportedLookAndFeelException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import main.common.Debug;
import main.io.Audio;

/**
 * classe représentant un joueur humain
 * @author Stoufa
 *
 */
public class Humain extends Joueur {

    public Humain( String pseudo, Pioche pioche, Talon talon ) {
        super( pseudo, pioche, talon );
    }

    /**
     * permet d'attendre un peu pour que l'utilisateur arrive à lire le message affiché
     * utilisé dans la version console du jeu
     */
    //	private void pause() {
    //		try {
    //			Thread.sleep(3000);
    //		} catch (InterruptedException e) {
    //			e.printStackTrace();
    //		}
    //	}

    /**
     * permet au joueur de jouer une carte
     */
    //TODO
    //	private void jouerCarte() {
    //		boolean carteJouable = false;
    //		int num = -1;
    //		ArrayList<Carte> cartes = main.getCartes();
    //		
    //		System.out.println("Vous avez " + nbCartesJouables() + " cartes jouables");
    //		while (!carteJouable) {	// tant que la carte n'est pas jouable
    //			num = IO.lireEntier(cartes);
    //			Carte carteAjouer = cartes.get(num);
    //			if (carteAjouer.compatible(talon.sommet())) {
    //				carteJouable = true;
    //			} else {
    //				System.out.println(carteAjouer + " ne peut pas être jouée sur " + talon.sommet());
    //			}
    //		}
    //		
    //		Carte carte = main.retirer(num);
    //		if (carte.getCouleur() == Couleur.NOIR) {	// TODO : ce test doit être déléguée à la classe Jeu
    //			// On doit demander une couleur au joueur
    //			System.out.println("Vous devez choisir une couleur");
    //			Couleur couleur = donnerCouleur();
    //			// Si elle est de couleur noir, on est sûr qu'elle est spéciale !
    //			((CarteSpecial) carte).setCouleur(couleur);
    //		}
    //		talon.empiler(carte);
    //		System.out.println(pseudo + " a joué " + carte);
    //	}

    /**
     * permet au joueur de donner une couleur
     * @return la couleur choisie
     */
    //	private Couleur donnerCouleur() {
    //		HashMap<Integer, Couleur> menu = new HashMap<>();
    //		int i = -1;
    //		for(Couleur couleur : Couleur.values()) {
    //			if (couleur != Couleur.NOIR) {	// la couleur doit être différente de NOIR
    //				i++;
    //				menu.put(i, couleur);
    //				System.out.println(i + ") " + couleur.getValeur());
    //			}
    //		}
    //		int choix = IO.lireEntier(0, i);	// le choix doit être entre 0 et i
    //		return menu.get(choix);
    //	}

    /**
     * cette méthode doit être privé ! seul le joueur doit connaître combien il a de cartes jouables !
     * @return le nombre de cartes jouables çàd : compatibles avec le sommet du talon 
     */
    //	private int nbCartesJouables() {
    //		int n = 0;
    //		ArrayList<Carte> cartes = main.getCartes();
    //		if (cartes.isEmpty()) {
    //			return 0;
    //		}
    //		Carte sommetTalon = talon.sommet();
    //		for(int i = 0; i < cartes.size(); ++i) {
    //			Carte carte = cartes.get(i);
    //			if (carte.compatible(sommetTalon)) {
    //				n++;
    //			}
    //		}
    //		return n;
    //	}

    /**
     * @return le pseudo du joueur courant
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * @return chaîne décrivant le joueur en cours
     * le joueur est identifié par son pseudo
     */
    @Override
    public String toString() {
        return "[Humain] : " + getPseudo();
    }

    @Override
    public void render( Graphics g ) throws SlickException {
        //this.afficherMain(g);
        super.render( g );
    }

    /**
     * permet au joueur de donner une couleur
     * @return la couleur choisie
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     * @throws InterruptedException 
     * @throws SlickException 
     */
    private Couleur donnerCouleur() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException, InterruptedException, SlickException {
        // Here we should show the dialog and wait for a response from the user
        Jeu.waitForDialogCountDownLatch = new CountDownLatch( 1 ); // used to wait the dialog

        //		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //GetColorDialog dialog = new GetColorDialog();
        //dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //dialog.setUndecorated(true);	// to hide the top-bar ( containing the X close button )
        //		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);	// to prevent closing the window without chosing !
        //		dialog.setVisible(true);
        //		dialog.addWindowListener(new WindowListener() {
        //			@Override
        //			public void windowOpened(WindowEvent e) {}
        //			@Override
        //			public void windowIconified(WindowEvent e) {}
        //			@Override
        //			public void windowDeiconified(WindowEvent e) {}
        //			@Override
        //			public void windowDeactivated(WindowEvent e) {}
        //			@Override
        //			public void windowClosing(WindowEvent e) {}
        //			@Override
        //			public void windowClosed(WindowEvent e) {	// when the window is closed
        //				waitForDialogCountDownLatch.countDown(); // release the dialog
        //			}
        //			@Override
        //			public void windowActivated(WindowEvent e) {}
        //		});

        Jeu.dialog.selectedColor = null; // reset chosen color value
        Jeu.dialog.setVisible( true ); // showing dialog

        //Jeu.waitForDialogCountDownLatch = new CountDownLatch(1);	// réinitialisation du bloqueur
        Debug.log( "Waiting for dialog..." );
        Jeu.waitForDialogCountDownLatch.await(); // hold

        /*
        String[] colorNames = { "Jaune", "Vert", "Bleu", "Rouge" };
        String selectedColor = null;
        
        do {
        	selectedColor = (String) JOptionPane.showInputDialog(
        			null,	// parentComponent
        			"",	// message
        			"Choisissez une couleur",	// title
        			JOptionPane.QUESTION_MESSAGE, null, // Use default icon
        			colorNames, // Array of choices
        			colorNames[0]); // Initial choice
        	if (selectedColor == null) {
        		Debug.log("Vous devez choisir une couleur !");
        	}
        } while (selectedColor == null);
        */

        Debug.log( "Dialog is done..." );

        //Jeu.dialog.setVisible(false);	// hiding dialog
        // TODO : add blocking loop
        //		Jeu.colorReceived = false;
        //		while (!Jeu.colorReceived) {	// waiting for a color
        //			Thread.sleep(1000);
        //		}

        Debug.log( "color chosen : " + Jeu.dialog.selectedColor );
        //Debug.log("color chosen : " + selectedColor);
        Couleur couleur = Couleur.getCouleur( Jeu.dialog.selectedColor );
        return couleur;
    }

    /**
     * permet au joueur de jouer une carte
     * @throws InterruptedException 
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     * @throws SlickException 
     */
    @Override
    public void jouerCarte() throws InterruptedException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException, SlickException {
        //		boolean carteJouable = false;
        //		int num = -1;
        //		ArrayList<Carte> cartes = main.getCartes();

        System.out.println( "Vous avez " + nbCartesJouables() + " cartes jouables" );

        // Ici, on doit attendre le joueur jusqu'a qu'il clique sur une carte jouable !
        Debug.log( "Waiting for a click ..." );

        Jeu.countDownLatch = new CountDownLatch( 1 ); // réinitialisation
        Jeu.countDownLatch.await(); // DOWN : arrêter le jeu en attendant le clique !
        // TODO : add blocking loop
        //		Jeu.clickReceived = false;
        //		while (!Jeu.clickReceived) {	// waiting for a click
        //			Thread.sleep(1000);
        //		}

        Debug.log( "Click received ! ..." );
        // a playable card is played !
        //Debug.log(playedCard);

        //		while (!carteJouable) {	// tant que la carte n'est pas jouable
        //			num = Clavier.lireEntier(cartes);
        //			Carte carteAjouer = cartes.get(num);
        //			if (carteAjouer.compatible(talon.sommet())) {
        //				carteJouable = true;
        //			} else {
        //				System.out.println(carteAjouer + " ne peut pas être jouée sur " + talon.sommet());
        //			}
        //		}

        //		Carte carte = main.retirer(num);
        //Carte carte = this.playedCard;

        //Debug.log("line 398 : " + this.playedCard);
        // main.nbCartes() > 1 : si la derniére carte jouée est une carte noire ! on n'a pas besoin
        // de séléctionner une couleur ! il est gagnant quelque soit la couleur séléctionnée !
        // main.nbCartes() > 1 et non pas main.nbCartes() > 0 car la carte jouée n'a pas encore été retiré de la main
        if ( main.nbCartes() > 1 && playedCard.couleur == Couleur.NOIR ) { // TODO : ce test doit être déléguée à la classe Jeu
            // On doit demander une couleur au joueur
            System.out.println( "Vous devez choisir une couleur" );
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
        System.out.println( pseudo + " a joué " + playedCard );
    }

    @Override
    public void update( GameContainer container ) throws SlickException {
        //		// test
        //		Debug.log("id = " + this.id);
        //		Debug.log(main.toString());
        if ( id != Jeu.tour ) {
            return; // ce n'est pas le tour de ce joueur !
        }
        //Input input = container.getInput();
        if ( Jeu.input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) ) { // click detected
            Debug.log( "update() / id = " + this.id );
            int mx = Jeu.input.getMouseX(), my = Jeu.input.getMouseY();
            for ( int i = main.cartes.size() - 1; i >= 0; --i ) {
                Carte carte = main.cartes.get( i );
                if ( carte.isClicked( new Point( mx, my ) ) ) { // the click was on one of the cards
                    if ( carte.jouable ) { // if the card is playable
                        //Debug.log(carte.toString());
                        System.out.println( "card clicked !" );
                        if ( carte instanceof CarteChiffre ) {
                            // play the sound only for numerical cards !
                            // special cards have special sound effects and we
                            // don't want to mix them up !
                            Audio.playSound( "clickSound" );
                        }
                        this.playedCard = carte; // saving the played card

                        Debug.log( "Waiting for a card click..." );
                        Jeu.countDownLatch.countDown(); // UP : release the block, MUST BE AFTER SETTING THE CARD OR ELSE NullException !
                        Debug.log( "Card click detected!" );
                        //Jeu.clickReceived = true;	// release the block, MUST BE AFTER SETTING THE CARD OR ELSE NullException !

                        // Puis on doit attendre si la carte est une carte noire !

                        // si la carte choisie est une carte Noir ( On ne doit pas libérer le 
                        // jeu jusqu'a ce que le joueur choisisse une couleur )
                        //						if (playedCard.couleur != Couleur.NOIR) {
                        //						}
                        // TODO : add a different sound if the card isn't playable :)
                        //carte.jouable = !carte.jouable;	// test
                        System.out.println( carte );
                        break; // stop propagating the click event !
                    } else {
                        Debug.log( "This card is not playable !" );
                        Audio.playSound( "invalidClickSound" );
                        break; // break the loop ! no need to check for the other cards !
                    }
                }
            }
            Debug.log( "==================================================" );
        }
    }

}

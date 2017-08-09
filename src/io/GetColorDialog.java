package main.io;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import main.gameObjects.Jeu;

/**
 * Classe représentant la fenêtre de choix de couleurs
 * ( Créée à l'aide de WindowBuilder )
 * @author Stoufa
 *
 */
public class GetColorDialog extends JDialog {

    private static final long serialVersionUID = -8889505211213199643L;
    private final JPanel      contentPanel     = new JPanel();
    private JButton           btnBleu;
    private JButton           btnJaune;
    private JButton           btnRouge;
    private JButton           btnVert;

    public String             selectedColor;                           // nom du couleur choisie

    /**
     * Constructeur
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     */
    public GetColorDialog() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        initComponents();
        createEvents();
    }

    private void createEvents() {
        // ajout des événements des boutons
        btnBleu.addActionListener( new GetColorDialogBtnActionListener( this, "Bleu" ) );
        btnJaune.addActionListener( new GetColorDialogBtnActionListener( this, "Jaune" ) );
        btnRouge.addActionListener( new GetColorDialogBtnActionListener( this, "Rouge" ) );
        btnVert.addActionListener( new GetColorDialogBtnActionListener( this, "Vert" ) );
    }

    private void initComponents() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE ); // pour empêcher la fermeture de la fenêtre sans choisir une couleur
        //setVisible(true);	// Ivisible par défaut
        addWindowListener( new WindowListener() {
            @Override
            public void windowOpened( WindowEvent e ) {
            }

            @Override
            public void windowIconified( WindowEvent e ) {
            }

            @Override
            public void windowDeiconified( WindowEvent e ) {
            }

            @Override
            public void windowDeactivated( WindowEvent e ) {
            }

            @Override
            public void windowClosing( WindowEvent e ) {
            }

            @Override
            public void windowClosed( WindowEvent e ) { // quand la fenêtre se ferme
                Jeu.waitForDialogCountDownLatch.countDown(); // libérer le sémaphore
            }

            @Override
            public void windowActivated( WindowEvent e ) {
            }
        } );

        setTitle( "Choisissez une couleur" );
        setResizable( false );
        setBounds( 100, 100, 285, 77 );
        setLocationRelativeTo( null );

        getContentPane().setLayout( new BorderLayout() );
        contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
        getContentPane().add( contentPanel, BorderLayout.CENTER );

        btnBleu = new JButton( "Bleu" );

        btnBleu.setForeground( Color.BLACK );
        btnBleu.setBackground( Color.BLUE );

        btnJaune = new JButton( "Jaune" );

        btnJaune.setBackground( Color.YELLOW );
        btnJaune.setForeground( Color.BLACK );

        btnRouge = new JButton( "Rouge" );

        btnRouge.setBackground( Color.RED );
        btnRouge.setForeground( Color.BLACK );

        btnVert = new JButton( "Vert" );

        btnVert.setForeground( Color.BLACK );
        btnVert.setBackground( Color.GREEN );
        GroupLayout gl_contentPanel = new GroupLayout( contentPanel );
        gl_contentPanel.setHorizontalGroup(
                gl_contentPanel.createParallelGroup( Alignment.LEADING )
                        .addGroup( gl_contentPanel.createSequentialGroup()
                                .addContainerGap()
                                .addComponent( btnBleu )
                                .addPreferredGap( ComponentPlacement.RELATED )
                                .addComponent( btnJaune )
                                .addPreferredGap( ComponentPlacement.RELATED )
                                .addComponent( btnRouge )
                                .addPreferredGap( ComponentPlacement.RELATED )
                                .addComponent( btnVert )
                                .addContainerGap( 130, Short.MAX_VALUE ) ) );
        gl_contentPanel.setVerticalGroup(
                gl_contentPanel.createParallelGroup( Alignment.LEADING )
                        .addGroup( gl_contentPanel.createSequentialGroup()
                                .addContainerGap()
                                .addGroup( gl_contentPanel.createParallelGroup( Alignment.BASELINE )
                                        .addComponent( btnBleu )
                                        .addComponent( btnJaune )
                                        .addComponent( btnRouge )
                                        .addComponent( btnVert ) )
                                .addContainerGap( 184, Short.MAX_VALUE ) ) );
        contentPanel.setLayout( gl_contentPanel );
    }
}

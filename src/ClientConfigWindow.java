package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class ClientConfigWindow extends JDialog {

    private static final long serialVersionUID = 3481165213363744732L;
    private final JPanel      contentPanel     = new JPanel();
    private JSpinner          spnLargeur;
    private JSpinner          spnLongueur;
    private JCheckBox         chckbxJouerMusique;
    private JCheckBox         chckbxJouerSons;
    private JButton           btnEnregistrerConfig;

    private String            configFilePath   = "config.ini";

    /**
     * Launch the application.
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     */
    public static void main( String[] args ) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException {
        new ClientConfigWindow();
        //ClientConfigWindow dialog = new ClientConfigWindow();
        //dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //dialog.setVisible(true);
    }

    /**
     * Create the dialog.
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     */
    public ClientConfigWindow() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        initComponents();
        loadConfig();
        createEvents();
    }

    private void createEvents() {
        btnEnregistrerConfig.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent arg0 ) {
                Properties p = new Properties();

                InputStream is;
                try {
                    // Pour ne pas perdre les anciennes valeurs !
                    is = new FileInputStream( configFilePath );
                    p.load( is );
                } catch ( IOException e1 ) {
                    e1.printStackTrace();
                }

                OutputStream os = null;
                try {
                    os = new FileOutputStream( configFilePath );
                    p.setProperty( "largeur", String.valueOf( spnLargeur.getValue() ) );
                    p.setProperty( "longueur", String.valueOf( spnLongueur.getValue() ) );
                    p.setProperty( "jouerMusique", String.valueOf( chckbxJouerMusique.isSelected() ) );
                    p.setProperty( "jouerSons", String.valueOf( chckbxJouerSons.isSelected() ) );
                    p.store( os, null );

                    // Change the button text
                    Color oldBgColor = btnEnregistrerConfig.getBackground(),
                            oldTextColor = btnEnregistrerConfig.getForeground();

                    btnEnregistrerConfig.setBackground( Color.blue );
                    btnEnregistrerConfig.setForeground( Color.blue );
                    btnEnregistrerConfig.setText( " == Enregistré == " );

                    // After 2 seconds, change it back
                    Timer timer = new Timer( 2000, new ActionListener() {
                        @Override
                        public void actionPerformed( ActionEvent arg0 ) {
                            btnEnregistrerConfig.setText( "Enregistrer configuration" );
                            btnEnregistrerConfig.setBackground( oldBgColor );
                            btnEnregistrerConfig.setForeground( oldTextColor );
                        }
                    } );
                    timer.setRepeats( false );
                    timer.start();

                } catch ( Exception e ) {
                    e.printStackTrace();
                } finally {
                    try {
                        os.close();
                    } catch ( IOException e ) {
                        e.printStackTrace();
                    }
                }

            }
        } );
    }

    private void loadConfig() {
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream( configFilePath );
            p.load( is );
            p.list( System.out );

            int largeur, longueur;
            boolean jouerMusique, jouerSons;
            largeur = Integer.parseInt( p.getProperty( "largeur" ) );
            longueur = Integer.parseInt( p.getProperty( "longueur" ) );
            jouerMusique = Boolean.parseBoolean( p.getProperty( "jouerMusique" ) );
            jouerSons = Boolean.parseBoolean( p.getProperty( "jouerSons" ) );

            //			System.out.println("jouerMusique: " + jouerMusique);
            //			System.out.println("jouerSons: " + jouerSons);

            spnLargeur.setValue( largeur );
            spnLongueur.setValue( longueur );
            chckbxJouerMusique.setSelected( jouerMusique );
            chckbxJouerSons.setSelected( jouerSons );

        } catch ( Exception e ) {
            e.printStackTrace();
            //System.err.println("File not found: " + configFilePath);
        } finally {
            try {
                if ( is != null ) {
                    is.close();
                }
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    private void initComponents() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        setIconImage( Toolkit.getDefaultToolkit()
                .getImage( ClientConfigWindow.class.getResource( "/test/test3/server/settings_32.png" ) ) );
        setTitle( "UNO - Configuration" );
        setBounds( 100, 100, 284, 212 );
        setLocationRelativeTo( null );
        getContentPane().setLayout( new BorderLayout() );
        contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
        getContentPane().add( contentPanel, BorderLayout.CENTER );
        contentPanel.setLayout( null );

        JLabel lblLargeurFentre = new JLabel( "Largeur Fen\u00EAtre" );
        lblLargeurFentre.setBounds( 10, 35, 106, 14 );
        contentPanel.add( lblLargeurFentre );

        JLabel lblLongueurFentre = new JLabel( "Longueur Fen\u00EAtre" );
        lblLongueurFentre.setBounds( 10, 66, 106, 14 );
        contentPanel.add( lblLongueurFentre );

        spnLargeur = new JSpinner();
        spnLargeur.setModel( new SpinnerNumberModel( new Integer( 800 ), new Integer( 600 ), null, new Integer( 1 ) ) );
        spnLargeur.setBounds( 136, 32, 122, 20 );
        contentPanel.add( spnLargeur );

        spnLongueur = new JSpinner();
        spnLongueur
                .setModel( new SpinnerNumberModel( new Integer( 600 ), new Integer( 600 ), null, new Integer( 1 ) ) );
        spnLongueur.setBounds( 136, 63, 122, 20 );
        contentPanel.add( spnLongueur );

        chckbxJouerMusique = new JCheckBox( "Jouer Musique" );
        chckbxJouerMusique.setSelected( true );
        chckbxJouerMusique.setBounds( 161, 113, 97, 23 );
        contentPanel.add( chckbxJouerMusique );

        chckbxJouerSons = new JCheckBox( "Jouer Sons" );
        chckbxJouerSons.setSelected( true );
        chckbxJouerSons.setBounds( 10, 113, 97, 23 );
        contentPanel.add( chckbxJouerSons );

        btnEnregistrerConfig = new JButton( "Enregistrer configuration" );

        btnEnregistrerConfig.setBounds( 10, 143, 248, 23 );
        contentPanel.add( btnEnregistrerConfig );

        setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        setVisible( true );
    }
}

package cti.teleskop;

/**
 * Created by Teleskop on 2016-11-08.
 */


import edsdk.api.*;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Main {

    static final CanonCamera camera = new CanonCamera();
//    public static void main(String[] args) {

//        CanonCamera slr = new CanonCamera();
//        slr.openSession();
//        int isoSteps = 0;
//
//        List<CanonConstants.EdsISOSpeed> isoSpeeds = new ArrayList<>();
//        List<CanonConstants.EdsISOSpeed> forbiddenIsoSpeeds = new ArrayList<>();
//        forbiddenIsoSpeeds.add(CanonConstants.EdsISOSpeed.kEdsISOSpeed_6);
//        forbiddenIsoSpeeds.add(CanonConstants.EdsISOSpeed.kEdsISOSpeed_12);
//        forbiddenIsoSpeeds.add(CanonConstants.EdsISOSpeed.kEdsISOSpeed_25);
//        forbiddenIsoSpeeds.add(CanonConstants.EdsISOSpeed.kEdsISOSpeed_50);
//        forbiddenIsoSpeeds.add(CanonConstants.EdsISOSpeed.kEdsISOSpeed_51200);
//        forbiddenIsoSpeeds.add(CanonConstants.EdsISOSpeed.kEdsISOSpeed_102400);
//        forbiddenIsoSpeeds.add(CanonConstants.EdsISOSpeed.kEdsISOSpeed_Auto);
//        forbiddenIsoSpeeds.add(CanonConstants.EdsISOSpeed.kEdsISOSpeed_Unknown);
//
//        for (CanonConstants.EdsISOSpeed value: CanonConstants.EdsISOSpeed.values()) {
//            if (!forbiddenIsoSpeeds.contains(value)) {
//                isoSteps++;
//                isoSpeeds.add(value);
//            }
//        }
//
//        //System.out.print(slr.getAvailableISOSpeeds()); // nie dziala -> null
//
//        System.out.println(isoSpeeds);
//
//        slr.setDriveMode(CanonConstants.EdsDriveMode.kEdsDriveMode_HighSpeedContinuous); //xD
//
//
//        for (int i=0;i < isoSteps; i++) {
//            slr.setISOSpeed(isoSpeeds.get(i));
//            try {
//                Thread.sleep(100); //rzuca wyjatkiem po paru zdjeciach, ciekawe czemu, moze nie wykluczylem wszystkich niepoprawnych iso czy cos
//            } catch (InterruptedException ie) {}
//            slr.shoot();
//            System.out.println(slr.getISOSpeed());
//        }
//
//
//        slr.closeSession();
//    }


    public static void main( final String[] args ) throws InterruptedException {
        if ( camera.openSession() ) {
            if ( camera.beginLiveView() ) {
                final JFrame frame = new JFrame( "Live view" );
                final Dimension liveViewDims = new Dimension(1280,960);
                final JLabel label = new JLabel();

                final JPanel panel = new JPanel(new GridBagLayout());
                addShootButton(panel);

                double width = frame.getContentPane().getBounds().getWidth();
                double height = frame.getContentPane().getBounds().getHeight();
                double x = frame.getContentPane().getBounds().getX();
                double y = frame.getContentPane().getBounds().getY();

                frame.getContentPane().setBounds((int)x, (int)y, (int)width, (int)height);
                frame.getContentPane().add( label, BorderLayout.CENTER );
                frame.getContentPane().add(panel, BorderLayout.SOUTH);

                frame.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
                frame.addWindowListener( new WindowAdapter() {

                    @Override
                    public void windowClosing( final WindowEvent e ) {
                        camera.endLiveView();
                        camera.closeSession();
                        CanonCamera.close();
                        System.exit( 0 );
                    }
                } );

                alignFrameToLeft(frame, liveViewDims);
                frame.setVisible( true );

                while ( true ) {

                    Thread.sleep( 50 );
                    final BufferedImage image = camera.downloadLiveView();


                    if ( image != null ) {
                        System.out.println(label.getBounds());
                        label.setIcon( new ImageIcon( image.getScaledInstance(1280, 960, Image.SCALE_SMOOTH) ) );
                        frame.pack();
                        image.flush();
                    }

                }
            }
            camera.closeSession();
        }
        CanonCamera.close();
        System.exit( 0 );
    }

    private static void addShootButton(JPanel gridPanel) {
        JButton button = new JButton("Take a snap!");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.shoot();
                camera.beginLiveView();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gridPanel.add(button, gbc);
    }

    private static void alignFrameToLeft(JFrame frame, Dimension liveViewDims) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(dimension.getWidth() - liveViewDims.getWidth()), frame.getLocation().y);
    }
}

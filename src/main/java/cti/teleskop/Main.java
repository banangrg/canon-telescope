package cti.teleskop;

/**
 * Created by Teleskop on 2016-11-08.
 */


import edsdk.api.*;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Main {

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
        final CanonCamera camera = new CanonCamera();
        if ( camera.openSession() ) {
            if ( camera.beginLiveView() ) {
                final JFrame frame = new JFrame( "Live view" );
                final JLabel label = new JLabel();

                double newWidth = frame.getContentPane().getBounds().getWidth() * 2;
                double newHeight = frame.getContentPane().getBounds().getHeight() * 2;
                double x = frame.getContentPane().getBounds().getX();
                double y = frame.getContentPane().getBounds().getY();

                frame.getContentPane().setBounds((int)x, (int)y, (int)newWidth, (int)newHeight);
                frame.getContentPane().add( label, BorderLayout.CENTER );
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
}

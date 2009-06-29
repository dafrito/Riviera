package com.bluespot.conversation.packet;

import java.awt.Dimension;

import javax.swing.JComponent;

import net.sourceforge.jpcap.capture.CaptureDeviceOpenException;

import com.bluespot.demonstration.Demonstration;

public class PacketSniffer extends Demonstration {

    @Override
    protected JComponent newContentPane() {
        try {
            final PacketGUI gui = new PacketGUI("\\Device\\NPF_{472DCB53-78C7-4CE3-8E76-55CC50453F98}");
            gui.setPreferredSize(new Dimension(800, 600));
            // TODO Add exit listener
            return gui;
        } catch (final CaptureDeviceOpenException e1) {
            throw new RuntimeException(e1);
        }
    }

    public static void main(final String[] args) {
        Demonstration.launch(PacketSniffer.class);
    }

}

package com.bluespot.conversation.packet;

import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;

import net.sourceforge.jpcap.capture.CaptureDeviceOpenException;
import net.sourceforge.jpcap.capture.CapturePacketException;
import net.sourceforge.jpcap.capture.PacketCapture;
import net.sourceforge.jpcap.capture.RawPacketListener;
import net.sourceforge.jpcap.net.RawPacket;

import com.bluespot.collections.observable.list.ObservableList;

public final class PacketGUI extends JPanel {

    private static final long serialVersionUID = -4321754111518166903L;

    private final List<RawPacket> packets;

    private final PacketCapture capture;

    public List<RawPacket> getPackets() {
        return this.packets;
    }

    private class CaptureTask implements Runnable {

        public CaptureTask() {
            // Do nothing; just ensure construction is allowed
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    PacketGUI.this.capture.capture(1);
                }
            } catch (final CapturePacketException e) {
                e.printStackTrace();
            } finally {
                PacketGUI.this.capture.close();
            }
        }
    }

    public PacketGUI(final String device) throws CaptureDeviceOpenException {
        super();
        final ObservableList<RawPacket> listModel = new ObservableList<RawPacket>();
        this.packets = listModel;

        new Thread(new CaptureTask()).start();

        this.add(new JList(listModel));
        this.capture = new PacketCapture();
        this.capture.open(device, true);

        this.capture.addRawPacketListener(new RawPacketListener() {
            public void rawPacketArrived(final RawPacket rawPacket) {
                listModel.add(rawPacket);
            }
        });

    }

    public void close() {
        this.capture.close();
    }
}

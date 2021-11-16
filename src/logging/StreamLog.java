package logging;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import logic.adapters.Adapter;

public class StreamLog<Message> implements Runnable {
    private TreeLog<? super Message> log;
    private InputStream io;
    private String name;
    private LogParser parser;
    private Adapter<String, Message> messageParser;

    public StreamLog(TreeLog<? super Message> sink, InputStream io, String name, Adapter<String, Message> messageParser) {
        this.log = sink;
        this.io = io;
        this.parser = new LogParser();
        this.messageParser = messageParser;
        this.name = name;
    }

    public void logString(String msg) {
        log.log(new LogMessage(messageParser.adapt(msg)));
    }

    @Override
    public void run() {
        log.enter(new LogMessage(messageParser.adapt("Connection received from " + name)));
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.io));
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    logString("Connection was abruptly closed by peer.");
                    break;
                }
                try {
                    if (!parser.readLine(log, line)) {
                        this.io.close();
                        logString("Connection closed on client request.");
                        break;
                    }
                } catch (Exception ex) {
                    logString("Parser exception");
                    logString(ex.toString());
                    break;
                }
            }
        }
        catch(IOException e) {
            logString("StreamLog IOException: " + e.toString());
        }
        log.leave();
    }
}

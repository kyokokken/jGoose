package Goose;

import javafx.application.Platform;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by justin on 9/2/2016.
 */
public class Logger {
    public static Logger INSTANCE = new Logger();
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public synchronized void println(String message){
        if(message != null) {
            Platform.runLater(() -> Program.INSTANCE.textArea.appendText(message + "\n"));
            System.out.println(message);
        }
    }

    public synchronized void connect(String message){
        if(message != null){
            Calendar cal = Calendar.getInstance();
            Platform.runLater(() -> Program.INSTANCE.textArea.appendText("\t" + dateFormat.format(cal.getTime()) + " : " + message + "\n"));
            System.out.println(message);
        }
    }

    public synchronized void print(String message){
        if(message != null) {
            Calendar cal = Calendar.getInstance();
            Platform.runLater(() -> Program.INSTANCE.textArea.appendText("\t" + dateFormat.format(cal.getTime()) + " : " + message));
            System.out.print(message);
        }
    }

    public synchronized void println(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        Calendar cal = Calendar.getInstance();
        Platform.runLater(() -> Program.INSTANCE.textArea.appendText("\t" + dateFormat.format(cal.getTime()) + " : " + sw.toString() + "\n"));
        e.printStackTrace();
    }
}

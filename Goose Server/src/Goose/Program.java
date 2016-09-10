package Goose;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Program extends Application{
    TextField input;
    GameServer gm;
    Player p = new Player();

    public static Program INSTANCE;

    /**
     * Just starts our GameServer
     */
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public TextArea textArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        INSTANCE = this;
        p.setState(Player.States.Ready);
        p.setAccess(Player.AccessStatus.GameMaster);
        input = new TextField();
        input.setPrefWidth(500);
        input.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                textArea.appendText("Command: " + input.getText() + "\n");
                handleEvent(input);
                input.setText("");
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(input, 0, 0, 2, 1);
        gridPane.add(textArea, 0, 2, 2, 1);

        Scene scene = new Scene(gridPane, 530, 250);
        primaryStage.setMaxWidth(540);
        primaryStage.setMaxHeight(280);
        primaryStage.setMinWidth(540);
        primaryStage.setMinHeight(280);
        primaryStage.setTitle("Aspereta Server");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(we -> {
            textArea.appendText("Shutting down server...");
            if(gm.gameworld.getRunning()) {
                gm.gameworld.setRunning(false);
            }
            exit();
        });
        run();
    }

    public void exit(){
        try {
            if(!GameWorld.isSaved){
                Thread.sleep(1000);
                exit();
            }
        } catch (InterruptedException e) {
            Logger.INSTANCE.println(e);
        }
        System.exit(0);
    }

    public void run(){
        try {
            GameServer gameServer = new GameServer();
            this.gm = gameServer;
            gameServer.start();
        }catch (Exception e){
            Logger.INSTANCE.println(e);
        }
    }

    public void handleEvent(TextField textField){
        try {
            String eventKey = textField.getText().trim();
            Event e = gm.gameworld.getEventHandler().stringToEvent.get(eventKey);
            if(e != null) {
                e.setPlayer(p);
                e.ready(gm.gameworld);
            }
        } catch (Exception e) {
            Logger.INSTANCE.println(e);
        }
    }

    public TextField getInput() {
        return input;
    }

    public void setInput(TextField input) {
        this.input = input;
    }
}

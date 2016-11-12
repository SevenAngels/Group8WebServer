import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * <p> Graphical user interface for the Group 8 Web Server Demo application. To use, enter a desired
 * port number and a server name. Afer that, the program will take a few seconds to connect,
 * be patient.Then, in a web browser, go to <code>http://localhost:</code>
 * followed by the port number. This will display the info that the server has received.
 * Refreshing the page will show that the server is now idle. Entering some text into the "send
 * data" field and pressing the button will send the data to the server. Refreshing the page will
 * then display the sent data on the web page. </p>
 *
 * <p>
 * ITCS 3166
 * Intro to Computer Networks
 * Dr. Angelina Tzacheva
 * 11/5/2016 </p>
 *
 * @author Adam Ritchie
 */
public class GUI extends Application {

    //Displays log messages to the window
    private static Label message;

    /**
     * Starts the application, by initializing the user interface.
     * @param primaryStage the stage that contains the GUI window
     * @throws Exception ignores exceptions
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        /* Initializes all GUI elements */

        primaryStage.setTitle("Group 8 Web Server");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Group 8 Web Server Demo");
        sceneTitle.setFont(Font.font("Helvetica", 20.0));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label portNum = new Label("Enter the port number to connect to:");
        grid.add(portNum, 0, 1);

        TextField portTextField = new TextField();
        grid.add(portTextField, 1, 1);

        Label name = new Label("Enter a name for your server:");
        grid.add(name, 0, 2);

        TextField nameTextField = new TextField();
        grid.add(nameTextField, 1, 2);

        Button connect = new Button("Connect!");
        GridPane.setConstraints(connect, 2, 2);
        grid.getChildren().add(connect);

        message = new Label();
        message.setTextFill(Color.CRIMSON);
        GridPane.setConstraints(message, 0, 5);
        GridPane.setColumnSpan(message, 2);
        grid.getChildren().add(message);

        Label data = new Label("Enter some data to send to the server:");
        grid.add(data, 0, 3);

        TextField dataTextField = new TextField();
        grid.add(dataTextField, 1, 3);

        Button sendData = new Button("Send Data");
        GridPane.setConstraints(sendData, 2, 3);
        grid.getChildren().add(sendData);

        Label stop = new Label("Press this button to stop the server:");
        grid.add(stop, 0, 4);

        Button stopServer = new Button("Stop Server");
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(stopServer);
        grid.add(hBox, 1, 4);

        /* Handles button actions */

        //Handles action for the "Send data" button
        sendData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(dataTextField.getText().isEmpty()) {
                    log(true, "Please enter some text before attempting to send it.");
                }
                if(ServerDriver.server != null) {
                    ServerDriver.sendData(dataTextField.getText());
                    log(false, "Data Sent!");
                } else {
                    log(true, "You cannot send data if you have not created a server yet!");
                }
            }
        });

        //Handles action for the "stop server" button
        stopServer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ServerDriver.stopServer();
                log(false, "Server Stopped.");
            }
        });

        //Handles action for the "Connect!" button
        connect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!portTextField.getText().isEmpty() && !nameTextField.getText().isEmpty()) {
                    int port = 0;
                    try {
                        port = Integer.parseInt(portTextField.getText());
                    } catch (NumberFormatException e) {
                        log(true, "The port field must contain an integer.");
                    }
                    if(port < 0 || port > 65535) {
                        log(true, "The port number must be 0-65535 inclusive.");
                    } else {
                        new ServerDriver(port, nameTextField.getText());
                        log(false, "Server created successfully.");
                    }
                } else {
                    log(true, "Please enter a port number and server name.");
                }
            }
        });

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    /**
     * Launches the application.
     * @param args not used
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Sends the specified String to the <code>message</code> label, to be displayed on the GUI.
     * If <code>error</code> is <code>true</code>, displays the message in red, otherwise
     * displays it in green.
     * @param error <code>true</code> if the message is an error message
     * @param str the message to be displayed
     */
    private static void log(boolean error, String str) {
        if(error) {
            message.setTextFill(Color.CRIMSON);
        } else {
            message.setTextFill(Color.FORESTGREEN);
        }
        message.setText(str);
    }
}

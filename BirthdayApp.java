package birthdayapp;

/**
 *
 * @author river-l3th3
 * compile together with the odbc connection for mysql
 * this can be done either in netbeans or by setting $CLASSPATH
 */

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javafx.application.*;
import static javafx.application.Application.launch;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.image.Image;


public class BirthdayApp extends Application {

    /**
     * @param args the command line arguments
     */  
    
    TextField nameTf;
    TextField targNumber;
    Label phCarrier;
    Label response;
    String CarrierVal = "";
    Button okB;
    Button canB;
    DatePicker dp;
    LocalDate bdayDate;
    
    //now initialize the sql variables
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/your_database";
    
    static final String USER = "username";
    static final String PASS = "password";
    
    public static void main(String[] args) {
        launch(args);  //launch JavaFX
    }
    
    public static String[] sanitize(String outBdayName,LocalDate outBday,
            String outNumber,String outCarrier) {
        /*
        this function does some housekeeping with potential vulnerabilities 
        introduced by user input
        */
        if (outBdayName.indexOf("--")!= 0) {
            outBdayName = outBdayName.replace("--","");
        }
        if (outNumber.indexOf("-")!=0) {
            outNumber = outNumber.replace("-","");
        }
        java.sql.Date inDate = java.sql.Date.valueOf(outBday);
        String processDate = inDate.toString();
        String[] arr = {outBdayName,processDate,outNumber,outCarrier};
        //start here
        return arr;
    }
    
    public int sqlify(String[] bdArray) {
        Connection conn = null;
	Statement stmt = null;
        String sql = ("call add_bday('" + bdArray[0] + "','" + bdArray[1] 
                + "','" + bdArray[2] + "','" + bdArray[3] + "')");
        
        try {
        conn=DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = conn.createStatement();
        
        ResultSet rs = stmt.executeQuery(sql);
        
        } catch (SQLException se) {
            se.printStackTrace();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        
        return 1;
    }
            
    @Override
    public void start(Stage myStage) {
        myStage.setTitle("Birthday Reminder");
        FlowPane rootNode = new FlowPane(10,10);
        rootNode.setAlignment(Pos.CENTER);
        rootNode.setPrefWrapLength(170);
              
        Scene myScene = new Scene(rootNode,400,300);
        
	/*
	in my app i put a picture of a pretty chocolate birthday cake.
	you can do that if you like, and redirect the Image path to the location you save your cake at.
	*/

        BackgroundImage myBI = new BackgroundImage(new Image("file:///your/file/path",400,300,false,true),
        BackgroundRepeat.REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
        rootNode.setBackground(new Background(myBI));
        
        myStage.setScene(myScene);
        
        nameTf = new TextField();
        nameTf.setPromptText("Whose name goes here?");
        
        //bdayDate = new TextField();
        dp = new DatePicker();
        dp.setPromptText("Enter the birthdate: ");
        dp.setOnAction(new EventHandler() {
            public void handle(Event t) {
                bdayDate = dp.getValue();
            }
        });
        
        targNumber = new TextField();
        targNumber.setPromptText("Your phone number");
        
        
        response = new Label("");
        
        okB = new Button("Ok!");
        canB = new Button("Cancel");
        
        ObservableList<String> computerTypes = 
                FXCollections.observableArrayList("AT&T","Verizon","T-Mobile");
        ListView<String> lvCarrier = new ListView<String>(computerTypes);
        
        lvCarrier.setPrefSize(160,70);
        
        MultipleSelectionModel<String> lvSelModel = 
                lvCarrier.getSelectionModel();
        
        lvSelModel.selectedItemProperty().addListener(
                                        new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> changed,
                    String oldVal, String newVal) {
                CarrierVal = newVal;
            }
            });
        
        Separator separator = new Separator();
        separator.setPrefWidth(180);
        
        canB.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent ae) {
                    System.exit(0);
                }
        });
        
        okB.setOnAction(new EventHandler<ActionEvent> () {
            public void handle(ActionEvent ae) {
                String outBdayName = nameTf.getText();
                LocalDate outBday = dp.getValue();
                String outNumber = targNumber.getText();
                String outCarrier = CarrierVal;
                String[] dbArr = sanitize(outBdayName,outBday,outNumber,outCarrier);
                int ok = sqlify(dbArr);
                if (ok!=1) { //if it went south, log the event
                    try {
                        FileWriter fout = new FileWriter("/etc/bdaylogger");
                        java.util.Date date = new java.util.Date();
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        String dt = df.format(date);
                        fout.write("Output failed at : " + dt + "\n");
                        fout.write("Arguments were: " + dbArr[0] + ", " + dbArr[1] 
                                + ", " + dbArr[2] + ", " + dbArr[3] + ".\n");
                    }
                    catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
                else {
                    System.exit(0);
                }
            }
        });
        
        rootNode.getChildren().addAll(nameTf,dp,targNumber
                ,lvCarrier,response,okB,canB);
        
        myStage.show();
    }  
}

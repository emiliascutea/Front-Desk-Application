package view.administrator;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Action;

import java.time.LocalDate;

public class AdministratorReportsView {
    private GridPane mainPage;
    private Scene scene;
    private Stage primaryStage;

    private Button backBtn;
    private Button generateBtn;
    private TextField employeeId;
    private TextField fromDate;
    private TextField toDate;
    private TableView<Action> table;
    private TableColumn<Action, String> table_name;
    private TableColumn<Action, Long> table_employeeId;

    public AdministratorReportsView() {
        mainPage = new GridPane();
        scene = new Scene(mainPage, 400, 600);
        backBtn = new Button("Back");
        generateBtn = new Button("Generate");
        employeeId = new TextField();
        fromDate = new TextField();
        toDate = new TextField();
        table = new TableView<>();
        table_employeeId= new TableColumn<>();
        table_name= new TableColumn<>();
        setUpView();
    }

    private void setUpView(){
        mainPage.prefHeight(400.0);
        mainPage.prefWidth(600.0);
        backBtn.setLayoutX(58);
        backBtn.setLayoutY(52);
        generateBtn.setLayoutX(475);
        generateBtn.setLayoutY(63);
        employeeId.setLayoutX(29);
        employeeId.setLayoutY(27);
        employeeId.setPromptText("Employee id");
        fromDate.setLayoutX(190);
        fromDate.setLayoutY(27);
        fromDate.setPromptText(String.valueOf(LocalDate.now()));
        toDate.setLayoutX(371);
        toDate.setLayoutY(27);
        toDate.setPromptText("to date");
        table_employeeId.setText("Employee id");
        table_name.setText("Action name");
        table.setPrefHeight(300);
        table.setPrefWidth(500);
        table.getColumns().addAll(table_employeeId, table_name);
        mainPage.add(employeeId,1,1);
        mainPage.add(fromDate,2,1);
        mainPage.add(toDate,3,1);
        mainPage.add(backBtn,1,2);
        mainPage.add(generateBtn,2,2);
        mainPage.add(table, 1, 3);

    }

    public void setScene(Scene scene){
        this.primaryStage.setScene(scene);
    }

    public Scene getScene(){
        return scene;
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public Button getBackBtn() {
        return backBtn;
    }

    public Button getGenerateBtn() {
        return generateBtn;
    }

    public String getEmployeeId(){
        return employeeId.getText();
    }

    public String getFromDate(){
        return fromDate.getText();
    }

    public String getToDate(){
        return toDate.getText();
    }

    public TableView<Action> getTable() {
        return table;
    }

    public TableColumn<Action, String> getTable_name() {
        return table_name;
    }

    public TableColumn<Action, Long> getTable_employeeId() {
        return table_employeeId;
    }
}

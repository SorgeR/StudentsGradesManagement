package sample.gui.controller;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.domain.DTOS.StudentGradeDTO;
import sample.domain.Grade;
import sample.domain.Homework;
import sample.domain.Student;
import sample.service.ServiceExemption;
import sample.service.ServiceGrade;
import sample.service.ServiceHomework;
import sample.service.ServiceStudent;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GradeController extends AbstractController {


    //pagination data
    private Integer itemsPerPage = 10;

   //models
    private ObservableList<StudentGradeDTO> model = FXCollections.observableArrayList();
    private ObservableList<StudentGradeDTO> paginationModel = FXCollections.observableArrayList();

    //combo box data
    private ObservableList<String> week = FXCollections.observableArrayList(
            "Week", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"
    );
    private ObservableList<String> groups = FXCollections.observableArrayList("Group");

    //pie chart data
    private ObservableList<PieChart.Data> passedNotPassedPieChartList = FXCollections.observableArrayList();
    private ObservableList<PieChart.Data> homeworkAveragePieChartList=FXCollections.observableArrayList();

    //services
    private ServiceStudent serviceStudent = new ServiceStudent();
    private ServiceHomework serviceHomework = new ServiceHomework();
    private ServiceExemption serviceExemption = new ServiceExemption(serviceStudent);
    private ServiceGrade serviceGrade = new ServiceGrade(serviceStudent, serviceHomework, serviceExemption);

    //table columns collection
    private ArrayList<TableColumn<StudentGradeDTO, String>> columns = new ArrayList<>();

    //pie charts
    @FXML
    private PieChart passedNotPassedPieChart;
    @FXML
    private PieChart homeworkAverageGradePieChart;

    //pagination
    @FXML
    private Pagination pagination;

    //input fields
    @FXML
    private TextField studentNameTextField;

    //table stuff
    @FXML
    private TableView<StudentGradeDTO> tableView;

    //add grade popup stuff
    @FXML
    private ComboBox<String> weekComboBox;
    @FXML
    private TextArea feedbackTextArea;

    //filter grades
    @FXML
    private ComboBox<String> homeworkComboBox;
    @FXML
    private ComboBox<String> groupComboBox;

    //reports export to pdf buttons
    @FXML
    private Button exportPassedNotPassedToPdf;
    @FXML
    private Button exportTop30ToPdf;
    @FXML
    private Button exportTopTeachersToPdf;
    @FXML
    private Button exportTopHardestHomeworkToPdf;

    //navigation buttons
    @FXML
    private Button studentGoButton;
    @FXML
    private Button homeworkGoButton;
    @FXML
    private Button gradeGoButton;
    @FXML
    private Button exemptionGoButton;

    @FXML
    private void initialize() {
        initializeGroupComboBox();
        setWeekComboBoxListener();
        initializeModel();
        initializeTableView();
        initColumnsListeners();
        initializeStudentTextListener();
        initializeGradeButtonColor();
        initializePieChart();

    }

    private List<StudentGradeDTO> getFullFilteredStudents(){

        if(groupComboBox.getValue().equals("Group"))
        {
            return this.serviceGrade.filterStudentGradesDTOListByStudentName(studentNameTextField.getText());
        }
        else{
            return this.serviceGrade.filterStudentsFromGivenGroupAndName(studentNameTextField.getText(),Integer.valueOf(groupComboBox.getValue()));
        }
    }

    private void initializeModel() {
        List<StudentGradeDTO> students = getFullFilteredStudents();
        model.setAll(new ArrayList<>(students));
    }

    private void initializeStudentTextListener() {
        studentNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            initializeModel();
            initializePagination();
        });
    }

    private void initializeTwoColumnsTableWithStudentAndHomework()
    {
        Integer i = Integer.valueOf(homeworkComboBox.getValue());
        tableView.getColumns().clear();
        pagination.setPrefWidth(230);

        TableColumn<StudentGradeDTO, String> nameColumn = new TableColumn<>("Name");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        nameColumn.getStyleClass().add("grade-table-column");
        nameColumn.setPrefWidth(130);

        TableColumn<StudentGradeDTO, String> column = new TableColumn<>("H" + i.toString());

        column.getStyleClass().add("grade-table-column");
        column.setPrefWidth(100);
        column.setCellValueFactory(new PropertyValueFactory<>("GradeH" + i.toString()));

        tableView.getColumns().addAll(nameColumn, column);

    }

    private void initializeTableForFilterByHomeworkAndStudentName() {


        if (homeworkComboBox.getValue().equals("Week")) {

            initializeTableView();

        } else {
            initializeTwoColumnsTableWithStudentAndHomework();
        }

    }

    private void initializeTableView() {
        pagination.setPrefWidth(735);
        tableView.getColumns().clear();

        TableColumn<StudentGradeDTO, String> nameColumn = new TableColumn<>("Name");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        nameColumn.getStyleClass().add("grade-table-column");
        nameColumn.setPrefWidth(130);
        
        if (columns.size() == 0) {
            
            for (int i = 1; i <= 14; ++i) {

                TableColumn<StudentGradeDTO, String> column = new TableColumn<>("H" + Integer.toString(i));

                column.getStyleClass().add("grade-table-column");
                column.setPrefWidth(43);
                column.setCellValueFactory(new PropertyValueFactory<>("GradeH" + Integer.toString(i)));
                columns.add(column);

            }


            Callback<TableColumn<StudentGradeDTO, String>, TableCell<StudentGradeDTO, String>> cellFactory =
                    p -> new EditingCell();
            for (TableColumn<StudentGradeDTO, String> column : columns) {
                column.setCellFactory(cellFactory);
            }

        }
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().addAll(columns);
        tableView.setEditable(true);


        initializePagination();


    }

    private void initColumnsListeners() {

        for (TableColumn<StudentGradeDTO, String> column : columns) {
            column.setOnEditCommit(
                    t -> {
                        try{
                            onTextCellChange(t.getRowValue().getStudentId(), t.getTablePosition().getColumn(), t.getNewValue());

                        }
                        catch (NumberFormatException ex)
                        {
                            showErrorMessage("Please enter a valid grade!");
                            initializePagination();
                        }
                    }
            );
        }

    }

    private void initializePagination() {
        int count = (model.size() / itemsPerPage) + 1;
        pagination.setPageCount(count);
        pagination.setPageFactory(this::createPage);


    }

    private void initializeGroupComboBox() {
        this.serviceStudent.getGroups().forEach(x -> groups.add(String.valueOf(x)));
        groupComboBox.setItems(groups);
        groupComboBox.setValue("Group");
        groupComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            initializeModel();
            initializePagination();
        });
    }

    private void setWeekComboBoxListener() {
        homeworkComboBox.setItems(week);
        homeworkComboBox.setValue("Week");

        homeworkComboBox.valueProperty().addListener((observable, oldValue, newValue) -> initializeTableForFilterByHomeworkAndStudentName());
    }

    private void setupPaginationModelWithPage(ObservableList<StudentGradeDTO> list,int from,int to)
    {
        paginationModel.clear();

        for (int i = from; i < to; ++i) {
            this.paginationModel.add(list.get(i));
        }
    }

    private Node createPage(int pageIndex) {
        int from , to ;

        from = pageIndex * itemsPerPage;
        to = (from + itemsPerPage) < model.size() ? (from + itemsPerPage) : model.size();

        setupPaginationModelWithPage(model,from,to);

        tableView.setItems(paginationModel);


        return tableView;
    }

    private void onTextCellChange(Integer studentId, Integer idHomework, String grade) {

        Grade foundGrade = serviceGrade.findGrade(studentId, idHomework);
        Homework homework=serviceHomework.findHomework(idHomework);
        if(homework==null)
        {
            this.showErrorMessage("The homework does not exist!");
            initializePagination();
            return;
        }

        if (foundGrade != null) {
            this.showErrorMessage("You can not change the grade of a student!");
            tableView.getColumns().clear();
            initializeTableView();
            return;
        }
        displayPopup(studentId, idHomework, Double.parseDouble(grade));


    }

    private void saveGrade(Integer studentId, Integer idHomework, Double grade,Stage window)
    {
        if (weekComboBox.getValue().equals("Week")) {
            this.showErrorMessage("Please choose a valid week!");
            return;
        }
        if (feedbackTextArea.getText().equals("")) {
            this.showErrorMessage("Please enter the feedback!");
            return;
        }
        Integer week = Integer.valueOf(weekComboBox.getValue());
        String feedback = feedbackTextArea.getText();

        try {
            Double penaltyPoints = this.serviceGrade.penaltyPointsOfStudentInAWeekOnAHomework(studentId, idHomework, week);
            Grade g = this.serviceGrade.saveGrade(new Grade(studentId, idHomework, grade, week, feedback));
            if (penaltyPoints > 5) {
                showMessage(Alert.AlertType.INFORMATION, "Information!", "The student can not give the homework anymore grade is 1!");
            } else {
                showMessage(Alert.AlertType.INFORMATION, "Information!", "The grade was decreased with " + penaltyPoints.toString() + " points!");

            }
            if (g == null) {

                showMessage(Alert.AlertType.INFORMATION, "Success!", "The grade was saved!");
                window.close();
                initializeModel();
                initializeTableView();
                initializePieChart();
            } else {
                this.showErrorMessage("Ops! We could not save the grade!");
            }
        } catch (Exception ex) {
            this.showErrorMessage(ex.getMessage());
        }

    }

    private void displayPopup(Integer studentId, Integer idHomework, Double grade) {
        if(grade>10 || grade<1){
            this.showErrorMessage("Please enter a valid grade!");
            this.initializeModel();
            this.initializePagination();
            return;
        }
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Grade");
        window.setWidth(300);
        window.setHeight(300);

        this.feedbackTextArea = new TextArea();
        feedbackTextArea.setPromptText("Feedback...");
        feedbackTextArea.setPrefWidth(200);
        feedbackTextArea.setPrefHeight(100);
        feedbackTextArea.setMaxWidth(200);
        feedbackTextArea.setMaxHeight(100);

        this.weekComboBox = new ComboBox<>();
        weekComboBox.setMinWidth(200);
        weekComboBox.setMaxWidth(200);
        weekComboBox.setValue("Week");
        weekComboBox.setItems(week);

        Button add = new Button("Add grade");
        add.setStyle(" -fx-background-color: #F3AB71;\n" +
                "    -fx-font-family: \"Calibri Light\";\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-font-size: 15;");
        Button close = new Button("Close");
        close.setStyle(" -fx-background-color: #F3AB71;\n" +
                "    -fx-font-family: \"Calibri Light\";\n" +
                "    -fx-text-fill: white;\n" +
                "    -fx-font-size: 15;");

        add.setMinWidth(100);
        close.setMinWidth(100);

        close.setOnAction(x -> {
            window.close();
            initializeModel();
            initializeTableView();
        });

        add.setOnAction(x -> {
                this.saveGrade(studentId,idHomework,grade,window);

        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(add, close);
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox();
        layout.getChildren().addAll(feedbackTextArea, weekComboBox, hBox);
        layout.setSpacing(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("  -fx-background-color: #404C5A;");

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }



    public void exportPassedNotPassedButtonHandler(){
        try{
        this.serviceGrade.exportPassedNotPassedToPdf();
        showMessage(Alert.AlertType.INFORMATION, "Success!", "Successfully exported to PDF!");
        }
        catch (FileNotFoundException ex)
        {
            showErrorMessage("Please close the PDF first!");
        }

    }

    public void exportTop30ButtonHandler() {
        try {
            this.serviceGrade.exportTop30ToPDF();
            showMessage(Alert.AlertType.INFORMATION, "Success!", "Successfully exported to PDF!");
        }
        catch (FileNotFoundException ex)
        {
            showErrorMessage("Please close the PDF first!");
        }

    }

    public void exportTopTeachersButtonHandler() {
        try {
            this.serviceGrade.exportTopTeachersToPDF();
            showMessage(Alert.AlertType.INFORMATION, "Success!", "Successfully exported to PDF!");
        }
        catch (FileNotFoundException ex)
    {
        showErrorMessage("Please close the PDF first!");
    }
    }

    public void exportTopHardestHomeworkToPdf() {
        try{
        this.serviceGrade.exportTopHardestHomeworksToPDF();
        showMessage(Alert.AlertType.INFORMATION, "Success!", "Successfully exported to PDF!");
        }
        catch (FileNotFoundException ex)
        {
            showErrorMessage("Please close the PDF first!");
        }
    }

    private void initializeGradeButtonColor() {
        this.gradeGoButton.setStyle("-fx-background-color: #4a5a6d;-fx-text-fill: #F3AB71");
    }

    @Override
    public  void goToStudents() {
        super.goToStudents();
        Stage stage= (Stage) gradeGoButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void goToExemptions() {
        super.goToExemptions();
        Stage stage= (Stage) gradeGoButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void goToHomework() {
        super.goToHomework();
        Stage stage= (Stage) gradeGoButton.getScene().getWindow();
        stage.close();
    }

    private void getPieChartData(){
        passedNotPassedPieChartList.clear();
        this.serviceGrade.getPassedNotPassedNumberOfStudents()
                .forEach(x->passedNotPassedPieChartList.add(new PieChart.Data(x.getPassed() ? "Passed":"Not Passed",x.getNumberOfPassed())));

        homeworkAveragePieChartList.clear();
        this.serviceGrade.averageGradeOnHomeworks()
                .forEach(x->homeworkAveragePieChartList.add(new PieChart.Data(x.getNumber().toString(),x.getGrade())));
    }

    private void initializePieChart(){
        getPieChartData();
        passedNotPassedPieChart.setData(passedNotPassedPieChartList);
        passedNotPassedPieChart.setTitle("Passed/Not Passed");

        homeworkAverageGradePieChart.setData(homeworkAveragePieChartList);
        homeworkAverageGradePieChart.setTitle("Average per Homework");
    }

   
}

package sample.gui.controller;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.domain.DTOS.StudentExemptionDTO;
import sample.domain.Exemption;
import sample.domain.Student;
import sample.service.ServiceExemption;
import sample.service.ServiceStudent;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;


public class ExemptionController extends AbstractController {

    private Integer goListen=0;
    private Integer from = 0, to = 0, itemsPerPage = 8;
    private ObservableList<String> weekData = FXCollections.observableArrayList(
            "Week", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"
    );

    private ObservableList<PieChart.Data> weekNumberOfExemptionsPieChartList = FXCollections.observableArrayList();

    //models
    private ObservableList<StudentExemptionDTO> exemptionModel = FXCollections.observableArrayList();
    private ObservableList<StudentExemptionDTO> paginationExemptionModel = FXCollections.observableArrayList();
    private ObservableList<Student> studentsModel = FXCollections.observableArrayList();
    private ObservableList<Student> paginationStudentsModel = FXCollections.observableArrayList();

    //services
    private ServiceStudent serviceStudent = new ServiceStudent();
    private ServiceExemption serviceExemption = new ServiceExemption(serviceStudent);

    //the current selected student
    private Student selectedStudent;

    //listener
    private ChangeListener<String> nameTextBoxListener = (observable, oldValue, newValue) -> {
        if(this.goListen==0) {
            initializeStudentModel();
            initializePaginationStudent();
        }
        goListen=0;
    };

    @FXML
    private Pagination paginationExemption;
    @FXML
    private Pagination paginationStudents;
    @FXML
    private TableView<Student> tableStudents;
    @FXML
    private TableView<StudentExemptionDTO> tableExemption;
    @FXML
    private TextField studentsNameTextField;

    @FXML
    private TableColumn<StudentExemptionDTO, String> columnStudentNameExemption;
    @FXML
    private TableColumn<StudentExemptionDTO, Integer> columnWeekExemption;
    @FXML
    private TableColumn<StudentExemptionDTO, String> columnReasonExemption;


    @FXML
    private TableColumn<Student, String> columnStudentName;
    @FXML
    private TableColumn<Student, String> columnGroupName;
    @FXML
    private TableColumn<Student, String> columnSubgroupName;
    @FXML
    private TableColumn<Student, String> columnTeacherName;

    @FXML
    private ComboBox<String> weeksComboBox;
    @FXML
    private TextArea reasonTextArea;

    @FXML
    private Button addExemptionButton;

    @FXML
    private PieChart weekExemptionsNumbersPieChart;


    @FXML
    private Button studentGoButton;
    @FXML
    private Button homeworkGoButton;
    @FXML
    private Button gradeGoButton;
    @FXML
    private Button exemptionGoButton;


    @FXML
    void initialize() {
        initializeTextFieldListener();
        initializeStudentModel();
        initializeExemptionModel();
        initializeTableStudent();
        initializeTableExemption();
        initializeComboBoxData();
        initializeExemptionButtonColor();
        initializeWeekNumberOfExemptionsPieChart();

        preventColumnsMovement();


    }

    private void initializeStudentModel() {
        studentsModel.setAll(new ArrayList<>(serviceStudent.filterStudentsByName(studentsNameTextField.getText())));
    }

    private void initializeExemptionModel() {
        exemptionModel.setAll(new ArrayList<>(serviceExemption.studentExemptionDTOList()));
    }

    private void initializeComboBoxData() {
        weeksComboBox.setItems(weekData);
        weeksComboBox.setValue("Week");
    }

    private void saveStudent() {
        Integer studentId, week;
        String reason;
        if (this.selectedStudent == null) {
            this.showErrorMessage("Please select a student from list!");
            return;
        }

        if (weeksComboBox.getValue().equals("Week")) {
            this.showErrorMessage("Please select a week!");
            return;
        }

        if (reasonTextArea.getText().isEmpty()) {
            this.showErrorMessage("The reason can not be empty!");
            return;
        }

        try {
            studentId = this.selectedStudent.getID();
            week = Integer.valueOf(weeksComboBox.getValue());
            reason = reasonTextArea.getText();
            Exemption exemption = this.serviceExemption.saveExemption(new Exemption(studentId, week, reason));
            if (exemption == null) {
                freeInputs();
                showMessage(Alert.AlertType.INFORMATION, "Success!", "The exemption was saved!");

            } else {
                freeInputs();
                this.showErrorMessage("Ops! We could not save the exemption!");
            }
        } catch (Exception ex) {
            this.showErrorMessage(ex.getMessage());
        }
    }

    public void saveExemptionButtonHandler() {
        this.saveStudent();
        this.initializeExemptionModel();
        this.initializePaginationExemption();
        initializeWeekNumberOfExemptionsPieChart();
    }

    private void initializeTableExemption() {
        columnStudentNameExemption.setCellValueFactory(new PropertyValueFactory<>("StudentName"));
        columnWeekExemption.setCellValueFactory(new PropertyValueFactory<>("Week"));
        columnReasonExemption.setCellValueFactory(new PropertyValueFactory<>("Reason"));
        initializePaginationExemption();
    }


    private void initializeTableStudent() {
        columnStudentName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnGroupName.setCellValueFactory(new PropertyValueFactory<>("Group"));
        columnSubgroupName.setCellValueFactory(new PropertyValueFactory<>("Subgroup"));
        columnTeacherName.setCellValueFactory(new PropertyValueFactory<>("TeachersName"));

        initializePaginationStudent();
    }

    private void initializeTextFieldListener() {
        studentsNameTextField.textProperty().addListener(this.nameTextBoxListener);
    }

    private void initializePaginationExemption() {
        int count = (exemptionModel.size() / itemsPerPage) + 1;
        paginationExemption.setPageCount(count);
        paginationExemption.setPageFactory(this::createPageExemption);
    }

    private Node createPageExemption(int pageIndex) {
        from = pageIndex * itemsPerPage;
        to = (from + itemsPerPage) < exemptionModel.size() ? (from + itemsPerPage) : exemptionModel.size();
        paginationExemptionModel.clear();

        for (int i = from; i < to; ++i) {
            this.paginationExemptionModel.add(this.exemptionModel.get(i));
        }
        tableExemption.setItems(paginationExemptionModel);

        return tableExemption;

    }


    private void initializePaginationStudent() {
        int count = (studentsModel.size() / itemsPerPage) + 1;
        paginationStudents.setPageCount(count);
        paginationStudents.setPageFactory(this::createPageStudent);


    }


    private Node createPageStudent(int pageIndex) {
        from = pageIndex * itemsPerPage;
        to = (from + itemsPerPage) < studentsModel.size() ? (from + itemsPerPage) : studentsModel.size();
        paginationStudentsModel.clear();

        for (int i = from; i < to; ++i) {
            this.paginationStudentsModel.add(this.studentsModel.get(i));
        }

        tableStudents.setItems(paginationStudentsModel);

        tableStudents.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if(newValue!=null) {
                        this.selectedStudent = newValue;
                        goListen = 1;
                        studentsNameTextField.setText(newValue.getName());
                    }
                });

        return tableStudents;

    }

    private void freeInputs(){
        reasonTextArea.setText("");
        studentsNameTextField.setText("");
        initializeTableStudent();
        initializePaginationExemption();
        weeksComboBox.setValue("Week");
    }

    private void initializeExemptionButtonColor() {
        this.exemptionGoButton.setStyle("-fx-background-color: #4a5a6d;-fx-text-fill: #F3AB71");
    }

    @Override
    public void goToGrades() {
        super.goToGrades();
        Stage stage = (Stage) gradeGoButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void goToStudents() {
        super.goToStudents();
        Stage stage = (Stage) gradeGoButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void goToHomework() {
        super.goToHomework();
        Stage stage = (Stage) gradeGoButton.getScene().getWindow();
        stage.close();
    }
    private void getGetWeekNumberOfExemptions() {
        weekNumberOfExemptionsPieChartList.clear();
        this.serviceExemption.getWeekNumberOfExemptionsDTOS()
                .forEach(x -> weekNumberOfExemptionsPieChartList.add(new PieChart.Data(x.getWeek().toString(), x.getNumberOfExemptions())));
    }

    private void initializeWeekNumberOfExemptionsPieChart() {
        getGetWeekNumberOfExemptions();
        weekExemptionsNumbersPieChart.setData(weekNumberOfExemptionsPieChartList);
        weekExemptionsNumbersPieChart.setTitle("Exemptions Per Week");
    }

    private void preventColumnsMovement()
    {
        tableStudents.getColumns().addListener(new ListChangeListener<TableColumn<Student, ?>>() {
            @Override
            public void onChanged(Change<? extends TableColumn<Student, ?>> c) {

                initializePaginationStudent();
            }
        });
    }



}

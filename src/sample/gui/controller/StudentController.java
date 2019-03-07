package sample.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.changeevent.ChangeEventType;
import sample.changeevent.StudentChangeEvent;
import sample.domain.Student;
import sample.observer.Observer;
import sample.service.ServiceStudent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StudentController extends AbstractController implements Observer<StudentChangeEvent>  {


    //pagination stuff
    private Integer itemsPerPage = 10;

    //mode determination (filter/manager)
    private Integer mode = 1;

    //subgroup combo box data
    private ObservableList<String> subgroupData = FXCollections.
            observableArrayList(Arrays.asList("Subgroup", "1", "2"));

    //model
    private ObservableList<Student> model = FXCollections.observableArrayList();
    private ObservableList<Student> paginationModel = FXCollections.observableArrayList();

    //charts data
    private ObservableList<PieChart.Data> teacherPieChartList = FXCollections.observableArrayList();
    private ObservableList<PieChart.Data> groupPieChartList = FXCollections.observableArrayList();

    //services
    private ServiceStudent serviceStudent = new ServiceStudent();

    //the last selected student
    private Student selectedStudent = null;

    //pie charts
    @FXML
    private PieChart pieChartTeacher;
    @FXML
    private PieChart pieChartGroup;

    //pagination
    @FXML
    private Pagination pagination;

    //table stuff
    @FXML
    private TableView<Student> tableView;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, Integer> groupColumn;
    @FXML
    private TableColumn<Student, Integer> subgroupColumn;
    @FXML
    private TableColumn<Student, String> emailColumn;
    @FXML
    private TableColumn<Student, String> teacherColumn;

    //radio buttons
    @FXML
    private RadioButton filterRadioButton;
    @FXML
    private RadioButton manageRadioButton;

    //manager/filter fields
    @FXML
    private ComboBox<String> subgroupComboBox;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField groupTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField teacherTextField;

    //manager buttons
    @FXML
    private Button saveButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;


    //menu buttons
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
        serviceStudent.addObserver(this);
        initializeModel();
        getPieChartData();
        initializePieChartData();
        initializeTableView();
        initializePagination();
        initializeRadioButtons();
        initializeComboBox();
        setRadioButtonsListeners();
        initializeStudentsButtonColor();
        setTextInputListeners();

    }


    private void initializeTableView() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        subgroupColumn.setCellValueFactory(new PropertyValueFactory<>("subgroup"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("TeachersName"));


    }

    private void initializeModel() {
        model.setAll(StreamSupport.stream(serviceStudent.findActiveStudents().spliterator(), false)
                .collect(Collectors.toList()));
    }

    private void initializeRadioButtons() {
        manageRadioButton.selectedProperty().setValue(true);
    }

    private void initializeComboBox() {
        subgroupComboBox.setItems(subgroupData);
        subgroupComboBox.setVisibleRowCount(3);
    }

    private void putSelectedStudentDataIntoFields(Student student) {
        if (student == null) {
            nameTextField.setText("");
            groupTextField.setText("");
            emailTextField.setText("");
            subgroupComboBox.setPromptText("Subgroup");
            teacherTextField.setText("");
        } else {
            nameTextField.setText(student.getName());
            groupTextField.setText(student.getGroup().toString());
            emailTextField.setText(student.getEmail());
            subgroupComboBox.setValue(student.getSubgroup().toString());
            teacherTextField.setText(student.getTeachersName());

        }
    }

    private void setRadioButtonsListeners() {
        filterRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                manageRadioButton.selectedProperty().setValue(false);

                mode = 0;
                saveButton.setDisable(true);
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                freeInputs();

            }
        });

        manageRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                filterRadioButton.selectedProperty().setValue(false);

                mode = 1;
                saveButton.setDisable(false);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                freeInputs();
                initializeModel();
                initializePagination();

            }
        });
    }

    private Student getDataFromTextInputs() {
        if (nameTextField.getText().isEmpty()) {
            this.showErrorMessage("The name text field can not be empty!");
            return null;
        }
        if (groupTextField.getText().isEmpty()) {
            this.showErrorMessage("The group text field can not be empty!");
            return null;
        }
        if (emailTextField.getText().isEmpty()) {
            this.showErrorMessage("The email text field can not be empty!");
            return null;
        }
        if (subgroupComboBox.getValue().equals("Subgroup")) {
            this.showErrorMessage("Please choose a valid subgroup!");
            return null;
        }
        if (teacherTextField.getText().isEmpty()) {
            this.showErrorMessage("The teachers name text field can not be empty!");
            return null;
        }
        try {
            String name = nameTextField.getText();
            Integer group = Integer.valueOf(groupTextField.getText());
            Integer subgroup = Integer.valueOf(subgroupComboBox.getValue());
            String email = emailTextField.getText();
            String teacher = teacherTextField.getText();
            return new Student(name, group, subgroup, email, teacher);
        } catch (NumberFormatException ex) {
            this.showErrorMessage("The group must be a number!");
            return null;
        }

    }

    private void getPieChartData() {
        teacherPieChartList.clear();
        this.serviceStudent.getNumberOfStudentsForEachTeacher()
                .forEach(x -> this.teacherPieChartList.add(new PieChart.Data(x.getTeachersName(), x.getNumberOfStudents())));

        this.groupPieChartList.clear();
        this.serviceStudent.getNumbersOfStudentsForEachGroup()
                .forEach(x -> this.groupPieChartList.add(new PieChart.Data(x.getGroup().toString(), x.getNumber())));
    }

    private void initializePieChartData() {
        this.pieChartTeacher.setData(this.teacherPieChartList);
        this.pieChartTeacher.setTitle("Students per teacher");

        this.pieChartGroup.setData(this.groupPieChartList);
        this.pieChartGroup.setTitle("Students per group");
    }

    private void initializePagination() {
        int count = (model.size() / itemsPerPage) + 1;
        pagination.setPageCount(count);
        pagination.setPageFactory(this::createPage);

    }

    private void initializeStudentsButtonColor() {
        this.studentGoButton.setStyle("-fx-background-color: #4a5a6d;-fx-text-fill: #F3AB71");

    }

    private void initializeModelWithList(List<Student> students) {
        model.setAll(new ArrayList<>(students));
        initializePagination();
    }

    private void setTableViewData(ObservableList<Student> list) {
        this.tableView.setItems(list);
    }

    private void populatePaginationDataWithList(ObservableList<Student> list,int from, int to) {
        paginationModel.clear();
        for (int i = from; i < to; ++i) {
            this.paginationModel.add(list.get(i));
        }
    }

    private void setTableViewListener() {
        tableView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (mode == 1) {
                        putSelectedStudentDataIntoFields(newValue);
                        this.selectedStudent = newValue;
                    }
                });
    }

    private Node createPage(int pageIndex) {
        int from,to;

        from = pageIndex * itemsPerPage;
        to = (from + itemsPerPage) < model.size() ? (from + itemsPerPage) : model.size();

        populatePaginationDataWithList(model,from,to);

        setTableViewData(paginationModel);

        setTableViewListener();

        return tableView;
    }

    private void setTextInputListeners() {

        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (mode == 0) {
                initializeModelWithList(getFilteredStudents());
            }
        });

        groupTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (mode == 0) {
                initializeModelWithList(getFilteredStudents());
            }

        });


        emailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (mode == 0) {
                initializeModelWithList(getFilteredStudents());
            }
        });

        teacherTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (mode == 0) {
                initializeModelWithList(getFilteredStudents());
            }
        });
    }

    private List<Student> getFilteredStudents() {

        String name, email, teacher;
        Integer group;

        if (nameTextField.getText().isEmpty()) {
            name = null;
        } else {
            name = nameTextField.getText();
        }

        if (emailTextField.getText().isEmpty()) {
            email = null;
        } else {
            email = emailTextField.getText();
        }

        if (teacherTextField.getText().isEmpty()) {
            teacher = null;
        } else {
            teacher = teacherTextField.getText();
        }

        if (groupTextField.getText().isEmpty()) {
            group = null;
        } else {
            group = Integer.valueOf(groupTextField.getText());
        }


        return this.serviceStudent.filterStudents(name, group, null, email, teacher);
    }

    private void freeInputs(){
        nameTextField.setText("");
        teacherTextField.setText("");
        groupTextField.setText("");
        subgroupComboBox.setValue("Subgroup");
        emailTextField.setText("");
    }

    private void saveStudent() {
        Student student = this.getDataFromTextInputs();
        if (student == null) {
            return;
        }

        try {
            Student newStud = this.serviceStudent.saveStudent(student);
            if (newStud == null) {
                freeInputs();
                showMessage(Alert.AlertType.INFORMATION, "Success!", "The student was saved!");
            } else {
                this.showErrorMessage("Ops! We could not save the student!");
            }
        } catch (Exception ex) {
            this.showErrorMessage(ex.getMessage());
        }


    }

    public void buttonSaveHandler() {
        this.saveStudent();
    }

    private void deleteStudent() {
        if (this.selectedStudent == null) {
            this.showErrorMessage("Please choose a student!");
            return;
        }
        try {
            Student s = this.serviceStudent.deleteStudent(this.selectedStudent.getID());
            if (s != null) {
                freeInputs();
                showMessage(Alert.AlertType.INFORMATION, "Success!", "The student was deleted!");
            } else {
                this.showErrorMessage("Ops! We could not delete the student!");
            }
        } catch (Exception ex) {
            this.showErrorMessage(ex.getMessage());
        }
    }

    public void buttonDeleteHandler() {
        this.deleteStudent();
    }

    private void updateStudent() {
        Student student = this.getDataFromTextInputs();
        if (student == null) {
            return;
        }

        try {
            student.setID(this.selectedStudent.getID());
            student.setDeleted(false);
            Student newStud = this.serviceStudent.updateStudent(student);
            if (newStud == null) {
                freeInputs();
                showMessage(Alert.AlertType.INFORMATION, "Success!", "The student was updated!");

            } else {
                this.showErrorMessage("Ops! We could not update the student!");
            }
        } catch (Exception ex) {
            this.showErrorMessage(ex.getMessage());
        }

    }

    public void buttonUpdateHandler() {
        this.updateStudent();
    }

    @Override
    public void update(StudentChangeEvent studentChangeEvent) {
        if (studentChangeEvent.getType().equals(ChangeEventType.ADD)) {
            Student s = studentChangeEvent.getData();
            s.setDeleted(false);
            model.add(s);

        }
        if (studentChangeEvent.getType().equals(ChangeEventType.DELETE)) {
            model.remove(studentChangeEvent.getData());
        }
        if (studentChangeEvent.getType().equals(ChangeEventType.UPDATE)) {
            initializeModel();

        }
        initializePagination();

        getPieChartData();
        initializePieChartData();
    }

    @Override
    public void goToGrades() {
        super.goToGrades();
        Stage stage = (Stage) gradeGoButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void goToExemptions() {
        super.goToExemptions();
        Stage stage = (Stage) gradeGoButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void goToHomework() {
        super.goToHomework();
        Stage stage = (Stage) gradeGoButton.getScene().getWindow();
        stage.close();
    }
}

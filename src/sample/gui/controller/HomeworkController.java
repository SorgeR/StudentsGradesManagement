package sample.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.changeevent.ChangeEventType;
import sample.changeevent.HomeworkChangeEvent;
import sample.domain.DTOS.HomeworkDurationDTO;
import sample.domain.Homework;
import sample.domain.WeekHelper;
import sample.observer.Observer;
import sample.service.ServiceHomework;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class HomeworkController extends AbstractController implements Observer<HomeworkChangeEvent> {


    //the model
    private ObservableList<Homework> model = FXCollections.observableArrayList();

    //lists for populating combo box
    private ObservableList<String> receivedWeeksList = FXCollections.observableArrayList(
            "Received","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"
    );
    private ObservableList<String> deadlineWeeksList = FXCollections.observableArrayList(
            "Deadline","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"
    );

    //pie chart data
    private ObservableList<PieChart.Data> homeworksWithNumberOfWeeks = FXCollections.observableArrayList();

    //current selected homework
    private Homework selectedHomework;

    //services
    private ServiceHomework serviceHomework = new ServiceHomework();


    //pie charts
    @FXML
    private PieChart pieChartNumberOfWeeksPerHomework;


    //manager input fields
    @FXML
    private ComboBox<String> receivedWeekComboBox;
    @FXML
    private ComboBox<String> deadlineWeekComboBox;
    @FXML
    private TextArea descriptionTextArea;

    //manager buttons
    @FXML
    private Button saveButton;
    @FXML
    private Button extendDeadlineButton;

    //table stuff
    @FXML
    private TableView<Homework> tableView;
    @FXML
    private TableColumn<Homework, Integer> receivedColumn;
    @FXML
    private TableColumn<Homework, Integer> deadlineColumn;
    @FXML
    private TableColumn<Homework, String> descriptionColumn;

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
    void initialize() {
        serviceHomework.addObserver(this);
        initializeModel();
        initializeTableView();
        initializeComboBoxes();
        getPieChartData();
        initializeCharts();
        initializeHomeworksButtonColor();
    }

    private void initializeComboBoxes() {
        receivedWeekComboBox.setItems(receivedWeeksList);
        receivedWeekComboBox.setValue("Received");
        deadlineWeekComboBox.setItems(deadlineWeeksList);
        deadlineWeekComboBox.setValue("Deadline");
    }

    private void initializeTableView() {
        receivedColumn.setCellValueFactory(new PropertyValueFactory<>("ReceivedWeek"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("DeadlineWeek"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableView.setItems(this.model);
        tableView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {

                    putSelectedHomeworkDataIntoFields(newValue);
                    this.selectedHomework = newValue;

                });
    }

    private void initializeModel() {
        model.setAll(new ArrayList<>(serviceHomework.getSortedHomeworksById()));
    }

    private void getPieChartData() {
        homeworksWithNumberOfWeeks.clear();
        for (HomeworkDurationDTO h : this.serviceHomework.numberOfHomeworksGroupedByNumberOfWeeks()) {
            homeworksWithNumberOfWeeks.add(new PieChart.Data(h.getWeeks().toString(), h.getNumberOfHomeworksWithWeeks()));
        }
    }

    private void initializeCharts() {
        this.pieChartNumberOfWeeksPerHomework.setData(this.homeworksWithNumberOfWeeks);
        this.pieChartNumberOfWeeksPerHomework.setTitle("Homework with number of weeks");
    }

    private void putSelectedHomeworkDataIntoFields(Homework homework) {
        if (homework == null) {
            deadlineWeekComboBox.setValue("Deadline");
            receivedWeekComboBox.setValue("Received");
            descriptionTextArea.setText("");
        } else {
            deadlineWeekComboBox.setValue(homework.getDeadlineWeek().toString());
            receivedWeekComboBox.setValue(homework.getReceivedWeek().toString());
            descriptionTextArea.setText(homework.getDescription());
        }
    }


    private Homework getHomeworkFromTextInput() {
        Integer deadlineWeek, receivedWeek;
        String description;
        if (this.deadlineWeekComboBox.getValue().equals("Deadline")) {
            this.showErrorMessage("The deadline week can not be empty!");
            return null;
        }

        if (this.receivedWeekComboBox.getValue().equals("Received")) {
            this.showErrorMessage("The received week can not be empty!");
            return null;
        }

        if (this.descriptionTextArea.getText().equals("")) {
            this.showErrorMessage("The description can not be empty!");
            return null;
        }

        deadlineWeek = Integer.valueOf(this.deadlineWeekComboBox.getValue());
        receivedWeek = Integer.valueOf(this.receivedWeekComboBox.getValue());
        description = this.descriptionTextArea.getText();

        return new Homework(description, deadlineWeek, receivedWeek);

    }

    private void freeInputs(){
        receivedWeekComboBox.setValue("Received");
        deadlineWeekComboBox.setValue("Deadline");
        descriptionTextArea.setText("");
    }

    private void saveHomework() {
        Homework homework = this.getHomeworkFromTextInput();
        if (homework == null) {
            return;
        }
        try {
            Homework h = this.serviceHomework.saveHomework(homework);
            if (h == null) {
                freeInputs();
                showMessage(Alert.AlertType.INFORMATION, "Success!", "The homework was saved!");
            } else {
                this.showErrorMessage("Ops! We could not save the homework!");
            }
        } catch (Exception ex) {
            this.showErrorMessage(ex.getMessage());
        }
    }

    public void saveHomeworkButtonHandler() {
        this.saveHomework();
    }


    private void extendDeadlineOfHomework() {
        if (deadlineWeekComboBox.getValue().equals("Deadline")) {
            this.showErrorMessage("Please choose a deadline!");
            return;
        }
        if (this.selectedHomework == null) {
            this.showErrorMessage("Please choose a homework first!");
            return;
        }
        Integer deadline = Integer.valueOf(deadlineWeekComboBox.getValue());

        try {
            Homework h = this.serviceHomework.extendDeadline(this.selectedHomework.getID(), deadline, WeekHelper.currentWeek);
            if (h == null) {
                freeInputs();
                showMessage(Alert.AlertType.INFORMATION, "Success!", "The deadline was extended!");
            } else {
                this.showErrorMessage("Ops! We could not extend the deadline!");
            }
        } catch (Exception ex) {
            this.showErrorMessage(ex.getMessage());
        }

    }

    public void extendDeadlineButtonHandler() {
        this.extendDeadlineOfHomework();
    }


    /**Observer interface function implementation**/

    @Override
    public void update(HomeworkChangeEvent homeworkChangeEvent) {
        if (homeworkChangeEvent.getType().equals(ChangeEventType.ADD)) {
            initializeModel();

        }
        if(homeworkChangeEvent.getType().equals(ChangeEventType.UPDATE))
        {
            initializeModel();
        }
        if(homeworkChangeEvent.getType().equals(ChangeEventType.DELETE))
        {
            model.remove(homeworkChangeEvent.getData());
        }
        getPieChartData();
        initializeCharts();
    }


    /** Navigation buttons functions**/

    private void initializeHomeworksButtonColor() {
        this.homeworkGoButton.setStyle("-fx-background-color: #4a5a6d;-fx-text-fill: #F3AB71");
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
    public void goToExemptions() {
        super.goToExemptions();
        Stage stage = (Stage) gradeGoButton.getScene().getWindow();
        stage.close();
    }

}

package hacklympics.student.pages;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import com.jfoenix.controls.JFXComboBox;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.config.TerminalConfig;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.material.Answer;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.material.Problem;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.user.Student;
import hacklympics.utility.CodeTab;
import hacklympics.utility.AlertDialog;
import hacklympics.utility.ConfirmDialog;

public class CodeController implements Initializable {
    
    private TerminalConfig terminalConfig;
    private TerminalBuilder terminalBuilder;
    private TerminalTab terminal;
    
    @FXML
    private TabPane codeTabPane;
    @FXML
    private TabPane terminalPane;
    @FXML
    private StackPane dialogPane;

    @FXML
    private Label absolutePathLabel;
    @FXML
    private Label examLabel;
    @FXML
    private JFXComboBox problemBox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        terminalConfig = new TerminalConfig();
        terminalConfig.setBackgroundColor("#eff1f5");
        terminalBuilder = new TerminalBuilder(terminalConfig);
        terminal = terminalBuilder.newTerminal();
        terminal.getStyleClass().add("minimal-tab");
        terminalPane.getTabs().add(terminal);
        
        codeTabPane.setOnMouseClicked((Event event) -> {
            updateAbsolutePathLabel();
            closeTerminal();
        });
        
        newCodeTab();
    }
    
    
    public void newFile(ActionEvent event) {
        newCodeTab();
        updateAbsolutePathLabel();
    }
    
    public void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File...");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Java Source Code", "*.java"),
                new ExtensionFilter("All Files", "*.*"));
        
        File selectedFile = fileChooser.showOpenDialog(codeTabPane.getScene().getWindow());
        
        if (selectedFile != null) {
            try {
                newCodeTab(selectedFile).open();
                updateAbsolutePathLabel();
            } catch (IOException ioe) {
                AlertDialog alert = new AlertDialog(
                        dialogPane,
                        "Error",
                        "Unable to open the specified file."
                );
                
                alert.show();
                codeTabPane.getTabs().remove(getCurrentTab());
            }
        }
    }
    
    public void saveFile(ActionEvent event) {
        if (getCurrentTab().getFile() != null) {
            try {
                getCurrentTab().save();
            } catch (IOException ioe) {
                AlertDialog alert = new AlertDialog(
                        dialogPane,
                        "Error",
                        "Unable to write to the specified file."
                );
                
                alert.show();
            }
        } else {
            saveFileAs(new ActionEvent());
        }
    }
    
    public void saveFileAs(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As...");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Java Source Code", "*.java"),
                new ExtensionFilter("All Files", "*.*"));
        
        File selectedFile = fileChooser.showSaveDialog(codeTabPane.getScene().getWindow());
        
        if (selectedFile != null) {
            getCurrentTab().setFile(selectedFile);
            saveFile(new ActionEvent());
        }
    }
    
    public void closeFile(ActionEvent e) {
        codeTabPane.getTabs().remove(getCurrentTab());
        updateAbsolutePathLabel();
    }
    
    
    public void toggleTerminal(ActionEvent event) {
        if (terminalPane.getOpacity() == 0) {
            showTerminal();
        } else {
            closeTerminal();
        }
    }
    
    
    public void compile(ActionEvent event) throws IOException {
        showTerminal();
        
        CodeTab current = getCurrentTab();
        String location = current.getLocation();
        String filepath = current.getFilepath();
        
        terminal.onTerminalFxReady(() -> {
            terminal.command(String.join(" ", "javac", "-cp", location, filepath, "\r"));
        });
    }
    
    public void execute(ActionEvent event) {
        showTerminal();
        
        CodeTab current = getCurrentTab();
        String location = current.getLocation();
        String className = current.getFilename().split("[.]")[0];
        
        terminal.onTerminalFxReady(() -> {
            terminal.command(String.join(" ", "java", "-cp", location, className, "\r"));
        });
    }
    
    
    public void submit(ActionEvent event) {
        Student student = (Student) Session.getInstance().getCurrentUser();
        Exam selectedExam = Session.getInstance().getCurrentExam();
        Problem selectedProblem = (Problem) problemBox.getSelectionModel().getSelectedItem();
        
        if (selectedExam == null | selectedProblem == null) {
            AlertDialog alert = new AlertDialog(
                    dialogPane,
                    "Tips",
                    "Please make sure both Exam and Problem are selected."
            );
            
            alert.show();
            return;
        }
        
        ConfirmDialog confirm = new ConfirmDialog(
                dialogPane,
                "Submit Answer",
                String.format(
                        "Submitting \"%s\" for \"%s\".\n\n"
                      + "Once submitted, you will NOT be able to revise it.\n"
                      + "Submit your code now?",
                        getCurrentTab().getFilename(),
                        selectedProblem
                )
        );
        
        confirm.getConfirmBtn().setOnAction((ActionEvent e) -> {
            Response create = Answer.create(
                    selectedExam.getCourseID(),
                    selectedExam.getExamID(),
                    selectedProblem.getProblemID(),
                    getCurrentTab().getFilename(),
                    getCurrentTab().getCodeArea().getText(),
                    student.getUsername()
            );
            
            confirm.close();
            
            switch (create.getStatusCode()) {
                case SUCCESS:
                    Answer answer = new Answer(
                            selectedExam.getCourseID(),
                            selectedExam.getExamID(),
                            selectedProblem.getProblemID(),
                            (int) Double.parseDouble(create.getContent().get("id").toString())
                    );
                    
                    validate(answer);
                    break;
                    
                case ALREADY_SUBMITTED:
                    AlertDialog submitted = new AlertDialog(
                            dialogPane,
                            "Tips",
                            "You've already submitted your code for this problem."
                    );
                    
                    submitted.show();
                    break;
                    
                default:
                    AlertDialog error = new AlertDialog(
                            dialogPane,
                            "Error",
                            "ErrorCode: " + create.getStatusCode()
                    );
                    
                    error.show();
                    break;
            }
        });
        
        confirm.show();
    }
    
    private void validate(Answer answer) {
        Response validate = answer.validate();
        
        switch (validate.getStatusCode()) {
            case SUCCESS:
                AlertDialog correct = new AlertDialog(
                        dialogPane,
                        "Congratulations",
                        "Your code works correctly. Nice work!"
                );
                
                correct.show();
                break;
            case INCORRECT_ANSWER:
                AlertDialog failed = new AlertDialog(
                        dialogPane,
                        "Sorry",
                        "It seems that your code doesn't work out,"
                      + "keep going!"
                );
                
                failed.show();
                break;
            default:
                AlertDialog error = new AlertDialog(
                        dialogPane,
                        "Error",
                        "ErrorCode: " + validate.getStatusCode()
                );
                
                error.show();
                break;
        }
    }
    
    // Still buggy. DialogTextWrapper needed.
    public void showHint(ActionEvent e) {
        Problem selectedProblem = (Problem) problemBox.getSelectionModel().getSelectedItem();
        if (selectedProblem == null) return;
        
        AlertDialog submitted = new AlertDialog(
                dialogPane,
                selectedProblem.getTitle(),
                selectedProblem.getDesc()
        );
        dialogPane.setMaxWidth(124.0);
        dialogPane.setPrefWidth(124.0);
        submitted.show();
    }
    
    
    private CodeTab newCodeTab() {
        CodeTab c = new CodeTab();
        codeTabPane.getTabs().add(c);
        codeTabPane.getSelectionModel().select(c);
        
        return c;
    }
    
    private CodeTab newCodeTab(File file) {
        CodeTab c = new CodeTab(file);
        codeTabPane.getTabs().add(c);
        codeTabPane.getSelectionModel().select(c);
        
        return c;
    }
    
    private void updateAbsolutePathLabel() {
        CodeTab current = getCurrentTab();
        String filename = (current == null) ? "" : current.getFilepath();
        absolutePathLabel.setText(filename);
    }
    
    private CodeTab getCurrentTab() {
        return ((CodeTab) codeTabPane.getSelectionModel().getSelectedItem());
    }
    
    
    private void showTerminal() {
        terminalPane.setOpacity(100);
        terminalPane.setMouseTransparent(false);
    }
    
    private void closeTerminal() {
        terminalPane.setOpacity(0);
        terminalPane.setMouseTransparent(true);
    }
    
    
    public void setExamLabel(String s) {
        examLabel.setText(s);
    }
    
    public void setProblemBox(List<Problem> problems) {
        problemBox.getItems().clear();
        problemBox.getItems().addAll(problems);
    }
    
}
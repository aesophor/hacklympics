package hacklympics.student.pages;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.fxmisc.richtext.CodeArea;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.config.TerminalConfig;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.materials.Answer;
import com.hacklympics.api.materials.Exam;
import com.hacklympics.api.materials.Problem;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.users.Student;
import com.jfoenix.controls.JFXTextArea;
import hacklympics.utility.AlertDialog;
import hacklympics.utility.CodeTab;
import hacklympics.utility.FormDialog;
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
    private Label examLabel;
    @FXML
    private JFXComboBox problemBox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        terminalConfig = new TerminalConfig();
        terminalConfig.setBackgroundColor("#eff1f5");
        terminalBuilder = new TerminalBuilder(terminalConfig);
        terminal = terminalBuilder.newTerminal();
        terminalPane.getTabs().add(terminal);
        
        newCodeTab();
    }
    
    
    private CodeArea getCurrentCodeArea() {
        return getCurrentTab().getCodeArea();
    }
    
    private CodeTab getCurrentTab() {
        return ((CodeTab) codeTabPane.getSelectionModel().getSelectedItem());
    }
    
    
    public void newFile(ActionEvent event) {
        newCodeTab();
    }
    
    private CodeTab newCodeTab() {
        CodeTab c = new CodeTab("Untitled");
        codeTabPane.getTabs().add(c);
        codeTabPane.getSelectionModel().select(c);
        
        return c;
    }
    
    public void openFile(ActionEvent event) {
        FormDialog form = new FormDialog(dialogPane, "Open File");
        form.addTextField("Path to the file", "");
        
        form.getConfirmBtn().setOnAction((ActionEvent e) -> {
            JFXTextField filepathField = (JFXTextField) form.get("Path to the file");
            String filepath = filepathField.getText();
            
            form.close();
            
            try {
                BufferedReader file = new BufferedReader(new FileReader(filepath));
                
                StringBuilder content = new StringBuilder();
                String line = file.readLine();
                
                while (line != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                    line = file.readLine();
                }
                
                newCodeTab().getCodeArea().replaceText(0, 0, content.toString());
                getCurrentTab().setFilename(filepath);
                
            } catch (IOException ioe) {
                
            }
        });

        form.show();
    }
    
    public void saveFile(ActionEvent event) {
        /*
        FormDialog form = new FormDialog(dialogPane, "Save the file as ...");
        form.addTextField("Filename", "Program.java");
        
        form.getConfirmBtn().setOnAction((ActionEvent save) -> {
            JFXTextField filenameField = (JFXTextField) form.get("Filename");
            filename = filenameField.getText();
            
            try {
                BufferedWriter code = new BufferedWriter(new FileWriter(filename));
                code.write(codeArea.getText());
                code.flush();
                //markAsSaved();
            } catch (IOException ioe) {
                
            }
            
            form.close();
        });
        
        form.show();
*/
    }
    
    public void closeFile(ActionEvent e) {
        Tab selected = codeTabPane.getSelectionModel().getSelectedItem();
        codeTabPane.getTabs().remove(selected);
    }
    
    
    
    
    public void compile(ActionEvent event) throws IOException {
        /*
        terminal.onTerminalFxReady(() -> {
            terminal.command(String.join(" ", "javac", filename, "\r"));
        });
        */
    }
    
    public void execute(ActionEvent event) {
        /*
        String className = filename.split("[.]")[0];
        terminal.onTerminalFxReady(() -> {
            terminal.command(String.join(" ", "java", className, "\r"));
        });
*/
    }
    
    
    
    
    
    
    public void submit(ActionEvent event) {
        /*
        Student student = (Student) Session.getInstance().getCurrentUser();
        Exam selectedExam = Session.getInstance().getCurrentExam();
        Problem selectedProblem = (Problem) problemBox.getSelectionModel().getSelectedItem();
        
        ConfirmDialog confirm = new ConfirmDialog(
                dialogPane,
                "Submit Code",
                "Once submitted, you will NOT be able to revise it.\n\n"
              + "Submit your code now?"
        );
        
        confirm.getConfirmBtn().setOnAction((ActionEvent e) -> {
            Response create = Answer.create(
                    selectedExam.getCourseID(),
                    selectedExam.getExamID(),
                    selectedProblem.getProblemID(),
                    filename,
                    codeArea.getText(),
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
                            "You have already submitted your code for this problem."
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
*/
    }
    
    private void validate(Answer answer) {
        Response validate = answer.validate();
        
        switch (validate.getStatusCode()) {
            case SUCCESS:
                AlertDialog correct = new AlertDialog(
                        dialogPane,
                        "Congratulations",
                        "Your code works correctly!"
                );
                
                correct.show();
                break;
            case INCORRECT_ANSWER:
                AlertDialog failed = new AlertDialog(
                        dialogPane,
                        "Sorry",
                        "Your code doesn't work out..."
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
    
    public void showHint(ActionEvent e) {
        Problem selectedProblem = (Problem) problemBox.getSelectionModel().getSelectedItem();
        if (selectedProblem == null) return;
        
        AlertDialog submitted = new AlertDialog(
                dialogPane,
                selectedProblem.getTitle(),
                selectedProblem.getDesc()
        );
        
        submitted.show();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    public void markAsUnsaved(KeyEvent e) {
        markAsUnsaved();
    }
    
    private void markAsUnsaved() {
        //filenameLabel.setText(String.format("* [%s]", filename));
    }
    
    private void markAsSaved() {
        //filenameLabel.setText(String.format("%s", filename));
    }
    
    
    public void setExamLabel(String s) {
        examLabel.setText(s);
    }
    
    public void setProblemBox(List<Problem> problems) {
        problemBox.getItems().clear();
        problemBox.getItems().addAll(problems);
    }
    
}

package hacklympics.student.pages;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import org.fxmisc.richtext.CodeArea;
import com.jfoenix.controls.JFXTextField;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.config.TerminalConfig;
import hacklympics.utility.CodeAreaBox;
import hacklympics.utility.FormDialog;

public class CodeController implements Initializable {
    
    private CodeAreaBox codeAreaBox;
    private String filename;
    
    private TerminalBuilder terminalBuilder;
    private TerminalTab terminal;

    @FXML
    private Label filenameLabel;
    @FXML
    private Label examLabel;
    @FXML
    private StackPane stackPane;
    @FXML
    private CodeArea codeArea;
    @FXML
    private TabPane terminalPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codeAreaBox = new CodeAreaBox(codeArea);
        
        terminalBuilder = new TerminalBuilder(new TerminalConfig());
        terminal = terminalBuilder.newTerminal();
        terminalPane.getTabs().add(terminal);
    }
    
    public void save(ActionEvent e) {
        FormDialog form = new FormDialog(stackPane, "Save the file as ...");
        form.addTextField("Filename", "Program.java");
        
        form.getConfirmBtn().setOnAction((ActionEvent save) -> {
            JFXTextField filenameField = (JFXTextField) form.get("Filename");
            filename = filenameField.getText();
            
            try {
                BufferedWriter code = new BufferedWriter(new FileWriter(filename));
                code.write(codeArea.getText());
                code.flush();
                
                filenameLabel.setText(filename);
            } catch (IOException ioe) {
                
            }
            
            form.close();
        });
        
        form.show();
    }
    
    public void compile(ActionEvent event) throws IOException {
        terminal.onTerminalFxReady(() -> {
            terminal.command(String.join(" ", "javac", filename, "\r"));
        });
    }
    
    public void execute(ActionEvent event) {
        String className = filename.split("[.]")[0];
        terminal.onTerminalFxReady(() -> {
            terminal.command(String.join(" ", "java", className, "\r"));
        });
    }
    
    public void submit(ActionEvent event) {
        
    }
    
    
    public void setExamLabel(String s) {
        examLabel.setText(s);
    }
    
}

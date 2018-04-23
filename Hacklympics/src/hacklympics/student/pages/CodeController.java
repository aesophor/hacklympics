package hacklympics.student.pages;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TabPane;
import com.jfoenix.controls.JFXTextField;
import org.fxmisc.richtext.CodeArea;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.config.TerminalConfig;
import hacklympics.utility.CodeAreaBox;
import hacklympics.utility.FormDialog;

public class CodeController implements Initializable {
    
    private CodeAreaBox codeAreaBox;
    private String filename;
    
    private TerminalConfig defaultConfig;
    private TerminalBuilder terminalBuilder;
    private TerminalTab terminal;

    @FXML
    private StackPane stackPane;
    @FXML
    private TabPane terminalPane;
    @FXML
    private CodeArea codeArea;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.codeAreaBox = new CodeAreaBox(codeArea);
        
        this.defaultConfig = new TerminalConfig();
        this.terminalBuilder = new TerminalBuilder(defaultConfig);
        this.terminal = this.terminalBuilder.newTerminal();
        this.terminalPane.setPadding(new Insets(3, 0, 3, 0));
        this.terminalPane.getTabs().add(this.terminal);
    }
    
    
    public void save(ActionEvent e) {
        FormDialog form = new FormDialog(stackPane, "Save the file as ...");
        form.addField("Filename", "Program.java");
        
        form.getConfirmBtn().setOnAction((ActionEvent save) -> {
            JFXTextField filenameField = (JFXTextField) form.get("Filename");
            this.filename = filenameField.getText();
            
            try {
                BufferedWriter code = new BufferedWriter(new FileWriter(this.filename));
                code.write(codeArea.getText());
                code.flush();
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
    
}

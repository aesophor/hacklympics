package com.hacklympics.student.code;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;

import com.hacklympics.utility.Utils;
import com.hacklympics.utility.code.CodePatch;
import com.hacklympics.utility.code.CodeUtils;
import com.hacklympics.utility.code.lang.Language;

public class FileTab extends Tab {
    
    private final AnchorPane anchorPane;
    private final VBox vbox;
    private final StyledCodeArea styledCodeArea;
    
    private File file;
    private boolean unsaved;

    public FileTab() {
        super("Untitled.java");
        
        // Initialize a styled CodeArea in current tab.
        styledCodeArea = new StyledCodeArea(Language.JAVA);
        
        // Whenever there's a change to the CodeArea, we compute the diff,
        // create a patch and add that patch to keystrokeHistory.
        // Later on we can send these patches to the teacher's client.
        // Make sure to mark current tab as unsaved as well.
        styledCodeArea.textProperty().addListener((observable, original, revised) -> {
            CodePatch patch = CodeUtils.diff(original, revised);
            
            try {
                synchronized (PendingCodePatches.getInstance()) {
                    PendingCodePatches.getInstance().add(Utils.serialize(patch));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                markAsUnsaved();
            }
        });
        
        // Then add sample code to the CodeArea.
        styledCodeArea.replaceText(styledCodeArea.getCurrentLanguage().getSampleCode());
        
        // Add the code area we just created into a VBox.
        vbox = new VBox();
        vbox.getStyleClass().add("code-vbox");
        vbox.getChildren().add(styledCodeArea);

        // Add the vbox into the AnchorPane.
        anchorPane = new AnchorPane();
        anchorPane.getStyleClass().add("code-anchor");
        anchorPane.getChildren().add(vbox);
        setContent(anchorPane);
        
        // Spice up the tab with css.
        getStyleClass().add("minimal-tab");
        markAsUnsaved();
    }
    

    /**
     * Opens the specified file in the tab.
     * @throws IOException if an input or output exception occurred.
     */
    public void open(File file) throws IOException {
        // Associate the file with this tab, and set up the tab title.
        this.file = file;

        // Now read the content of file into the CodeArea.
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder content = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            content.append(line);
            content.append(System.lineSeparator());
            line = br.readLine();
        }

        styledCodeArea.replaceText(content.toString());
        unsaved = false;
        br.close();
        
        setText(getFilename());
    }

    /**
     * Saves the content in the tab into a file.
     * @throws IOException if an input or output exception occurred.
     */
    public void save() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(styledCodeArea.getText());
        bw.flush();
        bw.close();

        setText(getFilename());
        unsaved = false;
    }

    
    /**
     * Gets whether this file has unsaved changes.
     * @return whether this file is unsaved.
     */
    public boolean unsaved() {
        return unsaved;
    }
    
    /**
     * Marks current file as unsaved, prepending the title of 
     * this tab with a "*" character.
     */
    private void markAsUnsaved() {
        if (unsaved == false) {
            unsaved = true;
            String title = getText();
            setText(String.format("* %s", title));
        }
    }

    
    /**
     * Gets the file opened in this tab.
     * @return file opened.
     */
    public File getFile() {
        return file;
    }
    
    /**
     * Sets the file opened in this tab.
     * @param file opened.
     */
    public void setFile(File file) {
        this.file = file;
    }
    
    /**
     * Gets the absolute path of the file opened in this tab.
     * @return absolute path of the file.
     */
    public String getAbsoluteFilePath() {
        return (file == null) ? "Unsaved file" : file.getAbsolutePath();
    }
    
    /**
     * Gets the filename of the file opened in this tab.
     * @return filename (with extension).
     */
    public String getFilename() {
        if (file == null) {
            return "Untitled";
        } else {
            String[] parts = file.getAbsolutePath().split("[/]");
            return parts[parts.length - 1];
        }
    }
    
    /**
     * Gets the extension of the file opened in this tab.
     * @return extension.
     */
    public String getFileExtension() {
        if (getFilename().contains(".")) {
            return getFilename().split("[.]")[1];
        } else {
            return "";
        }
    }

    /**
     * Gets the location of the file opened in this tab.
     * @return the directory in which the file is saved.
     */
    public String getFileLocation() {
        return file.getAbsoluteFile().getParent();
    }
    
    
    /**
     * Gets the StyledCodeArea embedded in this tab.
     * @return the StyledCodeArea.
     */
    public StyledCodeArea getStyledCodeArea() {
        return styledCodeArea;
    }

}
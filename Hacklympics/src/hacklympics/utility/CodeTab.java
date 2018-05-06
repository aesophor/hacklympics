package hacklympics.utility;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

public class CodeTab extends Tab {
    
    private static final String COLORSCHEME = "/resources/JavaKeywords.css";
    
    private static final String[] KEYWORDS = new String[] {
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
     
   private static final Pattern PATTERN = Pattern.compile(
           "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
         + "|(?<PAREN>" + PAREN_PATTERN + ")"
         + "|(?<BRACE>" + BRACE_PATTERN + ")"
         + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
         + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
         + "|(?<STRING>" + STRING_PATTERN + ")"
         + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    private static final String SAMPLE_CODE = String.join("\n", new String[] {
            "import java.util.*;",
            "",
            "public class Program {",
            "",
            "    public static void main(String[] args) {",
            "        // write your code right here.",
            "    }",
            "",
            "}"
    });
    
    private final AnchorPane anchorPane;
    private final VBox vbox;
    private final CodeArea codeArea;
    private File file;
    private boolean unsaved;
    
    public CodeTab() {
        super("Untitled");
        
        codeArea = new CodeArea();
        codeArea.getStyleClass().add("code-area");
        codeArea.getStylesheets().add(COLORSCHEME);
        
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(200))
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));
        
        codeArea.replaceText(0, 0, SAMPLE_CODE);
        
        codeArea.setOnKeyPressed((KeyEvent event) -> {
            markAsUnsaved();
        });
        
        
        vbox = new VBox();
        vbox.getStyleClass().add("code-vbox");
        vbox.getChildren().add(codeArea);
        
        
        anchorPane = new AnchorPane();
        anchorPane.getStyleClass().add("code-anchor");
        anchorPane.getChildren().add(vbox);
        
        
        getStyleClass().add("minimal-tab");
        setContent(anchorPane);
        
        
        markAsUnsaved();
    }
    
    public CodeTab(File file) {
        this();
        
        this.file = file;
        this.setText(getFilename());
    }
    
    
    public static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                    matcher.group("PAREN") != null ? "paren" :
                    matcher.group("BRACE") != null ? "brace" :
                    matcher.group("BRACKET") != null ? "bracket" :
                    matcher.group("SEMICOLON") != null ? "semicolon" :
                    matcher.group("STRING") != null ? "string" :
                    matcher.group("COMMENT") != null ? "comment" :
                    null;
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
    
    
    public void open() throws IOException {
        if (file != null) {
            BufferedReader br = new BufferedReader(new FileReader(file));
        
            StringBuilder content = new StringBuilder();
            String line = br.readLine();
                
            while (line != null) {
                content.append(line);
                content.append(System.lineSeparator());
                line = br.readLine();
            }
            
            codeArea.replaceText(content.toString());
            unsaved = false;
            br.close();
        }
    }
    
    public void save() throws IOException {
        if (file != null) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(codeArea.getText());
            bw.flush();
            bw.close();
            
            setText(getFilename());
            unsaved = false;
        }
    }
    
    
    private void markAsUnsaved() {
        if (unsaved == false) {
            unsaved = true;
            String title = getText();
            setText(String.format("* %s", title));
        }
    }
    
    
    public String getFilepath() {
        return (file == null) ? "Untitled" : file.getAbsolutePath();
    }
    
    public String getFilename() {
        if (file == null) {
            return "Untitled";
        } else {
            String[] parts = file.getAbsolutePath().split("[/]");
            return parts[parts.length-1];
        }
    }
    
    public String getLocation() {
        return file.getAbsoluteFile().getParent().toString();
    }
    
    
    public File getFile() {
        return file;
    }
    
    public void setFile(File file) {
        this.file = file;
    }
    
    public CodeArea getCodeArea() {
        return codeArea;
    }
    
}
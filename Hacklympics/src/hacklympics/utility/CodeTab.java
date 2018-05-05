package hacklympics.utility;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Tab;
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
    
    private String filepath;
    private String filename;
    
    public CodeTab(String filepath) {
        super(filepath);
        this.filename = filepath;
        
        codeArea = new CodeArea();
        codeArea.getStyleClass().add("code-area");
        codeArea.getStylesheets().add(COLORSCHEME);
        
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(200))
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));
        
        codeArea.replaceText(0, 0, SAMPLE_CODE);
        
        
        vbox = new VBox();
        vbox.getStyleClass().add("code-vbox");
        vbox.getChildren().add(codeArea);
        
        
        anchorPane = new AnchorPane();
        anchorPane.getStyleClass().add("code-anchor");
        anchorPane.getChildren().add(vbox);
        
        
        getStyleClass().add("file-tab");
        setContent(anchorPane);
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
    
    
    public CodeArea getCodeArea() {
        return codeArea;
    }
    
    public String getFilepath() {
        return filepath;
    }
    
    public String getFilename() {
        return filename;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
        this.setText(filename);
    }
    
}

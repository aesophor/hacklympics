package hacklympics.common;

import java.util.Map;
import java.util.HashMap;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXListView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class DropDownUserMenu {

    private static boolean showingMenu;

    private final Map<String, EventHandler<MouseEvent>> handlers;
    private final JFXListView<Label> labels;
    private final JFXPopup dropdownMenu;

    public DropDownUserMenu() {
        this.handlers = new HashMap<>();
        this.labels = new JFXListView<>();
        
        this.labels.getStyleClass().add("user-menu-list");
        this.dropdownMenu = new JFXPopup(labels);
        
        // Automatically calls the corresponding event handler.
        // handlers are put in handlers (HashMap<String, EventHandler>)
        this.labels.setOnMouseClicked((MouseEvent event) -> {
            Label selectedLabel = this.labels.getSelectionModel().getSelectedItem();
            this.handlers.get(selectedLabel.getText()).handle(event);
        });
    }

    public void add(Label item, EventHandler<MouseEvent> handler) {
        this.labels.getItems().add(item);
        this.handlers.put(item.getText(), handler);
    }

    public void show(Button triggeringBtn) {
        // If user clicks on the menu but it's already being shown,
        // then close the menu instead.
        if (showingMenu == true) {
            this.hide();
            return;
        }
        
        this.dropdownMenu.show(
                triggeringBtn,
                JFXPopup.PopupVPosition.TOP,
                JFXPopup.PopupHPosition.LEFT,
                - (triggeringBtn.getWidth() / 3),
                triggeringBtn.getHeight()
        );
        
        showingMenu = true;
    }

    public void hide() {
        this.labels.getSelectionModel().clearSelection();
        this.dropdownMenu.hide();
        
        showingMenu = false;
    }

}

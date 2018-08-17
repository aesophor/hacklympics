package com.hacklympics.common;

import java.util.Map;
import java.util.HashMap;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXListView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class UserMenu {

    private static boolean showingMenu;

    private final Map<String, EventHandler<MouseEvent>> handlers;
    private final JFXListView<Label> labels;
    private final JFXPopup dropdownMenu;

    public UserMenu() {
        handlers = new HashMap<>();
        labels = new JFXListView<>();
        
        labels.getStyleClass().add("user-menu-list");
        dropdownMenu = new JFXPopup(labels);
        
        // Automatically calls the corresponding event handler.
        // handlers are put in handlers (HashMap<String, EventHandler>)
        labels.setOnMouseClicked((MouseEvent event) -> {
            Label selectedLabel = labels.getSelectionModel().getSelectedItem();
            handlers.get(selectedLabel.getText()).handle(event);
        });
    }

    public void add(Label item, EventHandler<MouseEvent> handler) {
        labels.getItems().add(item);
        handlers.put(item.getText(), handler);
    }

    public void show(Button triggeringBtn) {
        // If user clicks on the menu but it's already being shown,
        // then close the menu instead.
        if (showingMenu == true) {
            hide();
            return;
        }
        
        dropdownMenu.show(
                triggeringBtn,
                JFXPopup.PopupVPosition.TOP,
                JFXPopup.PopupHPosition.LEFT,
                - (triggeringBtn.getWidth() / 3),
                triggeringBtn.getHeight() + 3
        );
        
        showingMenu = true;
    }

    public void hide() {
        labels.getSelectionModel().clearSelection();
        dropdownMenu.hide();
        
        showingMenu = false;
    }

}

package com.hacklympics.common.about;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;

public class AboutController implements Initializable {

    @FXML
    private Hyperlink githubHyperlink;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		githubHyperlink.setOnAction((ActionEvent event) -> {
			// Open url here.
		});
	}
    
}

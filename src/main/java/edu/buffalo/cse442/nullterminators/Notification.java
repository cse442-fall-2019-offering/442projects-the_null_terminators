package edu.buffalo.cse442.nullterminators;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class Notification {
    private String _datetime;
    private String _title;
    private String _description;

    public void setDatetime(String datetime){
        _datetime = datetime;
    }
    public void setTitle(String title){
        _title = title;
    }
    public void setDescription(String description){
        _description = description;
    }
    public boolean showDialog(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event: " + _title);
        alert.setHeaderText(null);
        alert.setContentText(_datetime + "\n" + _description);
        // Work around JavaFX alert dialog bug
        // See https://stackoverflow.com/questions/55190380/javafx-creates-alert-dialog-which-is-too-small
        alert.setResizable(true);
        alert.onShownProperty().addListener(e -> {
            Platform.runLater(() -> alert.setResizable(false));
        });
        alert.showAndWait();
        return true;
    }


}

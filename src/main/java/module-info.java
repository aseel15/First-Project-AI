module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.FirstProject to javafx.fxml;
    exports com.example.FirstProject;
}
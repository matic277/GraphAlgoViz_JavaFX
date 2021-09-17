module com.example.gav_fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.jgrapht.core;
    requires org.apache.commons.lang3;
    requires org.jgrapht.io;
    
    opens com.example.gav_fx to javafx.fxml;
    exports com.example.gav_fx;
}
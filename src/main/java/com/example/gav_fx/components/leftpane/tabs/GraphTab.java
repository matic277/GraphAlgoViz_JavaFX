package com.example.gav_fx.components.leftpane.tabs;

import com.example.gav_fx.core.LOG;
import com.example.gav_fx.core.LayoutType;
import com.example.gav_fx.core.Tools;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class GraphTab extends TabContentComponent {
    
    private boolean doingLayout = false;
    
    private Button applyBtn;
    private VBox contentContainer;
    
    public GraphTab() {
        VBox layoutComponent = getLayoutComponent();
        this.getChildren().add(layoutComponent);
    }
    
    public VBox getLayoutComponent() {
        HBox titleContainer = getTitleContainer("Set layout");
    
        ComboBox<LayoutType> dropdown = new ComboBox<>();
        dropdown.setItems(FXCollections.observableList(Arrays.stream(LayoutType.values()).toList()));
        
        applyBtn = new Button("Apply");
        applyBtn.setOnAction(e -> {
            applyBtn.setDisable(true);
            doGraphLayout(dropdown.getSelectionModel().getSelectedItem());
        });
        
        contentContainer = new VBox(dropdown, applyBtn);
        contentContainer.setPadding(new Insets(5, 5, 5, 5));
        contentContainer.setSpacing(5);
        contentContainer.setAlignment(Pos.CENTER_RIGHT);
        
        return getMainContainer(titleContainer, contentContainer);
    }
    
    private void doGraphLayout(LayoutType selectedlayout) {
        Label infoLbl = new Label("Doing layout...");
        infoLbl.setTextFill(Color.GREEN);
        
        CompletableFuture
                .runAsync(() -> {
                    Platform.runLater(() -> contentContainer.getChildren().add(infoLbl));
                    try {
                        selectedlayout.getLayoutExecutor().run();
                        Platform.runLater(() -> infoLbl.setText("Done!"));
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            infoLbl.setTextFill(Color.RED);
                            infoLbl.setText(e.getLocalizedMessage());
                        });
                        LOG.error("Error doing layout:\n" + e);
                    }
                    Tools.sleep(1500);
                    Platform.runLater(() -> contentContainer.getChildren().remove(infoLbl));
                })
                .thenAccept(v -> {
                    applyBtn.setDisable(false);
                });
    }
    
    @Override
    public String getTabName() {
        return "Graph";
    }
}

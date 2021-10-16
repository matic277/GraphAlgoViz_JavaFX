package com.example.gav_fx.panes.leftpane.tabs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public abstract class TabElement extends VBox {
    
    public TabElement() {
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setSpacing(20);
    }
    
    public abstract String getTabName();
    
    public HBox getTitleContainer(String title) {
        Text text = new Text(title);
        text.getStyleClass().add("-fx-font-weight: bold;"); // TODO color is same as text-clr var in css, can't see to make it work
        text.setStroke(Color.rgb(215, 215, 215));
        HBox titleContainer = new HBox(text);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setPadding(new Insets(3, 3, 3, 3));
        titleContainer.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(5, 5, 0, 0, false), Insets.EMPTY)));
        return titleContainer;
    }
    
    public VBox getMainContainer(Pane... nodes) {
        VBox mainContainer = new VBox(nodes);
        mainContainer.setMaxWidth(215);
        mainContainer.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
        mainContainer.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(5), Insets.EMPTY)));
        return mainContainer;
    }
}

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
    
    private static final Color COMPONENT_OUTLINE_COLOR =
            Color.BLACK
            //Color.rgb(76, 80, 82); // TODO strange color artifacts/discoloring around egdes... super annoying
            ;
    private static final int CORNER_RADII = 8;
    
    public HBox getTitleContainer(String title) {
        Text text = new Text(title);
        text.getStyleClass().add("-fx-font-weight: bold;"); // TODO color is same as text-clr var in css, can't see to make it work
        text.setStroke(Color.rgb(215, 215, 215));
        HBox titleContainer = new HBox(text);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setPadding(new Insets(3, 3, 3, 3));
        titleContainer.setBackground(new Background(new BackgroundFill(COMPONENT_OUTLINE_COLOR, new CornerRadii(CORNER_RADII, CORNER_RADII, 0, 0, false), Insets.EMPTY)));
        return titleContainer;
    }
    
    public VBox getMainContainer(Pane... nodes) {
        VBox mainContainer = new VBox(nodes);
        mainContainer.setMaxWidth(215);
        mainContainer.setBorder(new Border(new BorderStroke(COMPONENT_OUTLINE_COLOR, BorderStrokeStyle.SOLID, new CornerRadii(CORNER_RADII), BorderWidths.DEFAULT)));
        mainContainer.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(CORNER_RADII), Insets.EMPTY)));
        return mainContainer;
    }
}

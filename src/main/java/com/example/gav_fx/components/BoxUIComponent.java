package com.example.gav_fx.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class BoxUIComponent extends VBox {
    
    private static final int CORNER_RADII = 5;
    private static final Color TITLE_COLOR =
            Color.BLACK
            //Color.rgb(76, 80, 82);
            ;
    
    private final VBox mainContainer;
    private final HBox titleContainer;
    private final Text titleText;
    
    public BoxUIComponent() {
        titleText = new Text("null");
        titleText.setStroke(Color.rgb(215, 215, 215));
        
        titleContainer = new HBox(titleText);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setPadding(new Insets(3, 3, 3, 3));
        titleContainer.setBackground(new Background(new BackgroundFill(TITLE_COLOR, new CornerRadii(CORNER_RADII, CORNER_RADII, 0, 0, false), Insets.EMPTY)));
        
        mainContainer = new VBox();
        // Note: using .setBorder with rounded corners produces
        // strange off-coloring artifacts around edges... super annoying
        // this does not happen when you set rounded corners via css
        //mainContainer.setBorder(new Border(new BorderStroke(TITLE_COLOR, BorderStrokeStyle.SOLID, new CornerRadii(CORNER_RADII-1), BorderWidths.DEFAULT)));
        mainContainer.getStylesheets().add("left-menu-parent-component");
        mainContainer.setMaxWidth(215);
        mainContainer.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(CORNER_RADII+1), Insets.EMPTY)));
    
        mainContainer.getChildren().add(titleContainer);
        this.getChildren().add(mainContainer);
    }
    
    public HBox getTitleContainer() {
        return titleContainer;
    }
    
    public Text getTitleLabel() {
        return titleText;
    }
    
    public VBox getMainContainer() {
        return mainContainer;
    }
}

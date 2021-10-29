package com.example.gav_fx.components.leftpane.tabs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

public abstract class TabContentComponent extends VBox {
    
    private static final int CORNER_RADII = 5;
    private static final Color TITLE_COLOR =
            Color.BLACK
            //Color.rgb(76, 80, 82);
            ;
    
    public TabContentComponent() {
        this.setPadding(new Insets(15, 10, 10, 10));
        this.setSpacing(20);
        this.getStyleClass().add("tab-content-component");
    }
    
    public abstract String getTabName();
    
    public HBox getTitleContainer(String title) {
        Text text = new Text(title);
        text.setStroke(Color.rgb(215, 215, 215));
        HBox titleContainer = new HBox(text);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setPadding(new Insets(3, 3, 3, 3));
        titleContainer.setBackground(new Background(new BackgroundFill(TITLE_COLOR, new CornerRadii(CORNER_RADII, CORNER_RADII, 0, 0, false), Insets.EMPTY)));
        return titleContainer;
    }
    
    public VBox getMainContainer(Pane... nodes) {
        VBox mainContainer = new VBox(nodes);
        mainContainer.setMaxWidth(215);
        
        // Note: using .setBorder with rounded corners produces
        // strange off-coloring artifacts around edges... super annoying
        // this does not happen when you set rounded corners via css
        //mainContainer.setBorder(new Border(new BorderStroke(TITLE_COLOR, BorderStrokeStyle.SOLID, new CornerRadii(CORNER_RADII-1), BorderWidths.DEFAULT)));
        mainContainer.getStylesheets().add("left-menu-parent-component");
        
        mainContainer.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(CORNER_RADII+1), Insets.EMPTY)));
        return mainContainer;
    }
    
    public Button getSearchIconButton() {
        // Source: https://commons.wikimedia.org/wiki/File:VisualEditor_-_Icon_-_Search.svg
        SVGPath svgIcon = new SVGPath();
        svgIcon.setContent("M16.021,15.96l-2.374-2.375c-0.048-0.047-0.105-0.079-0.169-0.099c0.403-0.566,0.643-1.26,0.643-2.009 " +
                "C14.12,9.557,12.563,8,10.644,8c-1.921,0-3.478,1.557-3.478,3.478c0,1.92,1.557,3.477,3.478,3.477c0.749,0,1.442-0.239,2.01-0.643 " +
                "c0.019,0.063,0.051,0.121,0.098,0.169l2.375,2.374c0.19,0.189,0.543,0.143,0.79-0.104S16.21,16.15,16.021,15.96z M10.644,13.69 " +
                "c-1.221,0-2.213-0.991-2.213-2.213c0-1.221,0.992-2.213,2.213-2.213c1.222,0,2.213,0.992,2.213,2.213 " +
                "C12.856,12.699,11.865,13.69,10.644,13.69z");
        svgIcon.setScaleX(1.3);
        svgIcon.setScaleY(1.3);
        
        Button searchBtn = new Button();
        searchBtn.setGraphic(svgIcon);
        //searchBtn.setPrefWidth(25);
        return searchBtn;
    }
}

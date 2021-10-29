package com.example.gav_fx.panes.toppane;

import com.example.gav_fx.core.WorkerController;
import com.example.gav_fx.core.AlgorithmImplementor;
import com.example.gav_fx.core.Tools;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Transform;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * This is the component in top panel, that imports compiled algorithm class/es.
 *  ___________________________hbox(this)_____________________________
 * |  ______combo box________________________________   ___button__  |
 * | |                                      ______  |  |          |  |
 * | | name of algorithm class...  dropdown \    /  |  |  refresh |  |
 * | |                            indicator  \ /    |  |folder btm|  |
 * | |______________________________________________|  |__________|  |
 * |_________________________________________________________________|
 */
public class ImportComponent extends HBox {
    
    final WorkerController workerController;
    
    // Name of class file (to be displayed in dropdown menu) -> Class File
    final Map<String, File> algorithms;
    final File algoFolder = new File("algos");
    
    ComboBox<String> dropdown;
    Button refreshBtn;
    
    public ImportComponent(WorkerController workerController) {
        this.workerController = workerController;
        this.algorithms = new HashMap<>();
        readAlgorithmFiles();
        init();
        
        this.getStyleClass().add("import-component");
        this.setSpacing(5);
        this.setPadding(new Insets(3, 3, 4, 3));
    }
    
    private void init() {
        dropdown = new ComboBox<>();
        dropdown.setItems(FXCollections.observableList(algorithms.keySet().stream().toList()));
        dropdown.getStyleClass().add("top-button");
        dropdown.getStyleClass().add("toolbar-button");
        dropdown.setOnAction(e -> {
            try {
                // TODO how to load outside .class files or load .java and compile them here?
                //URL filUrl = algorithms.get(dropdown.getSelectionModel().getSelectedItem()).toURI().toURL();
                //URL[] urls = new URL[]{filUrl};
                
                //// Create a new class loader with the directory
                //ClassLoader cl = new URLClassLoader(urls);
                
                //// Load in the class; MyClass.class should be located in
                //// the directory file:/c:/myclasses/com/mycompany
                //Class cls = cl.loadClass("com.mycompany.MyClass");
    
                URL dirUrl = algoFolder.toURI().toURL();
                System.out.println(dirUrl);
                URLClassLoader cl = new URLClassLoader(new URL[] {dirUrl}, AlgorithmImplementor.class.getClassLoader());  // 2
                Class loadedClass = cl.loadClass(dropdown.getSelectionModel().getSelectedItem().replace(".class", ""));
                Object obj =  loadedClass.getDeclaredConstructor().newInstance();
    
                System.out.println(obj);
                
                //Algorithm algo = (Algorithm) Class.forName("test.MyClass").getDeclaredConstructor().newInstance();
                //algorithmController.setAlgorithm(algo);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        // reload icon
        SVGPath svg = new SVGPath();
        svg.setContent("M12.5747152,11.8852806 C11.4741474,13.1817355 9.83247882,14.0044386 7.99865879,14.0044386 C5.03907292,14.0044386 2.57997332,11.8615894 " +
                "2.08820756,9.0427473 L3.94774327,9.10768372 C4.43372186,10.8898575 6.06393114,12.2000519 8.00015362,12.2000519 C9.30149237,12.2000519 10.4645985,11.6082097 " +
                "11.2349873,10.6790094 L9.05000019,8.71167959 L14.0431479,8.44999981 L14.3048222,13.4430431 L12.5747152,11.8852806 Z M3.42785637,4.11741586 C4.52839138,2.82452748 " +
                "6.16775464,2.00443857 7.99865879,2.00443857 C10.918604,2.00443857 13.3513802,4.09026967 13.8882946,6.8532307 L12.0226389,6.78808057 C11.5024872,5.05935553 " +
                "9.89838095,3.8000774 8.00015362,3.8000774 C6.69867367,3.8000774 5.53545628,4.39204806 4.76506921,5.32142241 L6.95482203,7.29304326 L1.96167436,7.55472304 " +
                "L1.70000005,2.56167973 L3.42785637,4.11741586 Z");
        svg.getTransforms().add(Transform.rotate(3, 8.002,8.004));
        svg.setFill(Tools.ICON_COLOR);
        svg.setScaleX(1.1);
        svg.setScaleY(1.1);
        refreshBtn = new Button();
        refreshBtn.getStyleClass().add("top-button");
        refreshBtn.setGraphic(svg);
        refreshBtn.setOnMouseClicked(e -> {
            readAlgorithmFiles();
            dropdown.setItems(FXCollections.observableList(algorithms.keySet().stream().toList()));
        });
        
        this.getChildren().addAll(dropdown, refreshBtn);
    }
    
    private void readAlgorithmFiles() {
        System.out.println("algo folder path: " + algoFolder.getAbsolutePath());
        File[] files = algoFolder.listFiles();
        
        if (files == null) return;
        
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                algorithms.put(file.getName(), file);
            }
        }
    }
}

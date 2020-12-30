package reminiscent;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.*;

public class MainUI extends Application {
    //1000 is the hard limit. Although I doubt anyone would add 1000 instructions.
    public static String[] listOfCommands = new String[1000];
    public static int[] percentages = new int[1000];
    public static boolean[] finished = new boolean[1000];
    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        MainUI.stage = stage;
        for (int i = 0, x = 0, y = 0; i < ConfigHandler.counter; i++) {
            if (i % 2 == 0) {
                percentages[x] = Integer.parseInt(ConfigHandler.lines[i]);
                x++;
            }
            else {
                listOfCommands[y] = ConfigHandler.lines[i];
                y++;
            }
        }

        double scaling;
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        scaling = gd.getDisplayMode().getHeight() / 1080f;
        double positionScaling = 1;

        if (scaling > positionScaling) {
            positionScaling = scaling * 14 - positionScaling * 16;
        } else if (positionScaling > scaling) {
            positionScaling = scaling * 14 - positionScaling * 16;
        } else {
            positionScaling = 0;
        }

        stage.setX(stage.getX()/2);
        stage.setY(stage.getY()/2);

        Text program = new Text();
        program.setFont(new Font(35*scaling));
        program.setX(10*scaling);
        program.setY(35*scaling);
        program.setFill(Color.WHITE);
        program.setText("reminiscent (click to hide)");

        program.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    stage.hide();
                }
            }
        });

        BorderPane border = new BorderPane();
        border.setPadding(new Insets(6, 30, 6, 30));

        Button exitButton = new Button();
        exitButton.setText("Exit");
        exitButton.setMaxWidth(Double.MAX_VALUE);

        exitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    System.exit(0);
                }
            }
        });


        Button addNew = new Button();
        addNew.setText("Add");
        addNew.setMaxWidth(Double.MAX_VALUE);

        Button removeItem = new Button();
        removeItem.setText("Remove");
        removeItem.setId("removeItem");
        removeItem.setMaxWidth(Double.MAX_VALUE);

        VBox vbButtons = new VBox();
        vbButtons.setSpacing(10 * scaling);
        vbButtons.setLayoutX(985*scaling + (positionScaling * 2.7));
        vbButtons.setLayoutY(370*scaling + (positionScaling * 2.7));
        vbButtons.getChildren().addAll(addNew, removeItem, exitButton);
        vbButtons.setScaleX(scaling);
        vbButtons.setScaleY(scaling);

        TableView<TableData> tv = new TableView<TableData>();
        TableColumn commands = new TableColumn("Commands");
        commands.setCellValueFactory(new PropertyValueFactory<TableData, String>("command"));
        TableColumn percentage = new TableColumn("Percentage");
        percentage.setCellValueFactory(new PropertyValueFactory<TableData, String>("percentage"));

        final ObservableList<TableData> data = FXCollections.observableArrayList();

        tv.getColumns().addAll(commands, percentage);
        commands.prefWidthProperty().bind(tv.prefWidthProperty().multiply(0.8));
        percentage.prefWidthProperty().bind(tv.prefWidthProperty().multiply(0.2));
        tv.setLayoutX(10*scaling);
        tv.setLayoutY(80*scaling);
        tv.setPrefWidth(900*scaling);
        tv.setPrefHeight(420*scaling);

        tv.setMaxSize(900*scaling,420*scaling);
        tv.setEditable(true);
        commands.setCellFactory(TextFieldTableCell.forTableColumn());
        percentage.setCellFactory(TextFieldTableCell.forTableColumn());

        commands.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TableData, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TableData, String> t) {
                        ((TableData) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setCommand(t.getNewValue());
                        try {
                            BufferedWriter clear = new BufferedWriter(new FileWriter(ConfigHandler.configFile));
                            clear.close();
                        } catch (IOException e) {
                            System.out.println("Unable to read config file. Settings were not saved.");
                        }
                        for (int i = 0; i < tv.getItems().size(); i++) {
                            listOfCommands[i] = commands.getCellObservableValue(i).getValue().toString();
                            finished[i] = false;
                            try {
                                BufferedWriter out = new BufferedWriter(new FileWriter(ConfigHandler.configFile, true));
                                out.append(Integer.toString(percentages[i]) + "\n");
                                out.append(listOfCommands[i] + "\n");
                                out.close();
                            } catch (IOException e) {
                                System.out.println("Unable to write to config file. Settings were not saved.");
                            }
                        }
                    }
                }
        );

        percentage.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TableData, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TableData, String> t) {
                        ((TableData) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setPercentage(t.getNewValue());
                        try {
                            BufferedWriter clear = new BufferedWriter(new FileWriter(ConfigHandler.configFile));
                            clear.close();
                        } catch (IOException e) {
                            System.out.println("Unable to read config file. Settings were not saved.");
                        }
                        for (int i = 0; i < tv.getItems().size(); i++) {
                            percentages[i] = Integer.parseInt(percentage.getCellObservableValue(i).getValue().toString());
                            finished[i] = false;
                            try {
                                BufferedWriter out = new BufferedWriter(new FileWriter(ConfigHandler.configFile, true));
                                out.append(Integer.toString(percentages[i]) + "\n");
                                out.append(listOfCommands[i] + "\n");
                                out.close();
                            } catch (IOException e) {
                                System.out.println("Unable to write to config file. Settings were not saved.");
                            }
                        }
                    }
                }
        );

        tv.setItems(data);

        commands.styleProperty().bind(Bindings.concat("-fx-font-size: " + 12*scaling + "px;"));
        percentage.styleProperty().bind(Bindings.concat("-fx-font-size: " + 12*scaling + "px;"));

        StringProperty style = new SimpleStringProperty();
        style.bind(Bindings.concat("-fx-cell-size: " + 30*scaling + "px;"));

        tv.setRowFactory(rowstyle -> {
            TableRow<TableData> row = new TableRow<>();
            row.styleProperty().bind(style);
            return row;
        });

        for (int i = 0; i < ConfigHandler.counter/2; i++) {
            tv.getItems().add(new TableData(listOfCommands[i], Integer.toString(percentages[i])));
        }

        addNew.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    tv.getItems().add(new TableData("new command", "100"));
                    listOfCommands = new String[1000];
                    percentages = new int[1000];
                    finished = new boolean[1000];

                    try {
                        BufferedWriter clear = new BufferedWriter(new FileWriter(ConfigHandler.configFile));
                        clear.close();
                    } catch (IOException e) {
                        System.out.println("Unable to read config file. Settings were not saved.");
                    }

                    for (int i = 0; i < tv.getItems().size(); i++) {
                        listOfCommands[i] = commands.getCellObservableValue(i).getValue().toString();
                        percentages[i] = Integer.parseInt(percentage.getCellObservableValue(i).getValue().toString());
                        finished[i] = false;
                        try {
                            BufferedWriter out = new BufferedWriter(new FileWriter(ConfigHandler.configFile, true));
                            out.append(Integer.toString(percentages[i]) + "\n");
                            out.append(listOfCommands[i] + "\n");
                            out.close();
                        } catch (IOException e) {
                            System.out.println("Unable to write to config file. Settings were not saved.");
                        }
                    }
                }
            }
        });

        removeItem.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    tv.getItems().remove(tv.getSelectionModel().getSelectedItem());
                    try {
                        BufferedWriter clear = new BufferedWriter(new FileWriter(ConfigHandler.configFile));
                        clear.close();
                    } catch (IOException e) {
                        System.out.println("Unable to read config file. Settings were not saved.");
                    }
                    for (int i = 0; i < tv.getItems().size(); i++) {
                        listOfCommands[i] = commands.getCellObservableValue(i).getValue().toString();
                        percentages[i] = Integer.parseInt(percentage.getCellObservableValue(i).getValue().toString());
                        try {
                            BufferedWriter out = new BufferedWriter(new FileWriter(ConfigHandler.configFile, true));
                            out.append(Integer.toString(percentages[i]) + "\n");
                            out.append(listOfCommands[i] + "\n");
                            out.close();
                        } catch (IOException e) {
                            System.out.println("Unable to write to config file. Settings were not saved.");
                        }
                    }
                }
            }
        });

        Group root = new Group(program, tv, vbButtons);
        Scene scene = new Scene(root, 1100*scaling, 550*scaling);

        scene.setFill(Color.rgb(44,44,44));
        stage.setTitle("reminiscent");
        stage.setScene(scene);

        stage.initStyle(StageStyle.UTILITY);
        stage.setMaxWidth(1100*scaling);
        stage.setMinWidth(1100*scaling);
        stage.setMinHeight(550*scaling);
        stage.setMaxHeight(550*scaling);

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        Platform.setImplicitExit(false);
    }
    public static void main(String args[]) throws InterruptedException, IOException {
        ConfigHandler.ConfigHandle();
        Tray.Main();
        BatteryInfo.Main(args);
        launch(args);
    }
}

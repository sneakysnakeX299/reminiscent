package reminiscent;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BatteryInfo implements Runnable {
    int acpiInfo = 0;

    public void run() {
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                while (true) {
                    Process acpi = Runtime.getRuntime().exec("acpi");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(acpi.getInputStream()));
                    String line = reader.readLine();
                    try {
                        Pattern pPattern = Pattern.compile("\\d*[0-9]%");
                        Matcher pMatcher = pPattern.matcher(line);
                        Pattern cPattern = Pattern.compile("Discharging");
                        Matcher cMatcher = cPattern.matcher(line);
                        if (cMatcher.find()) {
                            if (pMatcher.find()) {
                                //commands will run whenever a new item is added to the list
                                //and im lazy to fix this at the moment
                                //will fix when this becomes a huge issue or something
                                for (int i = 0; i < MainUI.percentages.length; i++) {
                                    try {
                                        if (pMatcher.group(0).equals(MainUI.percentages[i] + "%") && !MainUI.finished[i]) {
                                            String[] command = {"/bin/sh", "-c", MainUI.listOfCommands[i]};
                                            Runtime.getRuntime().exec(command);
                                            MainUI.finished[i] = true;
                                        }
                                    } catch (IOException e) {
                                        System.out.println("Command not found.");
                                    }
                                }
                            }
                        }
                        else {
                            for(int i = 0; i < MainUI.percentages.length; i++) {
                                MainUI.finished[i] = false;
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("Unable to get battery information!");
                        if (acpiInfo == 0) {
                            Platform.runLater(new Runnable() {
                                @Override public void run() {
                                    double scaling;
                                    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                                    scaling = gd.getDisplayMode().getHeight() / 1080f;
                                    double positionScaling = 1;
                                    if (scaling > positionScaling) {
                                        positionScaling = scaling * 14 - positionScaling * 16;
                                    } else if (positionScaling > scaling) {
                                        positionScaling = scaling * 14 - positionScaling * 16;
                                    } else {
                                        positionScaling = -1.6;
                                    }
                                    Text unableToGetInfo = new Text("Unable to get battery information from acpi!");
                                    unableToGetInfo.setFont(new Font(15*scaling));
                                    unableToGetInfo.setX(20*scaling);
                                    unableToGetInfo.setY(18*scaling);
                                    unableToGetInfo.setFill(Color.WHITE);
                                    Stage noinfo = new Stage();
                                    noinfo.setAlwaysOnTop(true);
                                    Button ok = new Button();
                                    ok.setLayoutX(415*scaling + (positionScaling * 2.7));
                                    if (scaling < 1) {
                                        ok.setLayoutY(55*scaling + positionScaling * 0.5);
                                    }
                                    else if (scaling > 1) {
                                        ok.setLayoutY(55*scaling + positionScaling * 2);
                                    }
                                    else {
                                        ok.setLayoutY(55*scaling + positionScaling * 2);
                                    }
                                    ok.setScaleX(scaling);
                                    ok.setScaleY(scaling);
                                    ok.setText("OK");
                                    ok.setId("okButton");
                                    ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent mouseEvent) {
                                            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                                                noinfo.close();
                                            }
                                        }
                                    });
                                    Group warnGroup = new Group(unableToGetInfo, ok);
                                    Scene warn = new Scene(warnGroup, 500*scaling, 100*scaling);
                                    noinfo.setResizable(false);
                                    noinfo.initStyle(StageStyle.UTILITY);
                                    noinfo.setMaxWidth(500*scaling);
                                    noinfo.setMaxHeight(100*scaling);
                                    noinfo.setMinWidth(500*scaling);
                                    noinfo.setMinHeight(100*scaling);
                                    noinfo.setX(noinfo.getX()/2);
                                    noinfo.setY(noinfo.getY()/2);
                                    noinfo.setScene(warn);
                                    warn.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                                    warn.setFill(Color.rgb(44,44,44));
                                    noinfo.show();
                                    //set the proper window size
                                    noinfo.setMaxWidth(noinfo.getWidth());
                                    noinfo.setMaxHeight(noinfo.getHeight());
                                    noinfo.setMinWidth(noinfo.getWidth());
                                    noinfo.setMinHeight(noinfo.getHeight());
                                    noinfo.setWidth(noinfo.getWidth());
                                    noinfo.setHeight(noinfo.getHeight());
                                    acpiInfo = 1;
                                }
                            });
                        }
                        acpiInfo = 1;
                    }
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                if (acpiInfo == 0) {
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            double scaling;
                            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                            scaling = gd.getDisplayMode().getHeight() / 1080f;
                            double positionScaling = 1;
                            if (scaling > positionScaling) {
                                positionScaling = scaling * 14 - positionScaling * 16;
                            } else if (positionScaling > scaling) {
                                positionScaling = scaling * 14 - positionScaling * 16;
                            } else {
                                positionScaling = -1.6;
                            }
                            Text acpiIsNotInstalled = new Text("acpi is not installed! Install it through your package manager!");
                            acpiIsNotInstalled.setFont(new Font(15*scaling));
                            acpiIsNotInstalled.setX(20*scaling);
                            acpiIsNotInstalled.setY(18*scaling);
                            acpiIsNotInstalled.setFill(Color.WHITE);
                            Stage noacpi = new Stage();
                            noacpi.setAlwaysOnTop(true);
                            Button ok = new Button();
                            ok.setLayoutX(415*scaling + (positionScaling * 2.7));
                            if (scaling < 1) {
                                ok.setLayoutY(55*scaling + positionScaling * 0.5);
                            }
                            else if (scaling > 1) {
                                ok.setLayoutY(55*scaling + positionScaling * 2);
                            }
                            else {
                                ok.setLayoutY(55*scaling + positionScaling * 2);
                            }
                            ok.setScaleX(scaling);
                            ok.setScaleY(scaling);
                            ok.setText("OK");
                            ok.setId("okButton");
                            ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                                        noacpi.close();
                                    }
                                }
                            });
                            Group warnGroup = new Group(acpiIsNotInstalled, ok);
                            Scene warn = new Scene(warnGroup, 500*scaling, 100*scaling);
                            noacpi.setResizable(false);
                            noacpi.initStyle(StageStyle.UTILITY);
                            noacpi.setMaxWidth(500*scaling);
                            noacpi.setMaxHeight(100*scaling);
                            noacpi.setMinWidth(500*scaling);
                            noacpi.setMinHeight(100*scaling);
                            noacpi.setX(noacpi.getX()/2);
                            noacpi.setY(noacpi.getY()/2);
                            noacpi.setScene(warn);
                            warn.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                            warn.setFill(Color.rgb(44,44,44));
                            noacpi.show();
                            //again, set the proper window size
                            noacpi.setMaxWidth(noacpi.getWidth());
                            noacpi.setMaxHeight(noacpi.getHeight());
                            noacpi.setMinWidth(noacpi.getWidth());
                            noacpi.setMinHeight(noacpi.getHeight());
                            noacpi.setWidth(noacpi.getWidth());
                            noacpi.setHeight(noacpi.getHeight());
                            acpiInfo = 1;
                        }
                    });
                }
                System.out.println("acpi is not installed!");
                acpiInfo = 1;
            }
        } while(true);
    }
    public static void Main(String[] args) throws InterruptedException {
        Thread batteryThread = new Thread(new BatteryInfo());
        batteryThread.start();
    }
}

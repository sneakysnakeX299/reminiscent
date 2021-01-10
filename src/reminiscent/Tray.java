package reminiscent;

import java.awt.*;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javafx.application.Platform;

public class Tray implements Runnable {
    public void run() {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported.");
            System.exit(1);
        }
        ClassLoader cls = this.getClass().getClassLoader();
        URL url = cls.getResource("reminiscent/icon.png");
        TrayIcon icon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(url));
        icon.setImageAutoSize(true);
        SystemTray tray = SystemTray.getSystemTray();
        icon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        MainUI.stage.show();
                    }
                });
            }
        });
        try {
            tray.add(icon);
        } catch (Exception e) {
            System.out.println("Tray icon could not be added.");
        }
    }
    public static void Main() {
        Thread systray = new Thread(new Tray());
        systray.start();
    }
}

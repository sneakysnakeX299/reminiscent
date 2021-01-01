# reminiscent
## Customise battery events like never before

### Installation
Just download the jar and run with: "java -jar reminiscent.jar". If that doesn't work: "java --module-path=/path/to/java/libfiles --add-modules=ALL-MODULE-PATH -jar reminiscent.jar".

Note: OpenJDK 14 is recommended. See troubleshooting for more.

### Autostarting
This is a bit of a pain on Linux so add it to startup programs the way you want. Sorry.

### Sounds
Install sox and use the play command. You'll see some examples down below.

### Troubleshooting
- System tray issue on GNOME DE(e.g. Ubuntu): https://extensions.gnome.org/extension/1031/topicons/
- IO error when running as root: Create .config/reminiscent/reminiscent.conf and make it readable and writable.
- Tray icon not transparent: This is a Java AWT issue. I'm testing a workaround at the moment and this should fix the system tray issue on GNOME as well.
- Graphics device initalization error: Download this archive file: https://0x0.st/-rkB.7z and extract it. Run the following command: "java -p /path/to/archive/lib --add modules javafx.controls,javafx.fxml -jar reminiscent.jar".
- jrt.fs issue: Go to /path/to/java/libfiles(I tested the program in openjdk, and by default the location is "/usr/lib/jvm/java-14-openjdk/lib" on Arch Linux and possibly Manjaro too), and move to .. or delete jrt-fs.jar.

### Examples
https://sneakysnakex299.github.io/assets/2020-12-30%2011-31-42.mp4

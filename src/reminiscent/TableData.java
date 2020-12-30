package reminiscent;

import javafx.beans.property.*;

public class TableData {
    private final SimpleStringProperty command;
    private final SimpleStringProperty percentage;

    TableData(String cmd, String prcntg) {
        this.command = new SimpleStringProperty(cmd);
        this.percentage = new SimpleStringProperty(prcntg);
    }
    public String getCommand() {
        return command.get();
    }
    public String getPercentage() {
        return percentage.get();
    }
    public void setCommand(String cmd) {
        command.set(cmd);
    }
    public void setPercentage(String prcntg) {
        percentage.set(prcntg);
    }
}

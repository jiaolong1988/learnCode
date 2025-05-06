package util.seqExecFunction.service;

import java.io.File;

/**
 * @author jiaolong
 * @date 2025/01/07 9:45
 **/
public class ExecParameter {
    private String worktime;
    private File importFile;

    public File getImportFile() {
        return importFile;
    }

    public void setImportFile(File importFile) {
        this.importFile = importFile;
    }

    public String getWorktime() {
        return worktime;
    }

    public void setWorktime(String worktime) {
        this.worktime = worktime;
    }
}

package util.seqExecFunction.service;

import java.io.File;

/**
 * @author jiaolong
 * @date 2025/01/07 9:45
 **/
public class ExecParameter {
    private File importFile;
    private boolean isDelStatus = true;

    public File getImportFile() {
        return importFile;
    }

    public void setImportFile(File importFile) {
        this.importFile = importFile;
    }

    public boolean isDelStatus() {
        return isDelStatus;
    }

    public void setDelStatus(boolean delStatus) {
        isDelStatus = delStatus;
    }
}

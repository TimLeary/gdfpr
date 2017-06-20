package pkginterface;

import java.io.Serializable;

public class DepartmentWorker implements Serializable {
    private int workerId;
    private String departmentName, workerName;

    public DepartmentWorker(int workerId, String departmentName, String workerName) {
        this.departmentName = departmentName;
        this.workerName = workerName;
        this.workerId = workerId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public String getDepartmentName() {
        return departmentName;
    }
    
    @Override
    public String toString() {
        return this.workerName;
    }
}

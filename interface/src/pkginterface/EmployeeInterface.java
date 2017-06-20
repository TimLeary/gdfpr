package pkginterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.tree.DefaultTreeModel;

public interface EmployeeInterface extends Remote {
    DefaultTreeModel loadTree() throws RemoteException;
    ArrayList<DepartmentWorker> createList() throws RemoteException;
    int maxSalaryQuery() throws RemoteException;
    int empIdQuery() throws RemoteException;
    boolean setNewEmployee(NewEmployee newemp) throws RemoteException;

    ModifySalaryEmployee loadSalary(ModifySalaryEmployee empToUpdate) throws RemoteException;
    void updateSalary(ModifySalaryEmployee empToUpdate, double newSalary) throws RemoteException;
}

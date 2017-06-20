package pkginterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface DepartmentsInterface extends Remote
{
  ArrayList<Department> loadDepartments() throws RemoteException;

}

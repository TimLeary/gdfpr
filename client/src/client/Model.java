package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import pkginterface.Department;
import pkginterface.DepartmentWorker;
import pkginterface.EmployeeInterface;
import pkginterface.DepartmentsInterface;
import pkginterface.NewEmployee;

public class Model {
  private static ArrayList<DepartmentWorker> listDeptWorker;
  private static ArrayList<Department> listDepts;

  public static ArrayList<DepartmentWorker> loadListDeptWorker() {
    ArrayList<DepartmentWorker> workers = new ArrayList<>();

    try {
      EmployeeInterface employee = (EmployeeInterface) Naming.lookup("rmi://localhost:1099/employee");
      workers = employee.createList();
    } catch (MalformedURLException | NotBoundException | RemoteException ex) {
      ex.printStackTrace();
    }
    return workers;
  }

  public static ArrayList<Department> loadListDepts() {
    ArrayList<Department> workers = new ArrayList<>();

    try {
      DepartmentsInterface departments = (DepartmentsInterface) Naming.lookup("rmi://localhost:1099/departments");
      workers = departments.loadDepartments();
    } catch (MalformedURLException | NotBoundException | RemoteException ex) {
      ex.printStackTrace();
    }
    return workers;
  }

  public static DefaultTreeModel getEmployeeTreeModel() {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("None");
    DefaultTreeModel dlm = new DefaultTreeModel(root);
    try {
      EmployeeInterface employee = (EmployeeInterface) Naming.lookup("rmi://localhost:1099/employee");
      dlm = employee.loadTree();
    } catch (MalformedURLException | NotBoundException | RemoteException ex) {
      ex.printStackTrace();
    }

    return dlm;
  }

  public DefaultTreeModel treeModel(DepartmentWorker w) {
    listDeptWorker = loadListDeptWorker();
    int i = 0;

    // insertion
    while(!listDeptWorker.get(i).getDepartmentName().equals(w.getDepartmentName()) && i!= listDeptWorker.size()) //getting to the dept
      i++;

    while(listDeptWorker.get(i).getDepartmentName().equals(w.getDepartmentName()) && i!= listDeptWorker.size() && listDeptWorker.get(i).getWorkerName().compareTo(w.getWorkerName())<0)
      i++;

    listDeptWorker.add(i,w);

    DefaultMutableTreeNode root = new DefaultMutableTreeNode("None");
    DefaultTreeModel dtm = new DefaultTreeModel(root);

    int j = 0;
    while (j < listDeptWorker.size()) {
      String actDepartment = listDeptWorker.get(j).getDepartmentName();
      DefaultMutableTreeNode treeDepartment = new DefaultMutableTreeNode(actDepartment);
      while (j < listDeptWorker.size() && listDeptWorker.get(j).getDepartmentName().equals(actDepartment)) {
        treeDepartment.add(new DefaultMutableTreeNode(
                listDeptWorker.get(j)
        ));
        j++;
      }
      root.add(treeDepartment);
    }

    return dtm;
  }
}

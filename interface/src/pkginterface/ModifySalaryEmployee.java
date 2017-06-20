package pkginterface;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifySalaryEmployee implements Serializable
{
  private DepartmentWorker worker;
  private double salary = 0;
  private double maxSalary = 0;
  private double minSalary = 0;

  public ModifySalaryEmployee(DepartmentWorker worker)
  {
    this.worker = worker;
    loadSalary();
  }

  // call it only from client
  private void loadSalary() {
    try {
      EmployeeInterface employee = (EmployeeInterface) Naming.lookup("rmi://localhost:1099/employee");
      ModifySalaryEmployee temp = employee.loadSalary(this);
      this.salary = temp.getSalary();
      this.maxSalary = temp.getMaxSalary();
      this.minSalary = temp.getMinSalary();
    } catch (MalformedURLException | NotBoundException | RemoteException ex) {
      ex.printStackTrace();
    }
  }

  public double getMaxSalary() {
    return maxSalary;
  }

  public void setMaxSalary(double maxSalary)
  {
    this.maxSalary = maxSalary;
  }

  public double getMinSalary() {
    return minSalary;
  }

  public void setMinSalary(double minSalary)
  {
    this.minSalary = minSalary;
  }

  public double getSalary() {
    return salary;
  }

  public void setSalary(double salary)
  {
    this.salary = salary;
  }

  public int getWorkerId()
  {
    return worker.getWorkerId();
  }

  // call it only from client
  public void updateSalary(double newSalary) {
    try {
      EmployeeInterface employee = (EmployeeInterface) Naming.lookup("rmi://localhost:1099/employee");
      employee.updateSalary(this,newSalary);
    } catch (MalformedURLException | NotBoundException | RemoteException ex) {
      ex.printStackTrace();
    }
    loadSalary();
  }
}
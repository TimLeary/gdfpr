package pkginterface;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 *
 * @author MAGA
 */
public class NewEmployee implements Serializable
{

  private int empId;
  private String firstName;
  private String lastName;
  private String email;
  private String jobId;
  private String phoneNumber;
  private double salary;
  private int managerId;
  private int depId;
  private int minSalary = 0;
  private int maxSalary;

  public NewEmployee() {
  }

  public int getEmpId()
  {
    return empId;
  }

  // call it only from client
  public void setEmpId()
  {
    try {
      EmployeeInterface employee = (EmployeeInterface) Naming.lookup("rmi://localhost:1099/employee");
    } catch (MalformedURLException | NotBoundException | RemoteException ex) {
      ex.printStackTrace();
    }
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public double getSalary() {
    return salary;
  }

  public void setSalary(double salary) {
    this.salary = salary;
  }

  public int getManagerId() {
    return managerId;
  }

  public void setManagerId(int managerId) {
    this.managerId = managerId;
  }

  public int getDepId() {
    return depId;
  }

  public void setDepId(int depId) {
    this.depId = depId;
  }

  public int getMaxSalary()
  {
    return maxSalary;
  }

  // call it only from client
  public void setMaxSalary()
  {
    try {
      EmployeeInterface employee = (EmployeeInterface) Naming.lookup("rmi://localhost:1099/employee");
      this.maxSalary = employee.maxSalaryQuery();
    } catch (MalformedURLException | NotBoundException | RemoteException ex) {
      ex.printStackTrace();
    }
  }

  public int getMinSalary()
  {
    return minSalary;
  }

  // call it only from client
  public boolean save()
  {
    try {
      EmployeeInterface employee = (EmployeeInterface) Naming.lookup("rmi://localhost:1099/employee");
      employee.setNewEmployee(this);
      return true;
    } catch (MalformedURLException | NotBoundException | RemoteException ex) {
      ex.printStackTrace();
      return false;
    }
  }

    @Override
    public String toString() {
        return "{\n" +
            "\t" +"name: " + firstName + ", " + lastName + "\n" +
            "\t" +"email: " + email + "\n" +
            "\t" +"jobId: " + jobId + "\n" +
            "\t" +"phoneNumber: " + phoneNumber + "\n" +
            "\t" +"salary: " + salary + "\n" +
            "\t" +"managerId: " + managerId + "\n" +
            "\t" +"depId: " + depId +
        "\n}";
    }
  
  
}

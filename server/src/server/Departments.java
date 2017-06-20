package server;

import pkginterface.DepartmentsInterface;
import pkginterface.Job;
import pkginterface.Department;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class Departments extends UnicastRemoteObject implements DepartmentsInterface, Data
{
  public Departments() throws RemoteException {};

@Override
  public ArrayList<Department> loadDepartments() throws RemoteException {
    ArrayList<Department> list = new ArrayList<>();
    Model.openConnection();
    try {
      ResultSet result = Model.connection.createStatement().executeQuery(SQL_LOAD_DEPARTMENTS);
      while (result.next()) {
        int id = result.getInt("depId");
        String name = result.getString("depName");
        int managerId = result.getInt("manId");
        Department dept= new Department(id, name, managerId);
        System.out.println(dept);
        list.add(dept);
      }

      for (int i = 0; i<list.size(); i++)
      {
        Vector<Job> jList = new Vector();
          PreparedStatement ps = Model.connection.prepareStatement(SQL_GET_JOBS_BY_DEPARTMENT);
          ps.setInt(1, list.get(i).getId());
          result = ps.executeQuery();

          while (result.next()) {
            String id = result.getString("jobId");
            String title = result.getString("jobTitle");
            Job job = new Job(id, title);
            jList.add(job);
          }

          list.get(i).setJobList(jList);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    Model.closeConnection();

    return list;
  }
}

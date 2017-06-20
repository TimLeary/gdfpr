package pkginterface;

import java.io.Serializable;
import java.util.Vector;

public class Department implements Serializable
{
  private final int id;
  private final String name;
  private final int managerId;
  private Vector<Job> jobList;

  public Department(int id, String name, int managerId)
  {
    this.id = id;
    this.name = name;
    this.managerId = managerId;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getManagerId() {
    return managerId;
  }

  @Override
  public String toString() {
    return this.getName();
  }

  public Vector<Job> getJobList() {
    return jobList;
  }

  public void setJobList(Vector<Job> jList) {
    this.jobList = jList;
  }
}
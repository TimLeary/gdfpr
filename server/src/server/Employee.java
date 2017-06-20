package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import pkginterface.EmployeeInterface;
import pkginterface.DepartmentWorker;
import pkginterface.ModifySalaryEmployee;
import pkginterface.NewEmployee;


public class Employee extends UnicastRemoteObject implements EmployeeInterface, Data {

    public Employee() throws RemoteException {
    };

    private String rootname = "Company";

    @Override
    public DefaultTreeModel loadTree() throws RemoteException {
        ArrayList<DepartmentWorker> list = createList();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootname);
        DefaultTreeModel dtm = new DefaultTreeModel(root);

        int i = 0;
        while (i < list.size()) {
            String actDepartment = list.get(i).getDepartmentName();
            DefaultMutableTreeNode treeDepartment = new DefaultMutableTreeNode(actDepartment);
            while (i < list.size() && list.get(i).getDepartmentName().equals(actDepartment)) {
                treeDepartment.add(new DefaultMutableTreeNode(
                        list.get(i)
                ));
                i++;
            }
            root.add(treeDepartment);
        }
        return dtm;
    }

    @Override
    public ArrayList<DepartmentWorker> createList() throws RemoteException {
        Model.openConnection();
        ArrayList<DepartmentWorker> list = new ArrayList<>();
        try {
            ResultSet result = Model.connection.createStatement().executeQuery(SQL_DEPARTMENT_WORKERS);

            while (result.next()) {
                Integer workerId = result.getInt("empId");
                String department = result.getString("depName");
                String worker = result.getString("empName");
                DepartmentWorker dw = new DepartmentWorker(workerId, department, worker);
                list.add(dw);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Model.closeConnection();
        return list;
    }

    @Override
    public int maxSalaryQuery() throws RemoteException
    {
        double actMaxSalary = 0;
        double sumSalary = 1;
        int numEmployee = 0;

        Model.openConnection();
        try {
            ResultSet result = Model.connection.createStatement().executeQuery(SUM_SALARY_DATAS);
            while (result.next()) {
                sumSalary = result.getDouble("sumSalary");
                numEmployee = result.getInt("numEmployee");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Model.closeConnection();

        actMaxSalary = sumSalary / numEmployee;
        return (int)actMaxSalary;
    }

    @Override
    public int empIdQuery() throws RemoteException
    {
        int empId=0;
        Model.openConnection();
        try {
            PreparedStatement ps = Model.connection.prepareStatement(CURRENT_EMPLOYEE_ID);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                empId = result.getInt("NEXTVAL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally
        {
            Model.closeConnection();
        }
        return empId;
    }

    public boolean setNewEmployee(NewEmployee newEmployee) throws RemoteException {

        Model.openConnection();

        try {
            PreparedStatement ps = Model.connection.prepareStatement(CREATE_EMPLOYEE_NORMAL);
            ps.setInt(1, newEmployee.getEmpId());
            ps.setString(2, newEmployee.getFirstName());
            ps.setString(3, newEmployee.getLastName());
            ps.setString(4, newEmployee.getEmail());
            ps.setString(5, newEmployee.getJobId());
            ps.setString(6, newEmployee.getPhoneNumber());
            ps.setDouble(7, newEmployee.getSalary());
            ps.setInt(8, newEmployee.getManagerId());
            ps.setInt(9, newEmployee.getDepId());
            ps.executeQuery();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            Model.closeConnection();
        }
    }

    @Override
    public ModifySalaryEmployee loadSalary(ModifySalaryEmployee empToUpdate) throws RemoteException
    {
        Model.openConnection();
        try {
            PreparedStatement ps = Model.connection.prepareStatement(SALARY_AND_MAX_SALARY_BY_WORKER_ID);
            ps.setInt(1, empToUpdate.getWorkerId());
            ps.setInt(2, empToUpdate.getWorkerId());
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                empToUpdate.setSalary(result.getDouble("salary"));
                empToUpdate.setMaxSalary(result.getDouble("maxSalary"));
                empToUpdate.setMinSalary(empToUpdate.getSalary() * 0.95);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Model.closeConnection();

        return empToUpdate;
    }

    @Override
    public void updateSalary(ModifySalaryEmployee empToUpdate, double newSalary) throws RemoteException
    {
        Model.openConnection();
        try {
            PreparedStatement ps = Model.connection.prepareStatement(UPDATE_SALARY_BY_WORKER_ID);
            ps.setDouble(1, newSalary);
            ps.setInt(2, empToUpdate.getWorkerId());
            ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Model.closeConnection();
    }
}

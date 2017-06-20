package server;

/**
 * 
 * [mysqld]
 * lower_case_table_names = 1
 */

public interface Data {
    String DRIVER = "com.mysql.jdbc.Driver";
    String URL = "jdbc:mysql://localhost:3306/hr?autoReconnect=true&useSSL=false";
    String USERNAME = "root";
    String PASSWORD = "a";
    String SQL_DEPARTMENTS = "SELECT DISTINCT d.department_name AS depName \n" +
        "FROM departments d, employees e\n" +
        "WHERE d.department_id = e.department_id\n" +
        "GROUP BY d.department_name";
    
    String SQL_DEPARTMENT_WORKERS = "SELECT d.department_name AS depName, " +
        "concat(first_name,' ',e.last_name) AS empName, " +
        "e.employee_id AS empId " +
        "FROM departments d, employees e " +
        "WHERE d.department_id = e.department_id " +
        "ORDER BY depName ASC, empName ASC";
    
    String SQL_WORKER_FROM_DEPARTMENT = "SELECT concat(first_name,' ',e.last_name) AS empName \n" +
        "FROM departments d, employees e\n" +
        "WHERE d.department_id = e.department_id\n" +
        "AND d.department_name=?\n" +
        "ORDER BY empName ASC";
    
    String SALARY_AND_MAX_SALARY_BY_WORKER_ID = "SELECT (b.salary * 0.9) AS maxSalary, w.salary as salary\n" +
        "FROM employees w\n" +
        "INNER JOIN employees b ON w.manager_id = b.employee_id\n" +
        "WHERE w.employee_id =?\n" +
        "UNION\n" +
        "SELECT (SELECT SUM(salary) FROM employees) * 0.05 AS maxSalary, w.salary as salary\n" +
        "FROM employees w\n" +
        "WHERE w.manager_id IS NULL\n" +
        "AND w.employee_id =?";
    
    String UPDATE_SALARY_BY_WORKER_ID = "UPDATE employees e SET e.salary=? WHERE e.employee_id=?";
    
    String CREATE_EMPLOYEE_MIN = "INSERT INTO employees (" +
        "  first_name," +
        "  last_name," +
        "  email," +
        "  hire_date," +
        "  job_id" +
        ") VALUES ( " +
        "    '?', " +
        "    '?', " +
        "    '?', " +
        "    now(), " +
        "    '?' " +
        ")";
    
    String CREATE_EMPLOYEE_NORMAL = "INSERT INTO employees (" +
        "  first_name," +
        "  last_name," +
        "  email," +
        "  hire_date," +
        "  job_id," +
        "  phone_number," +
        "  salary," +
        "  manager_id," +
        "  department_id " +
        ") VALUES (" +
        "    ?," +
        "    ?," +
        "    ?," +
        "    now()," +
        "    ?," +
        "    ?," +
        "    ?," +
        "    ?," +
        "    ?" +
        ")";

    String CURRENT_EMPLOYEE_ID =
            "SELECT EMPLOYEES_SEQ.nextval\n"+
                    "FROM DUAL";
    
    String SUM_SALARY_DATAS = "SELECT sum(salary) as sumSalary, count(employee_id) as numEmployee FROM employees";
    
    String UPDATE_ALL_EMPLOYEE_BY_PERCENTAGE = "UPDATE employees e " +
        "SET e.salary=(" +
        "  SELECT (s.salary * ?)" +
        "  FROM employees s" +
        "  WHERE e.employee_id = s.employee_id)";
    
    String SQL_GET_JOBS_BY_DEPARTMENT = "SELECT DISTINCT j.job_id as jobId, j.job_title as jobTitle " +
        "FROM employees e " +
        "  INNER JOIN departments d ON e.department_id = d.department_id " +
        "  INNER JOIN jobs j ON e.job_id = j.job_id " +
        "WHERE d.department_id = ? "+
            "ORDER BY jobTitle ASC";
    
    String SQL_LOAD_DEPARTMENTS = "SELECT department_id as depId, department_name as depName, manager_id as manId FROM departments ORDER BY depName";
}

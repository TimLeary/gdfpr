package server;

public interface Data {
    String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    String DRIVER = "oracle.jdbc.driver.OracleDriver";
    String USERNAME = "HR";
    String PASSWORD = "hr";
    String SQL_DEPARTMENTS = "SELECT DISTINCT D.DEPARTMENT_NAME AS depName " +
        "FROM DEPARTMENTS D, EMPLOYEES E " +
        "WHERE D.DEPARTMENT_ID = E.DEPARTMENT_ID " +
        "GROUP BY D.DEPARTMENT_NAME";
    
    String SQL_DEPARTMENT_WORKERS = "SELECT D.DEPARTMENT_NAME AS depName, " +
        "E.FIRST_NAME || ' '  || E.LAST_NAME AS empName, " +
        "E.EMPLOYEE_ID AS empId " +
        "FROM DEPARTMENTS D, EMPLOYEES E " +
        "WHERE D.DEPARTMENT_ID = E.DEPARTMENT_ID " +
        "ORDER BY depName, empName";
    
    String SQL_WORKER_FROM_DEPARTMENT = "SELECT E.FIRST_NAME || ' ' || E.LAST_NAME AS EMPNAME " +
        "FROM DEPARTMENTS D, EMPLOYEES E WHERE E.DEPARTMENT_ID=D.DEPARTMENT_ID " +
        "AND D.DEPARTMENT_NAME=? ORDER BY EMPNAME";
    
    String SALARY_AND_MAX_SALARY_BY_WORKER_ID = "SELECT (B.SALARY * 0.9) AS maxSalary, W.SALARY as salary \n" +
        "FROM EMPLOYEES W \n" +
        "INNER JOIN EMPLOYEES B ON W.MANAGER_ID = B.EMPLOYEE_ID \n" +
        "WHERE W.EMPLOYEE_ID =? \n" +
        "UNION \n" +
        "SELECT (SELECT SUM(SALARY) FROM EMPLOYEES) * 0.05 AS maxSalary, W.SALARY as salary \n" +
        "FROM EMPLOYEES W \n" +
        "WHERE W.MANAGER_ID IS NULL \n" +
        "AND W.EMPLOYEE_ID =? ";
    
    String UPDATE_SALARY_BY_WORKER_ID = "UPDATE EMPLOYEES E SET E.SALARY=? WHERE E.EMPLOYEE_ID=?";
    
    String CREATE_EMPLOYEE_MIN = "INSERT INTO EMPLOYEES (" +
        "  EMPLOYEE_ID," +
        "  FIRST_NAME," +
        "  LAST_NAME," +
        "  EMAIL," +
        "  HIRE_DATE," +
        "  JOB_ID" +
        ") VALUES ( " +
        "    (SELECT MAX(EMPLOYEE_ID)+1 FROM EMPLOYEES), " +
        "    '?', " +
        "    '?', " +
        "    '?', " +
        "    SYSDATE, " +
        "    '?' " +
        ")";
    
    String CREATE_EMPLOYEE_NORMAL = "INSERT INTO EMPLOYEES (" +
        "  EMPLOYEE_ID," +
        "  FIRST_NAME," +
        "  LAST_NAME," +
        "  EMAIL," +
        "  HIRE_DATE," +
        "  JOB_ID," +
        "  PHONE_NUMBER," +
        "  SALARY," +
        "  MANAGER_ID," +
        "  DEPARTMENT_ID " +
        ") VALUES (" +
        "    ?," +
        "    ?," +
        "    ?," +
        "    ?," +
        "    SYSDATE," +
        "    ?," +
        "    ?," +
        "    ?," +
        "    ?," +
        "    ?" +
        ")";

    String CURRENT_EMPLOYEE_ID =
            "SELECT EMPLOYEES_SEQ.nextval\n"+
                    "FROM DUAL";
    
    String SUM_SALARY_DATAS = "SELECT sum(SALARY) as sumSalary, count(EMPLOYEE_ID) as numEmployee FROM EMPLOYEES";
    
    String UPDATE_ALL_EMPLOYEE_BY_PERCENTAGE = "UPDATE EMPLOYEES E " +
        "SET E.SALARY=(" +
        "  SELECT (S.SALARY * ?)" +
        "  FROM EMPLOYEES S" +
        "  WHERE E.EMPLOYEE_ID = S.EMPLOYEE_ID)";
    
    String SQL_GET_JOBS_BY_DEPARTMENT = "SELECT DISTINCT J.JOB_ID as jobId, J.JOB_TITLE as jobTitle " +
        "FROM EMPLOYEES E " +
        "  INNER JOIN DEPARTMENTS D ON E.DEPARTMENT_ID = D.DEPARTMENT_ID " +
        "  INNER JOIN JOBS J ON E.JOB_ID = J.JOB_ID " +
        "WHERE D.DEPARTMENT_ID = ? "+
            "ORDER BY jobTitle";
    
    String SQL_LOAD_DEPARTMENTS = "SELECT DEPARTMENT_ID as depId, DEPARTMENT_NAME as depName, MANAGER_ID as manId FROM DEPARTMENTS ORDER BY depName";
}

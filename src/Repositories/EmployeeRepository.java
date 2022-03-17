package Repositories;

import Models.Conexion;
import java.sql.Connection;
import java.sql.ResultSet;
import Models.Employee;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {

    Connection cnn;
    ResultSet rs;
    CallableStatement cs;

    public EmployeeRepository() {
    }

    public Employee Login(String username, String passwd) throws SQLException {
        Employee emp = null;
        try {
            Conexion cn = Conexion.getInstance();
            cnn = cn.getConnection();
            String sql = "{CALL SP_AUTHENTICATE_EMPLOYEE(?,?)}";
            cs = cnn.prepareCall(sql);
            cs.setString(1, username);
            cs.setString(2, passwd);
            rs = cs.executeQuery();
            if (rs.next()) {
                emp = new Employee();
                emp.setEmployee_id(rs.getString("employee_id"));
                emp.setName(rs.getString("name"));
                emp.setUsername(rs.getString("username"));
                emp.setRole(rs.getString("role"));
                emp.setStatus(rs.getBoolean("status"));
            }
        } catch (SQLException e) {
            throw e;
        }
        return emp;
    }

    public int CreateEmployee(Employee employee) throws SQLException {
        try {
            Conexion cn = Conexion.getInstance();
            cnn = cn.getConnection();
            String sql = "{CALL SP_CREATE_EMPLOYEE(?,?,?,?)}";
            cs = cnn.prepareCall(sql);
            cs.setString(1, employee.getName());
            cs.setString(2, employee.getUsername());
            cs.setString(3, employee.getPasswd());
            cs.setString(4, employee.getRole());
            return cs.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
    
    public int UpdateEmployee(Employee emp) throws SQLException{
        try {
            Conexion cn = Conexion.getInstance();
            cnn = cn.getConnection();
            String sql = "{CALL SP_UPDATE_EMPLOYEE(?,?,?,?)}";
            cs = cnn.prepareCall(sql);
            cs.setString(1, emp.getEmployee_id());
            cs.setString(2, emp.getName());
            cs.setString(3, emp.getUsername());
            cs.setString(4, emp.getRole());
            int result = cs.executeUpdate();
            return result;
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<Employee> ToList() throws Exception {
        List<Employee> employees = new ArrayList<>();
        try {
            Conexion cn = Conexion.getInstance();
            cnn = cn.getConnection();
            String sql = "{CALL SP_LIST_EMPLOYEES}";
            cs = cnn.prepareCall(sql);
            rs = cs.executeQuery();
            Employee emp;
            while (rs.next()) {
                emp = new Employee();
                emp.setEmployee_id(rs.getString("employee_id"));
                emp.setName(rs.getString("name"));
                emp.setUsername(rs.getString("username"));
                emp.setRole(rs.getString("role"));
                emp.setStatus(rs.getBoolean("status"));
                employees.add(emp);
            }
        } catch (SQLException e) {
            throw e;
        }
        return employees;
    }
    
    public int ActivateEmployee(String employee_id) throws SQLException{
        try {
            Conexion cn = Conexion.getInstance();
            cnn = cn.getConnection();
            String sql = "{CALL SP_ACTIVATE_EMPLOYEE(?)}";
            cs = cnn.prepareCall(sql);
            cs.setString(1, employee_id);
            int result = cs.executeUpdate();
            return result;
        } catch (SQLException e) {
            throw e;
        }
    }

    public int DeleteEmployee(String employee_id) throws SQLException {
        try {
            Conexion cn = Conexion.getInstance();
            cnn = cn.getConnection();
            String sql = "{CALL SP_DELETE_EMPLOYEE(?)}";
            cs = cnn.prepareCall(sql);
            cs.setString(1, employee_id);
            int result = cs.executeUpdate();
            return result;
        } catch (SQLException e) {
            throw e;
        }
    }

}

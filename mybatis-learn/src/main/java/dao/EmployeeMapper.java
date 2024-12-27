package dao;

import model.Employee;

import java.util.List;

/**
 * @author: jiaolong
 * @date: 2024/06/17 10:11
 **/
public interface EmployeeMapper {
    List<Employee> getQuery(List  userIds);
}

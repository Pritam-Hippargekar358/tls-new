package org.root.mapper;

import org.root.domain.Employee;
import org.root.dto.EmployeeDto;

public class EmployeeMapper {

    public static EmployeeDto convertToDto(Employee employee) {
        return new EmployeeDto(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getStatus());
    }

    public static Employee convertToEntity(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setFirstName(employeeDto.firstName());
        employee.setLastName(employeeDto.lastName());
        employee.setEmail(employeeDto.email());
        employee.setStatus(employeeDto.status());
        return employee;
    }
}

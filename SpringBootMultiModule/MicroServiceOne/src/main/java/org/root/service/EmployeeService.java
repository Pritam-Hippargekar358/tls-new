package org.root.service;

import org.root.dto.EmployeeDto;
import org.root.mapper.EmployeeMapper;
import org.root.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeDto> getAllEmployees() {
        //catch (DataAccessException e) {}
        return employeeRepository.findAll().stream()
                .map(EmployeeMapper::convertToDto)
                .collect(Collectors.toList());
    }
}

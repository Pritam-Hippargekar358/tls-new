package org.root.dto;

import org.root.enums.Status;

public record EmployeeDto(Long id, String firstName, String lastName, String email, Status status) {
}

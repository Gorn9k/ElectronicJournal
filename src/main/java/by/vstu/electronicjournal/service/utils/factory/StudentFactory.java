package by.vstu.electronicjournal.service.utils.factory;

import by.vstu.electronicjournal.dto.StudentDTO;
import by.vstu.electronicjournal.entity.Group;
import by.vstu.electronicjournal.entity.Student;
import by.vstu.electronicjournal.entity.common.Status;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;

public final class StudentFactory implements AbstractFactoryForRelatedResources<Student, StudentDTO> {

    @Override
    public Student create(StudentDTO dto) {
        Student student = new Student();
        student.setIdFromSource(dto.getId());
        student.setName(dto.getName());
        student.setPatronymic(dto.getPatronymic());
        student.setSurname(dto.getSurname());
        student.setStatus(dto.getStatus());
        return student;
    }
}

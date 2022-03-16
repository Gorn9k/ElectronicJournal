package by.vstu.electronicjournal.service.utils.factory;

import by.vstu.electronicjournal.dto.TeacherDTO;
import by.vstu.electronicjournal.entity.Teacher;
import by.vstu.electronicjournal.entity.common.Status;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;

public final class TeacherFactory implements AbstractFactoryForRelatedResources<Teacher, TeacherDTO> {

    @Override
    public Teacher create(TeacherDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setIdFromSource(dto.getId());
        teacher.setName(dto.getName());
        teacher.setSurname(dto.getSurname());
        teacher.setPatronymic(dto.getPatronymic());
        //teacher.setStatus(Status.valueOf(dto.getStatus()));
        return teacher;
    }
}

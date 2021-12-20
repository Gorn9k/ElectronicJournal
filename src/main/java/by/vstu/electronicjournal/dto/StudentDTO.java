package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import by.vstu.electronicjournal.entity.Group;
import lombok.Data;

@Data
public class StudentDTO extends AbstractDTO {

    private String surname;
    private String name;
    private String patronymic;
    private Integer subGroup;
}

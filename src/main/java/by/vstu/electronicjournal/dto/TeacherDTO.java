package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import lombok.Data;

@Data
public class TeacherDTO extends AbstractDTO {

    private String surname;
    private String name;
    private String patronymic;
    private Long idFromSource;
}

package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import lombok.Data;

@Data
public class JournalContentDTO extends AbstractDTO {

    private Boolean presence;
    private Integer grade;
    private String discription;
    private StudentDTO student;
}

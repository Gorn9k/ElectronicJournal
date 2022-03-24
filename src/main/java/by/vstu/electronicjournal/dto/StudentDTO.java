package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

//@EqualsAndHashCode(callSuper = false)
@Data
public class StudentDTO extends AbstractDTO {

    private String surname;
    private String name;
    private String patronymic;
    private Integer subGroupIdentificator;

}

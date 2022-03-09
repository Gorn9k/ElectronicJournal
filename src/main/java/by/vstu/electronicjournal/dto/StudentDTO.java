package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import by.vstu.electronicjournal.entity.Group;
import by.vstu.electronicjournal.entity.SubGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class StudentDTO extends AbstractDTO {

    private String surname;
    private String name;
    private String patronymic;
    @JsonIgnoreProperties(value =  {"id", "status", "created", "updated", "students", "journalHeaders", "hibernateLazyInitializer", "handler"}, allowSetters = true)
    private SubGroup subGroup;
}

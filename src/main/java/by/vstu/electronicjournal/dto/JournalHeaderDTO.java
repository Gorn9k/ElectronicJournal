package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import by.vstu.electronicjournal.entity.SubGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class JournalHeaderDTO extends AbstractDTO {

    @JsonIgnoreProperties(value =  {"id", "status", "created", "updated", "students", "journalHeaders", "hibernateLazyInitializer", "handler"}, allowSetters = true)
    private SubGroup subGroup;
    private String classTopic;
    private String discription;
    private LocalDate dateOfLesson;
    private Integer hoursCount;
    private TypeClassDTO typeClass;
    private List<JournalContentDTO> journalContents;
}

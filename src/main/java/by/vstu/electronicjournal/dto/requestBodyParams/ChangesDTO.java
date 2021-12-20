package by.vstu.electronicjournal.dto.requestBodyParams;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ChangesDTO extends AbstractDTO {

    private LocalDate canceled;
    private LocalDate postponed;
    private String teacherFio;
    private Integer frame;
    private Integer location;
    private Long contentId;
}

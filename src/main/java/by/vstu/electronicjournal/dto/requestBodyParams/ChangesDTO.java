package by.vstu.electronicjournal.dto.requestBodyParams;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ChangesDTO extends AbstractDTO {

    private LocalDate canceled;
    private LocalDate postponed;
    private String teacherFio;
    private String frame;
    private String location;
    private Long contentId;
}

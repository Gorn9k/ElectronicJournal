package by.vstu.electronicjournal.dto.requestBodyParams;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import lombok.Data;

@Data
public class PatternDTO extends AbstractDTO {

    private String lessonDay;
    private Boolean numerator;
    private Integer weekNumber;
    private Integer lessonNumber;
    private Integer subGroup;
    private String frame;
    private String location;
    private String disciplineName;
    private String typeClassName;
    private String groupName;
    private String teacherFio;
}

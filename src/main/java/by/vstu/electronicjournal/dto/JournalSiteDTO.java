package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JournalSiteDTO extends AbstractDTO {

    private DisciplineDTO discipline;
    private TeacherDTO teacher;
    private GroupDTO group;
    private Integer streamId;
    private List<JournalHeaderDTO> journalHeaders;
}

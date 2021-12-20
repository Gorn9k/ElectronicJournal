package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import lombok.Data;

@Data
public class DisciplineDTO extends AbstractDTO {

    private String name;
    private Long idFromSource;
}

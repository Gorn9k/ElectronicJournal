package by.vstu.electronicjournal.dto.common;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class AbstractDTO {

    private Long id;

}

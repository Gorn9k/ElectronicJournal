package by.vstu.electronicjournal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class StudentPerformanceDTO {

    private Number overallGPA = 0;
    @JsonIgnoreProperties(value =  {"id", "status", "patronymic", "subGroupIdentificator", "hibernateLazyInitializer", "handler"}, allowSetters = true)
    private StudentDTO studentDTO;
}

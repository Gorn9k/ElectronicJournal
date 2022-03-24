package by.vstu.electronicjournal.dto;

import lombok.Data;

@Data
public class AcademicPerformanceDTO {

    private StudentPerformanceDTO studentPerformanceDTO;
    private Number totalNumberPasses = 0;
    private Number totalNumberLates = 0;
}

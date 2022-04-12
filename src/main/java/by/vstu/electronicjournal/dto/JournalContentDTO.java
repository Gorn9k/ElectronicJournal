package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import lombok.Data;

@Data
public class JournalContentDTO extends AbstractDTO implements Comparable<JournalContentDTO> {

    private Boolean presence;
    private Short lateness;
    private Integer grade;
    private String discription;
    private StudentDTO student;

    @Override
    public int compareTo(JournalContentDTO o) {
        return (student.getSurname() + " " + student.getName().toUpperCase().charAt(0) +
                "." + student.getPatronymic().toUpperCase().charAt(0)).compareTo(o.student.getSurname() + " " + o.student.getName().toUpperCase().charAt(0) +
                "." + o.student.getPatronymic().toUpperCase().charAt(0));
    }
}

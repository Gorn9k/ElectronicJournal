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
        return (student.getSurname() + " " + student.getName() + " " +
                (student.getPatronymic() == null ? "" : student.getPatronymic())).compareTo(o.student.getSurname() + " " + o.student.getName() +
                        (o.student.getPatronymic() == null ? "" : o.student.getPatronymic()));
    }
}

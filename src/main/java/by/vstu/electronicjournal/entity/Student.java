package by.vstu.electronicjournal.entity;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "student")
@AttributeOverride(name = "id", column = @Column(name = "student_id"))
public class Student extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "surname")
    private String surname;

    @Column(name = "name")
    private String name;

    @Column(name = "patronymic")
    private String patronymic;

    @ManyToOne
    @JoinColumn(name = "sub_group_id")
    private SubGroup subGroup;

    @Column(name = "id_from_source")
    private Long idFromSource;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "student")
    private List<JournalContent> journalContents = new ArrayList<>();
}

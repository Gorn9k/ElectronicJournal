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
@Table(name = "teacher")
@AttributeOverride(name = "id", column = @Column(name = "teacher_id"))
public class Teacher extends AbstractEntity {

    @Column(name = "surname")
    private String surname;

    @Column(name = "name")
    private String name;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "id_from_source")
    private Long idFromSource;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "teacher")
    private List<JournalSite> journalSites = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "teacher")
    private List<JournalHeader> journalHeaders = new ArrayList<>();

}

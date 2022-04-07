package by.vstu.electronicjournal.entity;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@Table(name = "journal_header")
@AttributeOverride(name = "id", column = @Column(name = "journal_header_id"))
public class JournalHeader extends AbstractEntity {

    @ToString.Exclude
    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "type_class_id")
    private TypeClass typeClass;

    @ToString.Exclude
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "journal_site_id")
    private JournalSite journalSite;

    @Column(name = "sub_group")
    private Integer subGroup;

    @Column(name = "class_topic")
    private String classTopic;

    @ManyToOne
    @JoinColumn(name = "replacement_teacher_id")
    private Teacher teacher;

    @Column(name = "discription")
    private String discription;

    @Column(name = "date_of_lesson")
    private LocalDate dateOfLesson;

    @Column(name = "hours_count")
    private Integer hoursCount;

    @ToString.Exclude
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "journalHeader")
    private List<JournalContent> journalContents = new ArrayList<>();
}

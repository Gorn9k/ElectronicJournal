package by.vstu.electronicjournal.entity;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "journal_content")
@AttributeOverride(name = "id", column = @Column(name = "journal_content_id"))
public class JournalContent extends AbstractEntity {

	@ManyToOne
	@JoinColumn(name = "journal_header_id")
	private JournalHeader journalHeader;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	@Column(name = "presence")
	private Boolean presence;

	@Column(name = "lateness")
	private Short lateness;

	@Column(name = "grade")
	private Integer grade;

	@Column(name = "discription")
	private String discription;
}

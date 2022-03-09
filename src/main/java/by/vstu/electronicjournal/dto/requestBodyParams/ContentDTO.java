package by.vstu.electronicjournal.dto.requestBodyParams;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import by.vstu.electronicjournal.entity.SubGroup;
import com.sun.istack.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ContentDTO extends AbstractDTO {

	@NotNull
	private LocalDate lessonDate;
	@NotNull
	private Integer lessonNumber;
	@NotNull
	private SubGroup subGroup;
	@NotNull
	private String frame;
	@NotNull
	private String location;
	@NotNull
	private String disciplineName;
	@NotNull
	private String typeClassName;
	@NotNull
	private String groupName;
	@NotNull
	private String teacherFio;
	private ChangesDTO changes;
}

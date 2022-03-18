package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.JournalContentDTO;
import by.vstu.electronicjournal.dto.JournalHeaderDTO;
import by.vstu.electronicjournal.dto.TypeClassDTO;
import by.vstu.electronicjournal.dto.requestBodyParams.ParamsForCreateJournalHeader;
import by.vstu.electronicjournal.dto.requestBodyParams.PatternDTO;
import by.vstu.electronicjournal.entity.JournalContent;
import by.vstu.electronicjournal.entity.JournalHeader;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.JournalContentRepository;
import by.vstu.electronicjournal.repository.JournalHeaderRepository;
import by.vstu.electronicjournal.repository.JournalSiteRepository;
import by.vstu.electronicjournal.service.JournalContentService;
import by.vstu.electronicjournal.service.JournalHeaderService;
import by.vstu.electronicjournal.service.TypeClassService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JournalHeaderServiceImpl
		extends CommonCRUDServiceImpl<JournalHeader, JournalHeaderDTO, JournalHeaderRepository>
		implements JournalHeaderService {

	@Value("${entrance.timetable}")
	private String path;

	@Autowired
	private Mapper mapper;

	@Autowired
	private JournalSiteRepository journalSiteRepository;

	@Autowired
	private JournalHeaderRepository journalHeaderRepository;

	@Autowired
	private JournalContentRepository journalContentRepository;

	@Autowired
	private JournalContentService journalContentService;

	@Autowired
	private TypeClassService typeClassService;

	public JournalHeaderServiceImpl() {
		super(JournalHeader.class, JournalHeaderDTO.class);
	}

	@Override
	public List<JournalHeaderDTO> search(String query) {
		if (query.isEmpty()) {
			return findAll();
		}
		return mapper.toDTOs(journalHeaderRepository.findAll(getSpecifications(query)),
				JournalHeaderDTO.class);
	}

	@Override
	public JournalHeaderDTO create(ParamsForCreateJournalHeader params) {

		JournalSite journalSite = journalSiteRepository.getById(params.getJournalSiteId());
		JournalHeader journalHeader = (JournalHeader) mapper
				.toEntity(params.getJournalHeaderDTO(), JournalHeader.class);
		journalHeader.setJournalSite(journalSite);
		journalHeader = journalHeaderRepository.save(journalHeader);

		journalContentService.generate(journalHeader);

		return (JournalHeaderDTO) mapper.toDTO(journalHeader, JournalHeaderDTO.class);
	}

	@Override
	public List<JournalSite> generate(List<JournalSite> params) {

		RestTemplate restTemplate = new RestTemplate();

		for (JournalSite journalSite : params) {

			String queryToCommonInfo = String.format(
					"%s/patterns/search?q=groupName==%s;disciplineName==\'%s\';teacherFio==%s*",
					path,
					journalSite.getGroup().getName(),
					journalSite.getDiscipline().getName(),
					journalSite.getTeacher().getSurname()
			);
			List<PatternDTO> patternDTOS =
					restTemplate.exchange(queryToCommonInfo, HttpMethod.GET, null,
							new ParameterizedTypeReference<List<PatternDTO>>() {
							}).getBody();

			List<JournalHeaderDTO> headerDTOS = new ArrayList<>();

			for (PatternDTO patternDTO : patternDTOS) {
				JournalHeaderDTO journalHeaderDTO = new JournalHeaderDTO();
				journalHeaderDTO.setSubGroup(patternDTO.getSubGroup());
				journalHeaderDTO.setHoursCount(patternDTO.getLessonNumber());
				TypeClassDTO typeClassDTO = typeClassService
						.validator("name==\'" + patternDTO.getTypeClassName() + "\'").get(0);
				journalHeaderDTO.setTypeClass(typeClassDTO);

				headerDTOS.add(journalHeaderDTO);
			}

			List<JournalHeader> journalHeaders = mapper.toEntities(headerDTOS, JournalHeader.class);
			journalHeaders.stream()
					.forEach(journalHeader -> journalHeader.setJournalSite(journalSite));
			journalSite.setJournalHeaders(journalHeaderRepository.saveAll(journalHeaders));
		}
		return params;
	}

	@Override
	public List<JournalContentDTO> editList(Long id, List<JournalContentDTO> dtos) {

		JournalHeader header = journalHeaderRepository.getById(id);
		List<JournalContent> contents = mapper.toEntities(dtos, JournalContent.class);

		for (JournalContent content : contents) {
			content.setJournalHeader(header);
		}
		return mapper
				.toDTOs(journalContentRepository.saveAllAndFlush(contents), JournalContentDTO.class);
	}
}

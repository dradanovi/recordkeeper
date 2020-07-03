package hr.java.web.radanovic.recordkeeper.mapper;

import org.springframework.stereotype.Service;

import hr.java.web.radanovic.recordkeeper.dto.WorkHoursDto;
import hr.java.web.radanovic.recordkeeper.dto.WorkSubjectDto;
import hr.java.web.radanovic.recordkeeper.exception.UserException;
import hr.java.web.radanovic.recordkeeper.model.WorkHours;
import hr.java.web.radanovic.recordkeeper.model.WorkSubjects;
import hr.java.web.radanovic.recordkeeper.repository.UserRepository;
import hr.java.web.radanovic.recordkeeper.service.FormatDateService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WorksMapper {

	private final UserRepository userRepo;
	private final FormatDateService dateService;

	public WorkHours mapHoursDtoTokHours(WorkHoursDto hoursDto) {
		return WorkHours.builder()
				.start((hoursDto.getStart() != null) ? dateService.stringToLDT(hoursDto.getStart(), true) : null)
				.end((hoursDto.getEnd() != null) ? dateService.stringToLDT(hoursDto.getEnd(), false) : null)
				.user(userRepo.findByUsername(hoursDto.getUsername())
						.orElseThrow(() -> new UserException("User not found in mapper")))
				.build();
	}

	public WorkHoursDto mapHoursTokHoursDro(WorkHours hours) {
		return WorkHoursDto.builder().start(dateService.LDTToString(hours.getStart()))
				.end((hours.getEnd() != null) ? dateService.LDTToString(hours.getEnd()) : null)
				.username(hours.getUser().getUsername()).build();
	}

	public WorkSubjects mapSubjectsDtoToSubjects(WorkSubjectDto subjectsDto) {
		return WorkSubjects.builder()
				.start((subjectsDto.getStart() != null) ? dateService.stringToLDT(subjectsDto.getStart(), true) : null)
				.end((subjectsDto.getEnd() != null) ? dateService.stringToLDT(subjectsDto.getEnd(), false) : null)
				.title(subjectsDto.getTitle()).desc(subjectsDto.getDesc())
				.user(userRepo.findByUsername(subjectsDto.getUsername())
						.orElseThrow(() -> new UserException("User not found in mapper")))
				.build();
	}

	public WorkSubjectDto mapSubjectsToSubjectsDto(WorkSubjects subjects) {
		return WorkSubjectDto.builder().start(dateService.LDTToString(subjects.getStart()))
				.end(dateService.LDTToString(subjects.getStart())).title(subjects.getTitle()).desc(subjects.getDesc())
				.username(subjects.getUser().getUsername()).build();
	}

}

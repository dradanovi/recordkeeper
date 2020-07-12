package hr.java.web.radanovic.recordkeeper.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import hr.java.web.radanovic.recordkeeper.dto.FilerHoursRequest;
import hr.java.web.radanovic.recordkeeper.dto.StartSubjectRequest;
import hr.java.web.radanovic.recordkeeper.dto.WorkHoursDto;
import hr.java.web.radanovic.recordkeeper.dto.WorkSubjectDto;
import hr.java.web.radanovic.recordkeeper.mapper.WorksMapper;
import hr.java.web.radanovic.recordkeeper.model.AppUser;
import hr.java.web.radanovic.recordkeeper.model.WorkHours;
import hr.java.web.radanovic.recordkeeper.model.WorkSubjects;
import hr.java.web.radanovic.recordkeeper.repository.UserRepository;
import hr.java.web.radanovic.recordkeeper.repository.WorkHoursRepository;
import hr.java.web.radanovic.recordkeeper.repository.WorkSubjectRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WorkSubjectsService {

	private final WorkSubjectRepository subjectRepo;
	private final WorksMapper workMapper;
	private final FormatDateService dateService;
	private final AuthService authService;
	private final UserRepository userRepo;
	private final WorkHoursRepository hoursRepo;

	@Transactional
	public void startSubject(StartSubjectRequest subjectDto) {
		if (!checkSubject()) {
			AppUser user = authService.getCurrentUser();
			if (!hoursRepo.checkIfOpenHours(user)) {
				WorkHours hours = new WorkHours(LocalDateTime.now(), authService.getCurrentUser());
				hoursRepo.save(hours);
			}
			WorkSubjects subject = new WorkSubjects();
			subject.setStart(LocalDateTime.now());
			subject.setTitle(subjectDto.getTitle());
			if (subjectDto.getDesc() != null) {
				subject.setDesc(subjectDto.getDesc());
			} else {
				subject.setDesc("No description");
			}
			subject.setUser(user);
			subjectRepo.save(subject);
		}
	}

	@Transactional
	public void endSubject() {
		if (checkSubject()) {
			WorkSubjects subject = subjectRepo.findByOpenSubject(authService.getCurrentUser()).get();
			subject.setEnd(LocalDateTime.now());
			subjectRepo.save(subject);
		}
	}

	public List<WorkSubjectDto> getSubjectsDisplayFilter(FilerHoursRequest subjectsDto) {
		List<WorkSubjects> listSubjects = subjectRepo.findByDateAndUser(
				dateService.stringToLDT(subjectsDto.getStart(), true),
				(subjectsDto.getEnd() != null) ? dateService.stringToLDT(subjectsDto.getEnd(), false)
						: LocalDateTime.now(),
				authService.getCurrentUser());
		return listSubjects.stream().map(e -> workMapper.mapSubjectsToSubjectsDto(e)).collect(Collectors.toList());
	}

	public List<WorkSubjectDto> getSubjectsByUserDisplayFilter(WorkHoursDto subjectsDto) {
		List<WorkSubjects> listSubjects = subjectRepo.findByDateAndUser(
				dateService.stringToLDT(subjectsDto.getStart(), true),
				(subjectsDto.getEnd() != null) ? dateService.stringToLDT(subjectsDto.getEnd(), false)
						: LocalDateTime.now(),
				userRepo.findByUsername(subjectsDto.getUsername()).get());
		return listSubjects.stream().map(e -> workMapper.mapSubjectsToSubjectsDto(e)).collect(Collectors.toList());
	}

	public boolean checkSubject() {
		return subjectRepo.checkOpenSubject(authService.getCurrentUser());
	}

	public List<WorkSubjectDto> getSubjectsDisplayScript(FilerHoursRequest req) {
		List<Object[]> list = hoursRepo.workHours(dateService.stringToLDT(req.getStart(), true),
				dateService.stringToLDT(req.getEnd(), false), authService.getCurrentUsername());

		List<WorkSubjectDto> response = new ArrayList<>();
		for (Object[] objects : list) {
			WorkSubjectDto subjects = new WorkSubjectDto(objects[0].toString().substring(0, 19),
					objects[1].toString().substring(0, 19), objects[2].toString(), objects[3].toString(),
					objects[4].toString());
			response.add(subjects);
		}
		return response;
	}

}

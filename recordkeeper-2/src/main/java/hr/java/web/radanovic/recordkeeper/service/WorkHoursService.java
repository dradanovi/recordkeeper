package hr.java.web.radanovic.recordkeeper.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import hr.java.web.radanovic.recordkeeper.dto.FilerHoursRequest;
import hr.java.web.radanovic.recordkeeper.dto.WorkHoursDto;
import hr.java.web.radanovic.recordkeeper.exception.HoursException;
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
public class WorkHoursService {

	private final WorkHoursRepository hoursRepo;
	private final WorksMapper workMapper;
	private final FormatDateService dateService;
	private final AuthService authService;
	private final UserRepository userRepo;
	private final WorkSubjectRepository subjectRepo;

	@Transactional
	public void startHours() {
		if (!checkHours()) {
			WorkHours hours = new WorkHours(LocalDateTime.now(), authService.getCurrentUser());
			hoursRepo.save(hours);
		}
	}

	@Transactional
	public void endHours() {
		if (checkHours()) {
			AppUser user = authService.getCurrentUser();
			if (subjectRepo.checkOpenSubject(user) == true) {
				WorkSubjects subject = subjectRepo.findByOpenSubject(user).get();
				subject.setEnd(LocalDateTime.now());
				subjectRepo.save(subject);
			}
			WorkHours hours = hoursRepo.findByOpenHours(user)
					.orElseThrow(() -> new HoursException("Open work hours not found"));
			hours.setEnd(LocalDateTime.now());
			hoursRepo.save(hours);
		}
	}

	public List<WorkHoursDto> getHoursDisplayFilter(FilerHoursRequest hoursDto) {
		List<WorkHours> listHours = hoursRepo.findByDateAndUser(dateService.stringToLDT(hoursDto.getStart(), true),
				(hoursDto.getEnd() != null) ? dateService.stringToLDT(hoursDto.getEnd(), false) : LocalDateTime.now(),
				authService.getCurrentUser());
		return listHours.stream().map(e -> workMapper.mapHoursTokHoursDro(e)).collect(Collectors.toList());
	}

	public List<WorkHoursDto> getHoursByUserDisplayFilter(WorkHoursDto hoursDto) {
		List<WorkHours> listHours = hoursRepo.findByDateAndUser(dateService.stringToLDT(hoursDto.getStart(), true),
				(hoursDto.getEnd() != null) ? dateService.stringToLDT(hoursDto.getEnd(), false) : LocalDateTime.now(),
				(hoursDto.getUsername().equals("")) ? null : userRepo.findByUsername(hoursDto.getUsername()).get());
		return listHours.stream().map(e -> workMapper.mapHoursTokHoursDro(e)).collect(Collectors.toList());
	}

	public List<WorkHoursDto> getAll() {
		return hoursRepo.findAll().stream().map(e -> workMapper.mapHoursTokHoursDro(e)).collect(Collectors.toList());
	}

	public boolean checkHours() {
		return hoursRepo.checkIfOpenHours(authService.getCurrentUser());
	}

	public String hoursWorked(List<WorkHoursDto> listOfTimes) {
		long diff = 0;
		for (WorkHoursDto hours : listOfTimes) {
			LocalDateTime start = dateService.stringToLDT(hours.getStart(), true);
			LocalDateTime end = dateService.stringToLDT(hours.getEnd(), false);

			diff = diff + ChronoUnit.MINUTES.between(start, end);
		}
		System.out.println("diff -> " + diff);
		return String.valueOf(diff);
	}
	
	public List<WorkHoursDto> getHoursDisplayScript(FilerHoursRequest req) {
		List<Object[]> list = hoursRepo.workHours(dateService.stringToLDT(req.getStart(), true),
				dateService.stringToLDT(req.getEnd(), false), authService.getCurrentUsername());

		List<WorkHoursDto> response = new ArrayList<>();
		for (Object[] objects : list) {
			WorkHoursDto hours = new WorkHoursDto(objects[0].toString().substring(0, 19),
					objects[1].toString().substring(0, 19), objects[2].toString());
			response.add(hours);
		}
		return response;
	}

}

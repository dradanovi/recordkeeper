package hr.java.web.radanovic.recordkeeper.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "work_subject")
public class WorkSubjects {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORK_SUBJECT_SEQ")
	@SequenceGenerator(name = "WORK_SUBJECT_SEQ", sequenceName = "WORK_SUBJECT_SEQ", initialValue = 1, allocationSize = 1)
	private Long id;
	@Column(name = "subject_start")
	private LocalDateTime start;
	@Column(name = "subject_end")
	private LocalDateTime end;
	private String title;
	@Column(name = "subject_desc")
	private String desc;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id")
	private AppUser user;
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "work_hours_id")
//	private WorkHours hours;
	
}

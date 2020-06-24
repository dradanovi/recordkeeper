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
@Table(name = "work_hours")
public class WorkHours {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORK_HOURS_SEQ")
	@SequenceGenerator(name = "WORK_HOURS_SEQ", sequenceName = "WORK_HOURS_SEQ", initialValue = 1, allocationSize = 1)
	private Long id;
	@Column(name = "hours_start")
	private LocalDateTime start;
	@Column(name = "hours_end")
	private LocalDateTime end;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id")
	private AppUser user;

//	@OneToMany(mappedBy = "hours", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@Builder.Default
//	private List<WorkSubjects> workSubjects = new ArrayList<>();

	public WorkHours(LocalDateTime start, AppUser user) {
		this.start = start;
		this.user = user;
	}

}

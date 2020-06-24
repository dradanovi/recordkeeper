package hr.java.web.radanovic.recordkeeper.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
	@SequenceGenerator(name = "USERS_SEQ", sequenceName = "USERS_SEQ", initialValue = 1, allocationSize = 1)
	private Long id;
	private String username;
	private String password;
	private String email;
	private boolean enabled;
	private Instant created;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "USERS_AUTHORITY", joinColumns = @JoinColumn(name = "USERS_ID", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "id"))
	private Set<Authorities> roles = new HashSet<>();

	@Exclude
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<WorkHours> workHours = new ArrayList<>();

	@Exclude
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<WorkSubjects> workSubjects = new ArrayList<>();

}

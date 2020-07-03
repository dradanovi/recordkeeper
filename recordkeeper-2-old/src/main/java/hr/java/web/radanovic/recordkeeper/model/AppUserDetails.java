package hr.java.web.radanovic.recordkeeper.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_details")
public class AppUserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_DETAILS_SEQ")
	@SequenceGenerator(name = "USER_DETAILS_SEQ", sequenceName = "USER_DETAILS_SEQ", initialValue = 1, allocationSize = 1)
	private Long id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
//	@Column(name = "oib")
	private String OIB;
	private String address;
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "users_id")
	private AppUser user;
}

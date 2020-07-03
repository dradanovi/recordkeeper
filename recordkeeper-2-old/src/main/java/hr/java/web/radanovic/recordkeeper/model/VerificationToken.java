package hr.java.web.radanovic.recordkeeper.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class VerificationToken {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VERIFICATIONTOKEN_SEQ")
	@SequenceGenerator(name = "VERIFICATIONTOKEN_SEQ", sequenceName = "VERIFICATIONTOKEN_SEQ", initialValue = 1, allocationSize = 1)
	private Long id;
	private String token;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id")
	private AppUser user;
	private Instant expiration;
}

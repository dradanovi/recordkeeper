package hr.java.web.radanovic.recordkeeper.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class RefreshToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REFRESHTOKEN_SEQ")
	@SequenceGenerator(name = "REFRESHTOKEN_SEQ", sequenceName = "REFRESHTOKEN_SEQ", initialValue = 1, allocationSize = 1)
	private Long id;
	private String token;
	private Instant created;

}

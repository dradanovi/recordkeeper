package hr.java.web.radanovic.recordkeeper.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Authorities {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTHORITIES_SEQ")
	@SequenceGenerator(name = "AUTHORITIES_SEQ", sequenceName = "AUTHORITIES_SEQ", initialValue=1, allocationSize = 1)
	private Long id;
	@Enumerated(EnumType.STRING)
	private Authority authority;
}

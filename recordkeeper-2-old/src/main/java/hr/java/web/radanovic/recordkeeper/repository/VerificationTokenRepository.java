package hr.java.web.radanovic.recordkeeper.repository;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import hr.java.web.radanovic.recordkeeper.model.VerificationToken;

@Repository
@Transactional
public class VerificationTokenRepository {

	@PersistenceContext
	private EntityManager em;
	
	public VerificationToken save(VerificationToken verificationToken) {
		em.persist(verificationToken);
		return verificationToken;
	}

	public Optional<VerificationToken> findByToken(String token) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<VerificationToken> cq = cb.createQuery(VerificationToken.class);
		Root<VerificationToken> root = cq.from(VerificationToken.class);
		cq.where(cb.equal(root.get("token"), token));
		
		return Optional.ofNullable(em.createQuery(cq).getSingleResult());
	}

	public void remove(VerificationToken verificationToken) {
		em.remove(verificationToken);
	}

}

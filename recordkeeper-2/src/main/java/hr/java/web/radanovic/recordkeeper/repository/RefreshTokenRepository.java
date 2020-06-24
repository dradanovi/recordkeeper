package hr.java.web.radanovic.recordkeeper.repository;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import hr.java.web.radanovic.recordkeeper.model.RefreshToken;

@Repository
@Transactional
public class RefreshTokenRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public RefreshToken save(RefreshToken token) {
		em.persist(token);
		return token;
	}

	public Optional<RefreshToken> findByToken(String token) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RefreshToken> cq = cb.createQuery(RefreshToken.class);
		Root<RefreshToken> root = cq.from(RefreshToken.class);
		cq.where(cb.equal(root.get("token"), token));
		
		try {
			return Optional.ofNullable(em.createQuery(cq).getSingleResult());
		} catch (NoResultException e) {
			throw new NoResultException("no result for refresh token");
		}
	}

	public void deleteByToken(String token) {
		em.remove(findByToken(token).get());
	}

}

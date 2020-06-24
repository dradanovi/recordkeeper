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

import hr.java.web.radanovic.recordkeeper.model.AppUser;
import hr.java.web.radanovic.recordkeeper.model.AppUserDetails;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@Transactional
public class UserDetailsRepository {

	@PersistenceContext
	private EntityManager em;

	public AppUserDetails save(AppUserDetails details) {
		em.persist(details);
		return details;
	}
	
	public AppUserDetails update(AppUserDetails details) {
		em.merge(details);
		return details;
	}

	public Optional<AppUserDetails> findByUser(AppUser user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AppUserDetails> cq = cb.createQuery(AppUserDetails.class);
		Root<AppUserDetails> root = cq.from(AppUserDetails.class);
		cq.where(cb.equal(root.get("user"), user));

		try {
			return Optional.ofNullable(em.createQuery(cq).getSingleResult());
		} catch (NoResultException e) {
			log.info("No details found in UserDetailsRepository for user " + user.getUsername());
			return Optional.empty();
		}
	}

}

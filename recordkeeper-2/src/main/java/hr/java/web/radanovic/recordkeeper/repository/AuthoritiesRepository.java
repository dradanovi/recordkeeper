package hr.java.web.radanovic.recordkeeper.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import hr.java.web.radanovic.recordkeeper.model.Authorities;
import hr.java.web.radanovic.recordkeeper.model.Authority;

@Repository
@Transactional
public class AuthoritiesRepository {

	@PersistenceContext
	private EntityManager em;

	public Authorities findByAuthority(Authority authority) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Authorities> cq = cb.createQuery(Authorities.class);
		Root<Authorities> root = cq.from(Authorities.class);
		cq.where(cb.equal(root.get("authority"), authority));

		return em.createQuery(cq).getSingleResult();
	}

}

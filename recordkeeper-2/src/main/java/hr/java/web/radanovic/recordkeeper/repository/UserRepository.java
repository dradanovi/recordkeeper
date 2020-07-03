package hr.java.web.radanovic.recordkeeper.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import hr.java.web.radanovic.recordkeeper.model.AppUser;
import hr.java.web.radanovic.recordkeeper.model.AppUserDetails;

@Repository
@Transactional
public class UserRepository {

	@PersistenceContext
	private EntityManager em;

	public AppUser save(AppUser user) {
		em.persist(user);
		return user;
	}

	public List<AppUser> findAll() {
		return em.createQuery("from AppUser", AppUser.class).getResultList();
	}

	public List<AppUserDetails> findAllDetails() {
		return em.createQuery("from AppUserDetails", AppUserDetails.class).getResultList();
	}

	public Optional<AppUser> findByUsername(String username) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AppUser> cq = cb.createQuery(AppUser.class);
		Root<AppUser> root = cq.from(AppUser.class);
		cq.where(cb.equal(root.get("username"), username));

		return Optional.ofNullable(em.createQuery(cq).getSingleResult());
	}

	public List<String> findAllUsernames() {
		return em.createQuery("from AppUser", AppUser.class).getResultList().stream().map(e -> e.getUsername())
				.collect(Collectors.toList());
	}
	
	public Optional<AppUser> findById(Long id){
		return Optional.ofNullable(em.find(AppUser.class, id));
	}
}

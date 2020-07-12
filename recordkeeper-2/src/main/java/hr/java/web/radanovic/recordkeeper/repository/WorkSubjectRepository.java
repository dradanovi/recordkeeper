package hr.java.web.radanovic.recordkeeper.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import hr.java.web.radanovic.recordkeeper.exception.EmptyResultException;
import hr.java.web.radanovic.recordkeeper.model.AppUser;
import hr.java.web.radanovic.recordkeeper.model.WorkHours;
import hr.java.web.radanovic.recordkeeper.model.WorkSubjects;

@Repository
@Transactional
public class WorkSubjectRepository {

	@PersistenceContext
	private EntityManager em;

	public WorkSubjects save(WorkSubjects subject) {
		em.persist(subject);
		return subject;
	}

	public List<WorkSubjects> findByWorkHours(WorkHours hours) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<WorkSubjects> cq = cb.createQuery(WorkSubjects.class);
		Root<WorkSubjects> root = cq.from(WorkSubjects.class);
		cq.where(cb.equal(root.get("workHours"), hours));

		return em.createQuery(cq).getResultList();
	}

	public List<WorkSubjects> findByTitle(String title) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<WorkSubjects> cq = cb.createQuery(WorkSubjects.class);
		Root<WorkSubjects> root = cq.from(WorkSubjects.class);
		cq.where(cb.like(root.get("title"), "%" + title + "%"));

		return em.createQuery(cq).getResultList();
	}

	public Optional<WorkSubjects> findByOpenSubject(AppUser user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<WorkSubjects> cq = cb.createQuery(WorkSubjects.class);
		Root<WorkSubjects> root = cq.from(WorkSubjects.class);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.isNull(root.get("end")));
		predicates.add(cb.equal(root.get("user"), user));
		cq.where(cb.and(predicates.toArray(new Predicate[] {})));

		try {
			return Optional.ofNullable(em.createQuery(cq).getSingleResult());
		} catch (NoResultException e) {
			throw new EmptyResultException("No open subject found for user -> " + user.getUsername());
		}
	}

	public List<WorkSubjects> findByDateAndUser(LocalDateTime start, LocalDateTime end, AppUser user) {
		end = end.plusMinutes(1);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<WorkSubjects> cq = cb.createQuery(WorkSubjects.class);
		Root<WorkSubjects> root = cq.from(WorkSubjects.class);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.greaterThanOrEqualTo(root.get("start"), start));
		predicates.add(cb.lessThanOrEqualTo(root.get("end"), end));
		predicates.add(cb.equal(root.get("user"), user));
		cq.where(cb.and(predicates.toArray(new Predicate[] {})));

		return em.createQuery(cq).getResultList();
	}
	
	public boolean checkOpenSubject(AppUser user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<WorkSubjects> cq = cb.createQuery(WorkSubjects.class);
		Root<WorkSubjects> root = cq.from(WorkSubjects.class);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.isNull(root.get("end")));
		predicates.add(cb.equal(root.get("user"), user));
		cq.where(cb.and(predicates.toArray(new Predicate[] {})));

		if(em.createQuery(cq).getResultList().size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> workHours(LocalDateTime start, LocalDateTime end, String username) {
		List<Object[]> query = em.createStoredProcedureQuery("findHours")
				.registerStoredProcedureParameter(1, LocalDateTime.class, ParameterMode.IN)
				.registerStoredProcedureParameter(2, LocalDateTime.class, ParameterMode.IN)
				.registerStoredProcedureParameter(3, String.class, ParameterMode.IN)
				.registerStoredProcedureParameter(4, Class.class, ParameterMode.REF_CURSOR)
				.setParameter(1, start)
				.setParameter(2, end)
				.setParameter(3, username)
				.getResultList();
		
		return query;
	}
}

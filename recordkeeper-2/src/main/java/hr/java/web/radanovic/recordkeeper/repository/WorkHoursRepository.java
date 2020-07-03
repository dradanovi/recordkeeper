package hr.java.web.radanovic.recordkeeper.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hr.java.web.radanovic.recordkeeper.exception.EmptyResultException;
import hr.java.web.radanovic.recordkeeper.model.AppUser;
import hr.java.web.radanovic.recordkeeper.model.WorkHours;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@Transactional
public class WorkHoursRepository {
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	//	timestamp YYYY-MM-DD HH24:MI:SS

	@PersistenceContext
	private EntityManager em;

	public WorkHours save(WorkHours hours) {
		em.persist(hours);
		return hours;
	}

	public List<WorkHours> findAll() {
		return em.createQuery("from WorkHours", WorkHours.class).getResultList();
	}

	public List<WorkHours> findbyUser(AppUser user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<WorkHours> cq = cb.createQuery(WorkHours.class);
		Root<WorkHours> root = cq.from(WorkHours.class);
		cq.where(cb.equal(root.get("user"), user));

		return em.createQuery(cq).getResultList();
	}

	public List<WorkHours> findByDate(LocalDateTime start, LocalDateTime end) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<WorkHours> cq = cb.createQuery(WorkHours.class);
		Root<WorkHours> root = cq.from(WorkHours.class);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.greaterThanOrEqualTo(root.get("start"), start));
		predicates.add(cb.lessThanOrEqualTo(root.get("end"), end));
		cq.where(cb.and(predicates.toArray(new Predicate[] {})));

		return em.createQuery(cq).getResultList();
	}

	public List<WorkHours> findByDateAndUser(LocalDateTime start, LocalDateTime end, AppUser user) {
		log.info("inside findByDateAndUser");
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<WorkHours> cq = cb.createQuery(WorkHours.class);
		Root<WorkHours> root = cq.from(WorkHours.class);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.greaterThanOrEqualTo(root.get("start"), start));
		predicates.add(cb.lessThanOrEqualTo(root.get("end"), end));
		if (user != null) {
			predicates.add(cb.equal(root.get("user"), user));
		}
		cq.where(cb.and(predicates.toArray(new Predicate[] {})));

		return em.createQuery(cq).getResultList();
	}

	public Optional<WorkHours> findByOpenHours(AppUser user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<WorkHours> cq = cb.createQuery(WorkHours.class);
		Root<WorkHours> root = cq.from(WorkHours.class);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.isNull(root.get("end")));
		predicates.add(cb.equal(root.get("user"), user));
		cq.where(cb.and(predicates.toArray(new Predicate[] {})));

		try {
			return Optional.ofNullable(em.createQuery(cq).getSingleResult());
		} catch (NoResultException e) {
			throw new EmptyResultException("No open hours found for user -> " + user.getUsername());
		}
	}

	public boolean checkIfOpenHours(AppUser user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<WorkHours> cq = cb.createQuery(WorkHours.class);
		Root<WorkHours> root = cq.from(WorkHours.class);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.isNull(root.get("end")));
		predicates.add(cb.equal(root.get("user"), user));
		cq.where(cb.and(predicates.toArray(new Predicate[] {})));

		if (em.createQuery(cq).getResultList().size() > 0) {
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

		
		
//		StoredProcedureQuery query = em.createStoredProcedureQuery("testtest").registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN).
//				registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN).registerStoredProcedureParameter(3, Integer.class, ParameterMode.OUT).
//				setParameter(1, 5).setParameter(2, 10);
//		System.out.println(query.execute());
//		System.out.println("stored procedure " + (Integer)query.getOutputParameterValue(3));
	}

}

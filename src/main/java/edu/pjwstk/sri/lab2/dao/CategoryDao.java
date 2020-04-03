package edu.pjwstk.sri.lab2.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import edu.pjwstk.sri.lab2.model.Category;

/**
 * DAO for Category
 */
@Startup
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CategoryDao {
	@PersistenceContext(unitName = "sri2-persistence-unit")
	private EntityManager em;
	private List<Category> categoryList;

	public void create(Category entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Category entity = em.find(Category.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Category findById(Long id) {
		return em.find(Category.class, id);
	}

	public Category update(Category entity) {
		return em.merge(entity);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@Schedule(hour="*", minute="*", second="*/10") // run every 10 seconds
	@PostConstruct
	private void listAll(){
		System.out.println("----------###### Category Dao listAll start ######---------");
		System.out.println("----------###### (runs every 10 seconds), checking categories ######---------");
		TypedQuery<Category> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.parentCategory LEFT JOIN FETCH c.childCategories ORDER BY c.id",
						Category.class);
		categoryList = findAllQuery.getResultList();
		System.out.println("----------###### Category Dao listAll END ######---------");
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Category> getList(){
//		System.out.println("----------###### should show category data ######---------");
		return categoryList;
	}
	
}

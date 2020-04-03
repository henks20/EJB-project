package edu.pjwstk.sri.lab2.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import edu.pjwstk.sri.lab2.model.Category;
import edu.pjwstk.sri.lab2.model.Product;

/**
 * DAO for Product
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ProductDao {
	@PersistenceContext(unitName = "sri2-persistence-unit")
	private EntityManager em;
	
	public void create(Product entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Product entity = em.find(Product.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Product findById(Long id) {
		return em.find(Product.class, id);
	}

	public Product update(Product entity) {
		return em.merge(entity);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Product> listAll() {
		TypedQuery<Product> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.category ORDER BY p.id",
						Product.class);

//		System.out.println("-----------###### should show product data ##### ------------");
//		System.out.println(findAllQuery.getResultList());
//		System.out.println("-----------###### end showing product data ##### ------------");
		return findAllQuery.getResultList();
	}
	
}

package edu.pjwstk.sri.lab2.test;

import java.util.List;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.pjwstk.sri.lab2.dao.CategoryDao;
import edu.pjwstk.sri.lab2.dao.ProductDao;
import edu.pjwstk.sri.lab2.model.Category;
import edu.pjwstk.sri.lab2.model.Product;

@Named("testBean")
@RequestScoped
public class TestBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private CategoryDao categoryDao;
	
	@Inject
	private ShoppingCart shoppingCart;
	
	@Inject
	private ProductDao productDao;
		
	public TestBean() {
	}
	
	public void printItems() {
		System.out.println("-----------###### START TEST PRINT ITEMS ##### ------------");
		
		System.out.println("-----------###### PRINT : CATEGORIES ##### ------------");
		List<Category> categoryList = categoryDao.getList();
		for(int i = 0; i < categoryList.size(); i++){
			System.out.println(categoryList.get(i));
		}
		
		System.out.println("-----------###### PRINT : PRODUCTS ##### ------------");
		List<Product> productList = productDao.listAll();
		for(int i = 0; i < productList.size(); i++){
			System.out.println(productList.get(i).getName() + "  " + productList.get(i) + "  stock: " + productList.get(i).getStock());
		}
		
		System.out.println("-----------###### END TEST PRINT ITEMS ##### ------------");
	}
	
	public void printProductById(Long id) {
		System.out.println(productDao.findById(id));
	}
	
	public void printCategoryById(Long id){
		System.out.println(categoryDao.findById(id));
	}
	
	public void shoppingTest() {
		System.out.println("-----------###### START SHOPPING TEST ##### ------------");
		System.out.println("-----------###### TEST1: ADD CORRECTLY PRODUCTS TO CART ##### ------------");
		System.out.println("-----------###### TEST2: ADD INCORRECTLY PRODUCTS TO CART (MORE THAN IN STOCK) ##### ------------");
		List<Product> productList = productDao.listAll();

		System.out.println("-----------###### TEST 1 ##### ------------");
		Product testProduct1 = null;
		int testId1 = 2002;
		Product testProduct2 = null;
		int testId2 = 2004;
		Product testProduct3 = null;
		int testId3 = 2006;
		for(int i = 0; i < productList.size(); i++) {
			long productId = productList.get(i).getId();
			if(testId1 == productId) {
				testProduct1 = productList.get(i);
			} else if (testId2 == productId) {
				testProduct2 = productList.get(i);
			} else if (testId3 == productId) {
				testProduct3 = productList.get(i);
			}
		}
		shoppingCart.addProductToCart(testProduct1 , 1);
		shoppingCart.addProductToCart(testProduct2 , 1);
		shoppingCart.addProductToCart(testProduct3 , 2);
		shoppingCart.prepareOrder();
		shoppingCart.resetCard();
		printItems();
		System.out.println("CHANGE: ID:2002 -1 ; ID:2004 -1: ID:2006 -2");
		System.out.println("-----------###### END: TEST 1 ##### ------------");
		
		System.out.println("-----------###### TEST 2 ##### ------------");
		shoppingCart.addProductToCart(testProduct1 , 1);
		shoppingCart.addProductToCart(testProduct2 , 10);
		shoppingCart.addProductToCart(testProduct3 , 1);
		shoppingCart.prepareOrder();
		shoppingCart.resetCard();
		printItems();
		System.out.println("TRANSACTION SHOULD BE ROLLBACKED");
		System.out.println("-----------###### END: TEST 2 ##### ------------");
		
		System.out.println("-----------###### END SHOPPING TEST ##### ------------");
	}
	
	public void newProductTest() {
		System.out.println("-----------###### START NEW PRODUCT TEST ##### ------------");
		System.out.println("-----------###### NAME:TESTNewProduct, CATEGORY: Programy typu office', STOCK: 5  ##### ------------");
		Product newProduct = new Product();
		newProduct.setName("TESTNewProduct");
		newProduct.setCategory(categoryDao.findById(1007L));
		newProduct.setStock(5);
		productDao.create(newProduct);
		System.out.println("-----------###### END NEW CATEGORY TEST ##### ------------");
	}
	
	public void deleteProductTest() {
		System.out.println("-----------###### START DELETE PRODUCT TEST ##### ------------");
		System.out.println("-----------###### DELETE PRODUCT 'Dell Latitude' ##### ------------");
		productDao.deleteById(2003L);
		System.out.println("-----------###### END DELETE PRODUCT TEST ##### ------------");
	}
	
	public void newCategoryTest() {
		System.out.println("-----------###### START NEW CATEGORY TEST ##### ------------");
		System.out.println("-----------###### NAME: TESTcategory, PARENT: 'Oprogramowanie' (ID: 1005) ##### ------------");
		Category newCategory = new Category();
		newCategory.setName("TESTcategory");
		newCategory.setParentCategory(categoryDao.findById(1005L));
		categoryDao.create(newCategory);
		System.out.println("-----------###### END NEW CATEGORY TEST ##### ------------");
	}
	
	public void updateCategoryTest() {
		System.out.println("-----------###### START UPDATE CATEGORY TEST ##### ------------");
		System.out.println("-----------###### CHANGE CATEGORY NAME INTO *** Programy specjalistyczne *** ##### ------------");
		Category category = categoryDao.findById(1008L);
		category.setName("*** Programy specjalistyczne ***");
		categoryDao.update(category);
		System.out.println("-----------###### END UPDATE CATEGORY TEST ##### ------------");
	}
		

}















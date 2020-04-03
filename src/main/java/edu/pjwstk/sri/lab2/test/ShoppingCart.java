package edu.pjwstk.sri.lab2.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import edu.pjwstk.sri.lab2.dao.ProductDao;
import edu.pjwstk.sri.lab2.dto.OrderItem;
import edu.pjwstk.sri.lab2.model.Product;

@Stateful
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ShoppingCart {
	
	@Inject
	private ProductDao productDao;
	
	@Inject
	private OrderItem orderItem;
	
	@Resource
	EJBContext ejbContext;
	
	private List<OrderItem> orderItemList = new ArrayList<OrderItem>();
	private List<Product> productList = new ArrayList<Product>();
	
	public void addProductToCart(Product product, int amount){
		orderItem = new OrderItem();
		orderItem.setProduct(product);
		orderItem.setAmount(amount);
		orderItemList.add(orderItem);
	}
	
	public void removeProductFromCart(Product product) {
		for(int i = 0; i < orderItemList.size(); i++){
			if(orderItemList.get(i).getProduct() == product){
				orderItemList.remove(product);
				break;
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void prepareOrder() {
		productList = productDao.listAll();
		Product boughtProduct, currentProduct;
		int boughtQuantity, currentQuantity, newQuantity;
		
		for(int i = 0; i < orderItemList.size(); i++){
			boughtProduct = orderItemList.get(i).getProduct();
			boughtQuantity = orderItemList.get(i).getAmount();
			currentProduct = productDao.findById(boughtProduct.getId());
			currentQuantity = currentProduct.getStock();
			newQuantity = currentQuantity - boughtQuantity;
			if(newQuantity < 0) {
				ejbContext.setRollbackOnly();
				System.out.println("------###### BRAK TOWARU ###### -------");
				return;
			}
			
			currentProduct.setStock(newQuantity);
			productDao.update(currentProduct);
			System.out.println("------###### UDALO SIE ZMIENIC ILOSC ###### -------");
			
		}
	}
	
	public void resetCard() {
		productList.clear();
	}

}

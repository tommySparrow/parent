package cn.itcast.core.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 购物车实体类
 * 
 * @author Administrator
 *
 */
public class Cart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 购买项的集合
	private List<Item> items = new ArrayList<Item>();

	/**
	 * 自定义添加item到cart中方法，如果item的skuId（id）一样，则item的购买数量加上amount
	 * 
	 * @param item
	 */
	public void addItem(Item item) {

		for (Item item2 : items) {
			if (item2.getSkuId().equals(item.getSkuId())) {
				item2.setAmount(item2.getAmount() + item.getAmount());
				return;
			}
		}

		items.add(item);
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	// 合计等相关信息

	/**
	 * 商品数量
	 * 
	 * @return
	 */
	@JsonIgnore
	public Integer getProductAmount() {
		Integer result = 0;
		for (Item item : items) {
			result = result + item.getAmount();
		}
		return result;
	}

	/**
	 * 获得商品总金额
	 * 
	 * @return
	 */
	@JsonIgnore
	public Float getProductPrice() {
		
		Float result = 0f;

		for (Item item : items) {
			result = result + item.getAmount()
					* Float.parseFloat(item.getSku().get("price").toString());
		}
		return result;
	}
	
	/**
	 * 计算运费
	 * 
	 * @return
	 */
	@JsonIgnore
	public Float getFee() {
		Float result = 0f;
		// 如果商品总金额小于79就收个10块钱，否则就不要钱
		if (this.getProductPrice() < 79) {
			result = 10f;
		}
		return result;
	}
	
	/**
	 * 计算总价格
	 * 
	 * @return
	 */
	@JsonIgnore
	public Float getTotalPrice() {
		return this.getProductPrice() + this.getFee();
	}

}

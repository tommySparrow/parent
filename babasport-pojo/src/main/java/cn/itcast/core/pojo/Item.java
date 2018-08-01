package cn.itcast.core.pojo;

import java.io.Serializable;

/**
 * 购买项
 * 
 * @author Administrator
 *
 */
public class Item implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// skuid (单独提出来方便操作）
	private Long skuId;

	// 变异的复合型的sku
	private SuperPojo sku;

	// 购买数量
	private Integer amount;
	
	//有货无货标识属性
	private Boolean aisHave = true;

	public Boolean getAisHave() {
		return aisHave;
	}

	public void setAisHave(Boolean aisHave) {
		this.aisHave = aisHave;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public SuperPojo getSku() {
		return sku;
	}

	public void setSku(SuperPojo sku) {
		this.sku = sku;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
}

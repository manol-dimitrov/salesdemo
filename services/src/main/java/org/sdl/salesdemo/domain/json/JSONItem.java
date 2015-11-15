
package org.sdl.salesdemo.domain.json;

import java.text.DecimalFormat;

import org.sdl.salesdemo.common.SalesTaxUtil;
import org.sdl.salesdemo.domain.Item;

/**
 * The following class contains the base item details to be displayed to the 
 * user via a JSON object
 * @author shannonlal
 */
public class JSONItem {

    private Long id;
    private String productName;
    private Long productId;
    private String itemPrice;
    private Integer quantity;
    private static final DecimalFormat decimalFormat = SalesTaxUtil.SALES_TAX_NUMBER_FORMAT;
    
    /**
     * Default constructor.  Sets quantity to 0
     */
    public JSONItem(){
        this.quantity =0;
    }
    
    /**
     * The following constructor will convert an item
     * into a JSON Item.  It will also set quantity to 0
     * @param item 
     */
    public JSONItem( Item item){
       this.id = item.getId();
       this.itemPrice = decimalFormat.format( item.getLineTotal() );
       StringBuilder nameBuilder = new StringBuilder( item.getProduct().getName() );
       nameBuilder.append( " @ ");
       nameBuilder.append( item.getProduct().getPrice() );
       this.productName = nameBuilder.toString();
       this.productId = item.getProduct().getId();
       
       this.quantity = item.getQuantity();
    }
    /**
     * 
     * @return long
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 
     * @return String
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 
     * @param productName 
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 
     * @return 
     */
    public Integer getQuantity() {

        return quantity;
    }

    /**
     * 
     * @param quantity 
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 
     * @return 
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * 
     * @param productId 
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * 
     * @return 
     */
    public String getItemPrice() {
        return itemPrice;
    }

    /**
     * 
     * @param itemPrice 
     */
    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Override
    public String toString() {
        return "JSONItem{" + "id=" + id + ", productName=" + productName + ", productId=" + productId + ", itemPrice=" + itemPrice + ", quantity=" + quantity + '}';
    }
    
    

}


package org.sdl.salesdemo.domain.json;

import org.sdl.salesdemo.domain.Product;

/**
 * The following is a JSON object which will represent a
 * product in the system
 * @author shannonlal
 */
public class JSONProduct {

    private static final String YES = "YES";
    private static final String NO = "NO";
    private Long id;
    private String name;
    private String salesTaxExempt;
    private String importTaxExempt;
    
    /**
     * 
     */
    public JSONProduct(){
    }
    
    /**
     * The following method will convert the Product into a JSON
     * object
     * @param product 
     */
    public JSONProduct(Product product ){
       StringBuilder productName = new StringBuilder( product.getName() );
       productName .append( " @ ");
       productName .append( product.getPrice() );
       name = productName.toString();
       id = product.getId();
       
       salesTaxExempt = product.isSalesTaxExempt()? YES : NO;
       importTaxExempt = product.isImportDutiesExempt()? YES : NO;

    }

    /**
     * 
     * @return 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return 
     */
    public String getSalesTaxExempt() {
        return salesTaxExempt;
    }

    /**
     * 
     * @param salesTaxExempt 
     */
    public void setSalesTaxExempt(String salesTaxExempt) {
        this.salesTaxExempt = salesTaxExempt;
    }

    /**
     * 
     * @return 
     */
    public String getImportTaxExempt() {
        return importTaxExempt;
    }

    /**
     * 
     * @param importTaxExempt 
     */
    public void setImportTaxExempt(String importTaxExempt) {
        this.importTaxExempt = importTaxExempt;
    }
}

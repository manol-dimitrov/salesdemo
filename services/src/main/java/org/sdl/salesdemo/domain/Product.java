package org.sdl.salesdemo.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The following class will represent a product which can be sold and tax can be
 * applied. The product will hold the name, price and whether it is exempt from
 * sales tax and import duties
 *
 * @author shannonlal
 */

@Entity
@Table(name = "PRODUCT")
public class Product implements Serializable{

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private BigDecimal price;
    private boolean salesTaxExempt;
    private boolean importDutiesExempt;
   
    public Product(){
        id = 0L;
        price = BigDecimal.ZERO;
    }

    /**
     * 
     * @return 
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
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
    @Column(name = "PRODUCT_NAME", nullable = false, length = 60)
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
    @Column(name = "PRICE", nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the price.  Ensures the decimal places are set
     * @param p 
     */
    public void setPrice(BigDecimal p) {
        p = p.setScale( 2, RoundingMode.HALF_EVEN);
        this.price = p;
    }

    /**
     * 
     * @return 
     */
    @Column(name = "SALES_TAX_EXEMPT", nullable = false)
    public boolean isSalesTaxExempt() {
        return salesTaxExempt;
    }

    /**
     * 
     * @param salesTaxExempt 
     */
    public void setSalesTaxExempt(boolean salesTaxExempt) {
        this.salesTaxExempt = salesTaxExempt;
    }

    /**
     * 
     * @return 
     */
    @Column(name = "IMPORT_DUTIES", nullable = false)
    public boolean isImportDutiesExempt() {
        return importDutiesExempt;
    }

    /**
     * 
     * @param importDutiesExempt 
     */
    public void setImportDutiesExempt(boolean importDutiesExempt) {
        this.importDutiesExempt = importDutiesExempt;
    }

    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name="
                + name + ", price=" + price
                + ", salesTaxExempt=" + salesTaxExempt
                + ", importDutiesExempt=" + importDutiesExempt + '}';
    }

    /**
     * 
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (!(this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
    
}

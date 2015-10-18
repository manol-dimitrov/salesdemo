package org.sdl.salesdemo.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The following class will encapsulate a line item on an order it will contain
 * a product and quantity
 *
 * @author shannonlal
 */
@Entity
@Table(name = "ITEM")
public class Item implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private Product product;
    private int quantity;
    private BigDecimal lineTotal;
    private BigDecimal taxes;

    public Item() {
        this.id = 0L;
        lineTotal = BigDecimal.ZERO;
        taxes = BigDecimal.ZERO;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "PRODUCT_ID", nullable=false)
    public Product getProduct() {
        return product;
    }

    /**
     * 
     * @param product 
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * 
     * @return 
     */
    @Column(name = "QUANTITY", nullable = false)
    public int getQuantity() {
        return quantity;
    }

    /**
     * 
     * @param quantity 
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * 
     * @return 
     */
    @Column(name = "TOTAL", nullable = false)
    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    /**
     * This will set the line total and set the scale
     * to 2
     * @param total 
     */
    public void setLineTotal(BigDecimal total) {
        total = total.setScale(2, RoundingMode.HALF_EVEN);
        this.lineTotal = total;
    }

    /**
     * 
     * @return 
     */
    @Column(name = "TAXES", nullable = false)
    public BigDecimal getTaxes() {
        return taxes;
    }

    /**
     * 
     * @param tax 
     */
    public void setTaxes(BigDecimal tax) {
        tax = tax.setScale( 2, RoundingMode.HALF_EVEN);
        this.taxes = tax;
    }


    /**
     * 
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
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
        final Item other = (Item) obj;
        if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

}

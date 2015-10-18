package org.sdl.salesdemo.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

/**
 * The following domain class represents an order in the sales tax system. It
 * will encapsulate the list of items and the sales tax and total
 *
 * @author shannonlal
 */
@Entity
@Table(name = "ORDERS")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;    
    private Long id;
    private List<Item> items;
    private BigDecimal salesTax;
    private BigDecimal total;

    /**
     * 
     */
    public Order() {
        id = 0L;
        salesTax = BigDecimal.ZERO;
        total = BigDecimal.ZERO;
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
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "ORDER_ITEMS",  joinColumns = {
        @JoinColumn(name = "ORDER_ID")},
            inverseJoinColumns = {
                @JoinColumn(name = "ITEM_ID")})
    @OrderColumn(name="SEQUENCE_ID" )
    public List<Item> getItems() {
        if( items == null ){
            items = new ArrayList<Item>();
        }
        return items;
    }
    
    /**
     * The following method will add an item to 
     * the list 
     * @param item 
     */
    public void addItem( Item item){
        getItems().add( item );
    }

    /**
     * 
     * @param items 
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * 
     * @return 
     */
    @Column(name = "SALES_TAX", nullable = false)
    public BigDecimal getSalesTax() {
        return salesTax;
    }

    /**
     * 
     * @param tax 
     */
    public void setSalesTax(BigDecimal tax) {
        tax = tax.setScale(2, RoundingMode.HALF_EVEN);
        this.salesTax = tax;
    }

    /**
     * 
     * @return 
     */
    @Column(name = "SALES_TOTAL", nullable = false)
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * The following will set the total
     * @param t 
     */
    public void setTotal(BigDecimal t) {
        t = t.setScale( 2, RoundingMode.HALF_EVEN );
        this.total = t;
    }

    /**
     * 
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final Order other = (Order) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}

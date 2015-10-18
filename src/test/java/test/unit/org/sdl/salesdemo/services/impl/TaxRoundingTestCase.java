
package test.unit.org.sdl.salesdemo.services.impl;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Before;
import org.junit.Test;
import org.sdl.salesdemo.common.SalesTaxConstants.Taxes;
import org.sdl.salesdemo.services.TaxProcessingService;
import org.sdl.salesdemo.services.impl.TaxProcessingServiceImpl;


/**
 * The following test case will test the tax rounding calculations.  It will
 * ensure that the tax rounding is in compliance with the requirements
 * 
 * @author shannonlal
 */
public class TaxRoundingTestCase {

    private TaxProcessingService taxProcessingService;
    
    @Before
    public void setUp(){
        taxProcessingService = new TaxProcessingServiceImpl();
    }
    

    @Test
    public void shouldCalculateScenario1SalesTax(){
        
      BigDecimal cd = new BigDecimal(14.99);
      cd = cd.setScale(2, RoundingMode.HALF_EVEN);
      BigDecimal tax = cd.multiply(Taxes.SALES_TAX.getTaxRate());
      tax = tax.setScale( 2, RoundingMode.HALF_EVEN);
      tax = taxProcessingService.calculateRoundedTax(tax);
      
      BigDecimal expTax = new BigDecimal(1.50);
      expTax = expTax.setScale(2, RoundingMode.HALF_EVEN);
      assertEquals( expTax, tax);
      
      BigDecimal expTotal = new BigDecimal( 16.49 );
      expTotal = expTotal.setScale( 2, RoundingMode.HALF_EVEN);
      
      cd = cd.add( tax );
      cd = cd.setScale( 2, RoundingMode.HALF_EVEN);
      
      assertEquals( expTotal, cd);
    }
    
    @Test
    public void shouldCalculateScenario2ImportedChocolateSalesTax(){
        
      BigDecimal cd = new BigDecimal(10.00);
      cd = cd.setScale(2, RoundingMode.HALF_EVEN);
      BigDecimal tax = cd.multiply(Taxes.IMPORT_DUTY_TAX.getTaxRate());
      tax = tax.setScale( 2, RoundingMode.HALF_EVEN);
      tax = taxProcessingService.calculateRoundedTax(tax);
      
      BigDecimal expTax = new BigDecimal(0.50);
      expTax = expTax.setScale(2, RoundingMode.HALF_EVEN);
      assertEquals( expTax, tax);
      
      BigDecimal expTotal = new BigDecimal( 10.50 );
      expTotal = expTotal.setScale( 2, RoundingMode.HALF_EVEN);
      
      cd = cd.add( tax );
      cd = cd.setScale( 2, RoundingMode.HALF_EVEN);
      assertEquals( expTotal, cd);
    }
    
    @Test
    public void shouldCalculateScenario2ImportedPerfume(){
        
      BigDecimal perfume = new BigDecimal(47.50);
      perfume = perfume.setScale(2, RoundingMode.HALF_EVEN);
      BigDecimal importTax = perfume.multiply(Taxes.IMPORT_DUTY_TAX.getTaxRate());
      importTax = importTax.setScale( 2, RoundingMode.HALF_EVEN);
      importTax = taxProcessingService.calculateRoundedTax(importTax);
      
      BigDecimal expTax = new BigDecimal(2.40);
      expTax = expTax.setScale(2, RoundingMode.HALF_EVEN);
      assertEquals( expTax, importTax);
      //2.40 + 4.75
      
      BigDecimal salesTax = perfume.multiply(Taxes.SALES_TAX.getTaxRate());
      salesTax = salesTax.setScale( 2, RoundingMode.HALF_EVEN);
      salesTax = taxProcessingService.calculateRoundedTax(salesTax);
      
      expTax = new BigDecimal(4.75);
      expTax = expTax.setScale(2, RoundingMode.HALF_EVEN);
      assertEquals( expTax, salesTax);
      
      BigDecimal expTotal = new BigDecimal( 54.65 );
      expTotal = expTotal.setScale( 2, RoundingMode.HALF_EVEN);
      
      perfume = perfume.add( salesTax );
      perfume = perfume.add( importTax );
      perfume = perfume.setScale( 2, RoundingMode.HALF_EVEN);
      assertEquals( expTotal, perfume);
    }
    
    @Test
    public void shouldCalculateScenario3Test(){
        //
        BigDecimal importedPerfume = new BigDecimal(27.99);
        importedPerfume = importedPerfume.setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal importTaxImportedPerfume = importedPerfume.multiply(Taxes.IMPORT_DUTY_TAX.getTaxRate());
        importTaxImportedPerfume = importTaxImportedPerfume.setScale( 2, RoundingMode.HALF_EVEN);
        importTaxImportedPerfume = taxProcessingService.calculateRoundedTax(importTaxImportedPerfume);
        
        BigDecimal salesTaxImportedPerfume = importedPerfume.multiply( Taxes.SALES_TAX.getTaxRate());
        salesTaxImportedPerfume = salesTaxImportedPerfume.setScale(2, RoundingMode.HALF_EVEN);
        salesTaxImportedPerfume = taxProcessingService.calculateRoundedTax( salesTaxImportedPerfume);
        importedPerfume = importedPerfume.add( importTaxImportedPerfume );
        importedPerfume = importedPerfume.add( salesTaxImportedPerfume );
        
        BigDecimal exp = new BigDecimal(32.19);
        exp = exp.setScale(2, RoundingMode.HALF_EVEN);
        
        assertEquals( exp, importedPerfume);
        
        BigDecimal perfume = new BigDecimal(18.99);
        perfume = perfume.setScale(2, RoundingMode.HALF_EVEN);
        
        BigDecimal salesTaxPerfume = perfume.multiply( Taxes.SALES_TAX.getTaxRate());
        salesTaxPerfume = salesTaxPerfume.setScale(2, RoundingMode.HALF_EVEN);
        salesTaxPerfume = taxProcessingService.calculateRoundedTax( salesTaxPerfume);
        perfume = perfume.add( salesTaxPerfume );
        perfume = perfume.setScale(2, RoundingMode.HALF_EVEN);
        
        exp = new BigDecimal(20.89);
        exp = exp.setScale(2, RoundingMode.HALF_EVEN);
        assertEquals( exp, perfume);
        
        
    }

}

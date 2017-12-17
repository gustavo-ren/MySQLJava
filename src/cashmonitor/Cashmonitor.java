package cashmonitor;

import java.sql.SQLException;
import java.sql.Date;
/**
 *
 * @author Gustavo
 */
public class Cashmonitor {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
        
        Date initialDate = Date.valueOf("2017-01-01");
        Date endDate = Date.valueOf("2017-12-31");
        
        Sale sale = new Sale();
        sale.printReport(initialDate, endDate);

    }
    
}

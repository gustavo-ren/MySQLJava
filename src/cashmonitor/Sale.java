package cashmonitor;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

/**
 *
 * @author Gustavo
 */
public class Sale {

    private String store;
    private float visa;
    private float mastercard;
    private float diners;
    private float amex;
    private float total;

    public Sale(String store, float visa, float mastercard,
            float diners, float amex, float total) {
        this.store = store;
        this.visa = visa;
        this.mastercard = mastercard;
        this.diners = diners;
        this.amex = amex;
        this.total = total;
    }

    public Sale() {
    }
    
    /**
     * Method used to print report data
     *
     * @param initialDate
     * @param endDate
     * @throws java.sql.SQLException
     */
    public void printReport(Date initialDate, Date endDate) throws SQLException {

        //Creation of the Sale object list and invocation of the getSales method
        List<Sale> sales = getSales(initialDate, endDate);
        
        //Lambda expression to print the results
        sales.forEach((sale) -> {
            System.out.println(sale.toString());
        });
    }

    /**
     * Method used to get sales data from MySQL
     *
     * @param initialDate
     * @param endDate
     * @throws java.sql.SQLException
     * @return
     */
    private List getSales(Date initialDate, Date endDate) throws SQLException {
        //Creation of the Connection object
        Connection con;
        //Creation of the object responsable to create the connection
        ConnectionFactory cf = new ConnectionFactory();
        //List to be returned
        List<Sale> sales = new ArrayList<>();

        //Establishment of connection with the database
        con = cf.getConnection();
        //Query to be performed
        PreparedStatement statement = con.prepareStatement("SELECT st.name as Store,\n"
                + "COALESCE(SUM(case when cc.name = 'Visa' then sa.value end), 0) as Visa,\n"
                + "COALESCE(SUM(case when cc.name = 'Mastercard' then sa.value end), 0) as Master,\n"
                + "COALESCE(SUM(case when cc.name = 'Diners' then sa.value end), 0) as Diners,\n"
                + "COALESCE(SUM(case when cc.name = 'Amex' then sa.value end), 0) as Amex,\n"
                + "COALESCE(sum(sa.value), 0) as Total \n"
                + "FROM sales as sa left join credit_card as cc on (sa.credit_card_id = cc.id) left join store as st on (sa.store_id = st.id)\n"
                + "where sa.date BETWEEN (?) AND (?) GROUP BY st.name;");

        //Sending date param to query
        statement.setDate(1, initialDate);
        statement.setDate(2, endDate);

        //Execution of query
        ResultSet resultSet = statement.executeQuery();

        //Creation of Sale objects and addition on the List object
        while (resultSet.next()) {
            sales.add(new Sale(resultSet.getString(1), resultSet.getFloat(2), resultSet.getFloat(3),
            resultSet.getFloat(4), resultSet.getFloat(5), resultSet.getFloat(6)));
        }
        
        //Closing connection
        cf.closeConnection();
        return sales;
    }

    /**
     * Override of the toString method
     * it formats the data in the required format
     * @return
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        return (this.store + ";" + df.format(this.visa) + ";" +
                df.format(this.mastercard) + ";" + df.format(this.diners)+ ";" +
                df.format(this.amex) + ";" + df.format(this.total)).replace(',', '.');
    }
}

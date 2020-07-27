package classes;

import controllers.MainWindowController;
import database.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UpdateTextFieldInDB {

    private MainWindowController mainWindowController;

    private String previousTextField;

    public UpdateTextFieldInDB(MainWindowController newMainTestController){
        this.mainWindowController = newMainTestController;
    }

    public void updateTextField() {

        String currentTextField = mainWindowController.getUserTextField();

        if(previousTextField.isEmpty() || previousTextField!=currentTextField) {

            Session session2 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            Transaction transaction2 = session2.beginTransaction();
            Query newQuery1 = session2.createQuery("UPDATE User SET textfield = "
                    + "'" + currentTextField + "'"
                    + " WHERE id = " + mainWindowController.getUserId());
            newQuery1.executeUpdate();
            transaction2.commit();
            session2.close();

            previousTextField = currentTextField;
        }

    }
}

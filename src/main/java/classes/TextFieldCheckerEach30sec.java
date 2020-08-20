package classes;

import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

import controllers.MainWindowController;
import database.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class TextFieldCheckerEach30sec {

    private static final long JOB_START_DELAY = 0L;
    private static final long JOB_FREQUENCY = 1000L * 30L;

    private volatile static TextFieldCheckerEach30sec timerSingleton;
    private volatile static boolean isRunning;
    private volatile static TimerTaskJob task;

    private volatile static TimerTaskJob timerTask;
    private volatile static Timer timer;

    private volatile static MainWindowController mainWindowController;

    private TextFieldCheckerEach30sec() {}

    private TextFieldCheckerEach30sec(MainWindowController mainWindowController) {
        TextFieldCheckerEach30sec.mainWindowController = mainWindowController;
    }

    public static TextFieldCheckerEach30sec initialize(MainWindowController mainWindowController) {
        if (timerSingleton == null) {

            synchronized (TextFieldCheckerEach30sec.class) {

                if (timerSingleton == null) {
                    timerSingleton = new TextFieldCheckerEach30sec(mainWindowController);
                    task = timerSingleton.new TimerTaskJob();
                    isRunning = false;
                }

            }

        }

        return timerSingleton;
    }

    public static void start() {

        if (!isRunning) {
            isRunning = true;
            task.start();
        }

    }

    public static void stop() {
        if (isRunning) {
            isRunning = false;
            task.stop();
        }
    }

    public static boolean isRunning() {
        return isRunning;
    }


    private class TimerTaskJob extends TimerTask {

        private volatile String currentTextField = "";
        private volatile String previousTextField = "";
        private int counter = 0;

        @Override
        public void run() {
            doTask();
        }

        void start() {
            timerTask = new TimerTaskJob();
            timer = new Timer(true);
            timer.scheduleAtFixedRate(timerTask, JOB_START_DELAY, JOB_FREQUENCY);
        }

        void stop() {
            timerTask.cancel();
            timer.cancel();
        }

        private void doTask() {
            currentTextField = mainWindowController.getTextField();

            if(counter==0){
                previousTextField = currentTextField;
            }
            counter++;


            if (previousTextField.equals(currentTextField)) {
            } else {
                this.updateTextField();
            }

        }

        public void updateTextField() {
            Session session2 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            Transaction transaction2 = session2.beginTransaction();
            Query newQuery1 = session2.createQuery("UPDATE User SET textfield = "
                    + "'" + currentTextField + "'"
                    + " WHERE id = " + mainWindowController.getUserId());
            newQuery1.executeUpdate();
            transaction2.commit();
            session2.close();
            previousTextField = currentTextField;
            mainWindowController.getUser().setTextfield(currentTextField);
            System.out.println("Textfield has been updated!");
        }

    }

}
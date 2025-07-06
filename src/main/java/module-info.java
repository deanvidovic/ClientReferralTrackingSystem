module hr.clientreferraltrackingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    requires jbcrypt;


    exports hr.clientreferraltrackingsystem.main;
    opens hr.clientreferraltrackingsystem.main to javafx.fxml;
    exports hr.clientreferraltrackingsystem.controller;
    opens hr.clientreferraltrackingsystem.controller to javafx.fxml;
    opens hr.clientreferraltrackingsystem.controller.admin to javafx.fxml;
    opens hr.clientreferraltrackingsystem.controller.user to javafx.fxml;
    opens hr.clientreferraltrackingsystem.controller.menu to javafx.fxml;
    opens hr.clientreferraltrackingsystem.controller.admin.helper to javafx.fxml;

}
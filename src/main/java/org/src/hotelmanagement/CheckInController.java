package org.src.hotelmanagement;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class CheckInController implements Initializable {
    @FXML
    private DatePicker checkInDate;

    @FXML
    private StackPane checkInForm;

    @FXML
    private DatePicker checkOutDate;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phone;

    @FXML
    private Label priceLabel;

    @FXML
    private Label roomLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label total;

    @FXML
    private Label totalDays;

    private Stage stage;
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public void informationClear() {
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        phone.setText("");
        checkInDate.setValue(null);
        checkOutDate.setValue(null);
        totalDays.setText("---");
        total.setText("$0.0");
    }

    public void setRoomInfo(String room, String type, String price, String status) {
        roomLabel.setText(room);
        typeLabel.setText(type);
        priceLabel.setText(price);
        statusLabel.setText(status);
    }

    public void customerNumber(){
        conn = org.src.hotelmanagement.sql.JDBCUtil.getConnection();
        String query = "SELECT COUNT(*) FROM customer";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                getData.customerNum = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void customerCheckIn(){
        String insertCustomer = "INSERT INTO customer (customer_id, roomType, roomNumber, firstName, lastName, phoneNumber, email, checkIn, checkOut)" +
                " VALUES (?,?,?,?,?,?,?,?,?)";
        conn = org.src.hotelmanagement.sql.JDBCUtil.getConnection();
        Alert alert;
        if (firstName.getText().isEmpty() || lastName.getText().isEmpty() || phone.getText().isEmpty() || email.getText().isEmpty() || checkInDate.getValue() == null || checkOutDate.getValue() == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Check In");
            alert.setHeaderText("Check In Failed");
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
        }else if(isRoomAvailable(conn, roomLabel.getText(), checkInDate.getValue(), checkOutDate.getValue())) {
                try {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Check In");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure?");
                    customerNumber();
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        ps = conn.prepareStatement(insertCustomer);
                        ps.setInt(1, getData.customerNum + 1);
                        ps.setString(2, typeLabel.getText());
                        ps.setString(3, roomLabel.getText());
                        ps.setString(4, firstName.getText());
                        ps.setString(5, lastName.getText());
                        ps.setString(6, phone.getText());
                        ps.setString(7, email.getText());
                        ps.setString(8, checkInDate.getValue().toString());
                        ps.setString(9, checkOutDate.getValue().toString());
                        ps.executeUpdate();
                        String insertCustomerReceipt = "INSERT INTO customer_receipt (customer_num,total,date,date_receipt) VALUES (?,?,?,?)";
                        ps = conn.prepareStatement(insertCustomerReceipt);
                        ps.setInt(1, getData.customerNum + 1);
                        ps.setDouble(2, Double.parseDouble(total.getText().substring(1)));
                        ps.setDouble(3, Double.parseDouble(totalDays.getText()));
                        ps.setString(4, LocalDate.now().toString());
                        ps.executeUpdate();
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Check In");
                        alert.setHeaderText(null);
                        alert.setContentText("Check In Successful");
                        alert.showAndWait();
                        informationClear();
                    }else{
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Check In");
            alert.setHeaderText("Check In Failed");
            alert.setContentText("Room is not available");
            alert.showAndWait();
        }
    }

    //phương thức kiểm tra trùng lịch đặt phòng
    public boolean isRoomAvailable(Connection conn, String room, LocalDate checkIn, LocalDate checkOut) {
        String query = "SELECT COUNT(*) FROM customer WHERE roomNumber = ? " +
                "AND ((checkIn <= ? AND checkOut >= ?) " +
                "OR (checkIn <= ? AND checkOut >= ?) " +
                "OR (checkIn >= ? AND checkOut <= ?))";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, room);
            ps.setDate(2, Date.valueOf(checkOut));
            ps.setDate(3, Date.valueOf(checkIn));
            ps.setDate(4, Date.valueOf(checkOut));
            ps.setDate(5, Date.valueOf(checkIn));
            ps.setDate(6, Date.valueOf(checkIn));
            ps.setDate(7, Date.valueOf(checkOut));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void close() {
        stage.close();
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }
    private float totalP = 0;
    public void totalDaysAndTotalMoney(){
        if(checkInDate.getValue() != null && checkOutDate.getValue() != null) {
            if (checkInDate.getValue().isAfter(LocalDate.now())||checkInDate.getValue().isEqual(LocalDate.now())) {
                if (checkOutDate.getValue().isAfter(checkInDate.getValue())) {
                    LocalDate checkIn = checkInDate.getValue();
                    LocalDate checkOut = checkOutDate.getValue();
                    double days = ChronoUnit.DAYS.between(checkIn, checkOut);
                    totalDays.setText(String.valueOf(days));
                    totalP = (float) (days * Double.parseDouble(priceLabel.getText()));
                    total.setText("$" + String.valueOf(totalP));
                } else {
                    Alert alert;
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Check In");
                    alert.setHeaderText("Check In Failed");
                    alert.setContentText("Check Out date must be after Check In date");
                    alert.showAndWait();
                }
            } else {
                Alert alert;
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Check In");
                alert.setHeaderText("Check In Failed");
                alert.setContentText("Check In date must be after today");
                alert.showAndWait();
            }
        }
    }


    public void receipt(){
        HashMap hash = new HashMap();
        hash.put("hotelParameter", getData.customerNum + 1);
        try {
            if(totalP > 0){
                JasperDesign jDesign = JRXmlLoader.load("D:\\Java Project With IntelliJ\\HotelManagement\\src\\main\\resources\\org\\src\\hotelmanagement\\Report\\print.jrxml");
                JasperReport jReport = JasperCompileManager.compileReport(jDesign);
                JasperPrint jPrint = JasperFillManager.fillReport(jReport, hash, conn);

                JasperViewer.viewReport(jPrint, false);
            }else{
                Alert alert;
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Check In");
                alert.setHeaderText("null");
                alert.setContentText("Invalid Receipt");
                alert.showAndWait();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

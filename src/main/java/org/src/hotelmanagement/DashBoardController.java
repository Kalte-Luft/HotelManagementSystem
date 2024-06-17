package org.src.hotelmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.src.hotelmanagement.sql.JDBCUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {
    //menuForm
    @FXML
    private ImageView avtImage;
    @FXML
    private Button btnLogout;
    @FXML
    private Button zoomout;
    @FXML
    private SplitPane menu;
    @FXML
    private Button zoomin;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Label nameLB;

    //dashboardForm
    @FXML
    private Label incomePerMonthCard;
    @FXML
    private Label totalIncomeCard;
    @FXML
    private Label todayIncomeCard;
    @FXML
    private Label customerNumCard;
    @FXML
    private BarChart<?, ?> dashboard_IPM_chart;
    @FXML
    private AreaChart<?, ?> dashboard_NC_chart;
    @FXML
    private LineChart<?, ?> dashboard_IPD_chart;
    @FXML
    private AnchorPane dashboardForm;
    @FXML
    private AnchorPane tableRoomForm;
    @FXML
    private TableView<RoomData> tableRoom;
    @FXML
    private TableColumn<RoomData,String> colPrice;
    @FXML
    private TableColumn<RoomData,String> colRoom;
    @FXML
    private TableColumn<RoomData,String> colRoomType;
    @FXML
    private TableColumn<RoomData,String> colStatus;
    @FXML
    private ScrollPane orderForm;
    @FXML
    private GridPane grid1;
    @FXML
    private GridPane grid2;
    @FXML
    private GridPane grid3;
    @FXML
    private GridPane grid4;
    @FXML
    private TextField searchTextField;
    @FXML
    private ComboBox<String> typeSearch;
    @FXML
    private ComboBox<String> statusSearch;

    //customerForm
    @FXML
    private AnchorPane customerForm;
    @FXML
    private TableColumn<CustomerData, String> colCheckIn;
    @FXML
    private TableColumn<CustomerData, String> colCheckOut;
    @FXML
    private TableColumn<CustomerData, String> colCusNum;
    @FXML
    private TableColumn<CustomerData, String> colEmail;
    @FXML
    private TableColumn<CustomerData, String> colFirstName;
    @FXML
    private TableColumn<CustomerData, String> colLastName;
    @FXML
    private TableColumn<CustomerData, String> colPhone;
    @FXML
    private TableColumn<CustomerData, String> colCusRoom;
    @FXML
    private TableView<CustomerData> customerTb;
    @FXML
    private TextField emailTF;
    @FXML
    private TextField firstNameTF;
    @FXML
    private TextField lastNameTF;
    @FXML
    private TextField phoneTF;
    @FXML
    private TextField searchTF;
    @FXML
    private DatePicker checkInDate;
    @FXML
    private DatePicker checkOutDate;
    @FXML
    private TextField roomTF;

    //accountForm
    @FXML
    private TextField confirmPasswordTF;
    @FXML
    private TextField newPasswordTF;
    @FXML
    private TextField oldPasswordTF;
    @FXML
    private TextField renameTF;
    @FXML
    private AnchorPane accountForm;
    

    private Stage stage;
    private double x = 0;
    private double y = 0;

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    private Stage checkInStage;
    private CheckInController checkInController;
    //dashboardForm
    public void displayNumberOfCustomer(){
        int noc = 0;
        String sql = "SELECT COUNT(id) FROM customer_receipt";
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                noc = rs.getInt("COUNT(id)");
            }
            customerNumCard.setText(String.valueOf(noc));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void displayIncomeToday(){
        double income = 0;
        Date date = new Date(System.currentTimeMillis());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String sql = "SELECT SUM(total) FROM customer_receipt WHERE date_receipt = '"+sqlDate+"'";
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                income = rs.getDouble("SUM(total)");
            }
            todayIncomeCard.setText(String.valueOf("$"+income));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void displayTotalIncome(){
        double income = 0;
        String sql = "SELECT SUM(total) FROM customer_receipt";
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                income = rs.getDouble("SUM(total)");
            }
            totalIncomeCard.setText(String.valueOf("$"+income));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //phương thức hiển thị tổng doanh thu trong tháng này
    public void displayTotalIncomeThisMonth(){
        double income = 0;
        Date date = new Date(System.currentTimeMillis());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String sql = "SELECT SUM(total) FROM customer_receipt WHERE MONTH(date_receipt) = MONTH('"+sqlDate+"')";
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                income = rs.getDouble("SUM(total)");
            }
            incomePerMonthCard.setText(String.valueOf("$"+income));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void displayNOCChart(){
        dashboard_NC_chart.getData().clear();
        String sql = "SELECT date_receipt, COUNT(id) FROM customer_receipt GROUP BY TIMESTAMP(date_receipt) ORDER BY date_receipt DESC LIMIT 10";
        try {
            XYChart.Series chart = new XYChart.Series<>();
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                chart.getData().add(new XYChart.Data<>(rs.getString(1), rs.getInt(2)));
            }
            dashboard_NC_chart.getData().add(chart);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //hiển thị lên bảng IPM doanh thu trong ngày
    public void displayIPDChart(){
        dashboard_IPD_chart.getData().clear();
        String sql = "SELECT date_receipt, SUM(total) FROM customer_receipt GROUP BY TIMESTAMP(date_receipt) ORDER BY date_receipt DESC LIMIT 10";
        try {
            XYChart.Series chart = new XYChart.Series<>();
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                chart.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(2)));
            }
            dashboard_IPD_chart.getData().add(chart);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void displayIPMChart(){
        dashboard_IPM_chart.getData().clear();
        String sql = "SELECT DATE_FORMAT(date_receipt, '%Y-%m') AS month_receipt, SUM(total) FROM customer_receipt GROUP BY month_receipt ORDER BY month_receipt DESC LIMIT 10";
        try {
            XYChart.Series chart = new XYChart.Series<>();
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                chart.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(2)));
            }
            dashboard_IPM_chart.getData().add(chart);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void collapseMenu() {
        if (!menuPane.isVisible()) {
            menuPane.setVisible(true);
            menu.setVisible(true);
            zoomin.setVisible(false);
            zoomout.setVisible(true);
        } else {
            menuPane.setVisible(false);
            menu.setVisible(false);
            zoomin.setVisible(true);
            zoomout.setVisible(false);
        }
    }

    public void extendTable(){
        if(tableRoomForm.isVisible()){
            tableRoomForm.setVisible(false);
        }else{
            tableRoomForm.setVisible(true);
        }
    }

    @FXML
    public void onBtnDashboard(){
        if(!dashboardForm.isVisible()){
            dashboardForm.setVisible(true);
        }
        updateChart();
        orderForm.setVisible(false);
        tableRoomForm.setVisible(false);
        customerForm.setVisible(false);
        accountForm.setVisible(false);
        collapseMenu();
    }

    @FXML
    public void onBtnOrder(){
        if(!orderForm.isVisible()){
            orderForm.setVisible(true);
        }
            dashboardForm.setVisible(false);
            tableRoomForm.setVisible(false);
            customerForm.setVisible(false);
            accountForm.setVisible(false);
        collapseMenu();
    }

    @FXML
    public void onBtnCustomer(){
        if(!customerForm.isVisible()){
            customerForm.setVisible(true);
        }
            dashboardForm.setVisible(false);
            tableRoomForm.setVisible(false);
            orderForm.setVisible(false);
            accountForm.setVisible(false);
        collapseMenu();
    }
    @FXML
    public void onBtnAccount(){
        if(!accountForm.isVisible()){
            accountForm.setVisible(true);
        }
            dashboardForm.setVisible(false);
            tableRoomForm.setVisible(false);
            orderForm.setVisible(false);
            customerForm.setVisible(false);
    }
    public void exit() {
        System.exit(0);
    }

    public void minimize() {
        stage.setIconified(true);
    }

    private boolean isMaximized = false;

    public void maximize() {
        if (isMaximized) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
        }
        isMaximized = !isMaximized;
    }
    //lấy thông tin phòng từ database
    public ObservableList<RoomData> getRoomList() {
        ObservableList<RoomData> roomList = FXCollections.observableArrayList();

        try {
            String query = "SELECT * FROM room";
            conn = JDBCUtil.getConnection();
            RoomData room;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                room = new RoomData(rs.getInt("roomNumber")
                        , rs.getString("type")
                        , rs.getString("status")
                        , rs.getString("price"));
                roomList.add(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomList;
    }

    private ObservableList<RoomData> roomList ;

    //hiển thị thông tin phòng vào bảng
    public void setRoomTable(){
        roomList = getRoomList();
        colRoom.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableRoom.setItems(roomList);
    }

    public void switchToLogin() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");

        // Optional: Customize the buttons
        ButtonType logoutButton = new ButtonType("Log Out");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(logoutButton, cancelButton);

        // Show the dialog and wait for the user's response
        alert.showAndWait().ifPresent(response -> {
            if (response == logoutButton) {
                // User chose to log out
                try {
                    btnLogout.getScene().getWindow().hide();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    root.setOnMousePressed(event -> {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });
                    root.setOnMouseDragged(event -> {
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                    });
                    stage.setScene(scene);
                    LoginController controller = loader.getController();
                    controller.setStage(stage);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public RoomData getRoom(int roomNumber){
        RoomData room = null;
        try{
            conn = JDBCUtil.getConnection();
            String query = "SELECT * FROM room WHERE roomNumber = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, roomNumber);
            rs = ps.executeQuery();
            if(rs.next()){
                room = new RoomData(rs.getInt("roomNumber"), rs.getString("type"), rs.getString("status"), rs.getString("price"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return room;
    }

    @FXML
    public void checkIn(){
        try {
            if (checkInStage == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("checkIn.fxml"));
                Parent root = loader.load();
                checkInController = loader.getController();
                checkInStage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed(event -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });
                root.setOnMouseDragged(event -> {
                    checkInStage.setX(event.getScreenX() - x);
                    checkInStage.setY(event.getScreenY() - y);
                    checkInStage.setOpacity(0.8f);
                });
                root.setOnMouseReleased(event -> {
                    checkInStage.setOpacity(1.0f);
                });

                checkInStage.setScene(scene);
                checkInController.setStage(checkInStage);
                checkInStage.initStyle(StageStyle.TRANSPARENT);

            }
            checkInStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setStage(Stage stage){
        this.stage = stage;
    }
    public void onRoomButtonClicked(ActionEvent actionEvent) {

        Button button = (Button) actionEvent.getSource();
        String btnText = button.getText();
        String roomNumber = btnText.substring(5);
        RoomData room = getRoom(Integer.parseInt(roomNumber));

        if (room != null) {
            checkIn();
            checkInController.setRoomInfo(
                    String.valueOf(room.getRoomNumber()),
                    room.getRoomType(),
                    room.getPrice(),
                    room.getStatus()
            );
            checkInStage.toFront();
        }
    }



    public void onBtnChangeAvt(){
        // Change avatar
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            //lưu ảnh vào database
            try {
                conn = JDBCUtil.getConnection();
                String query = "UPDATE admin SET avatar = ? WHERE username = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, imagePath);
                ps.setString(2, LoginController.currentUser);
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Image image = new Image(imagePath);
            avtImage.setImage(image);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Change Avatar");
            alert.setHeaderText(null);
            alert.setContentText("Avatar changed successfully");
            alert.showAndWait();
        } else {
            System.out.println("No file selected");
        }
    }

    //phương thức hiển thị ảnh đại diện khi đăng nhập
    public void setAvtImage(){
        try {
            conn = JDBCUtil.getConnection();
            String query = "SELECT avatar FROM admin WHERE username = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, LoginController.currentUser);
            rs = ps.executeQuery();
            if (rs.next()) {
                String imagePath = rs.getString("avatar");
                Image image = new Image(imagePath);
                avtImage.setImage(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //phương thức cập nhật trạng thái phòng trong database
    public void updateRoomStatusInDB(){
        LocalDate today = LocalDate.now();
        String checkQuery = "SELECT roomNumber FROM customer WHERE ? BETWEEN checkIn AND checkOut";
        String updateOccupiedQuery = "UPDATE room SET status = 'occupied' WHERE roomNumber = ?";
        String updateAvailableQuery = "UPDATE room SET status = 'available' WHERE roomNumber " +
                "NOT IN (SELECT roomNumber FROM customer WHERE ? BETWEEN checkIn AND checkOut)";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement updateOccupied = conn.prepareStatement(updateOccupiedQuery);
             PreparedStatement updateAvailable = conn.prepareStatement(updateAvailableQuery)) {

            // Kiểm tra phòng nào cần set thành 'occupied'
            checkStmt.setDate(1, Date.valueOf(today));
            ResultSet rs = checkStmt.executeQuery();

            while (rs.next()) {
                String roomNumber = rs.getString("roomNumber");
                updateOccupied.setString(1, roomNumber);
                updateOccupied.executeUpdate();
            }

            // Cập nhật tất cả các phòng không có trong kết quả truy vấn trên thành 'available'
            updateAvailable.setDate(1, Date.valueOf(today));
            updateAvailable.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //phương thức cập nhật ảnh phòng
    private Image getImageForRoom(RoomData roomData){
        updateRoomStatusInDB();
        String roomType = roomData.getRoomType().replaceAll("\\s+", "").toLowerCase();
        String status = roomData.getStatus().toLowerCase();
        String imageName = roomType +(status.equals("available") ? "-empty.png" : ".png");
        URL imageUrl = getClass().getResource("/org/src/hotelmanagement/Images/"+imageName);
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image not found: /images/" + imageName);
        }
        return new Image(imageUrl.toExternalForm());
    }
    //phương thức duyệt nút phòng
    private void updateRoomButtons(GridPane gridPane, ObservableList<RoomData> roomList){
        for(Node node: gridPane.getChildren()){
            if(node instanceof Button){
                Button roomButton = (Button) node;
                String roomNumber = roomButton.getText().substring(5);
                for(RoomData room: roomList){
                    if(room.getRoomNumber() == Integer.parseInt(roomNumber)){
                        roomButton.setGraphic(new ImageView(getImageForRoom(room)));
                    }
                }
            }
        }
    }

    //phương thức cập nhật trạng thái phòng trên giao diện
    public void updateRoomStatus(){
        ObservableList<RoomData> roomList = getRoomList();
        updateRoomButtons(grid1, roomList);
        updateRoomButtons(grid2, roomList);
        updateRoomButtons(grid3, roomList);
        updateRoomButtons(grid4, roomList);
    }


    //phương thức reset lại view phòng
    public void resetRoomView() {
        // List of all GridPanes
        clearSearch();
        updateRoomStatus();
        //updateRoomStatusAfterCheckOut();
        List<GridPane> gridPanes = Arrays.asList(grid1, grid2, grid3, grid4);

        // Make all buttons in all GridPanes visible
        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                    node.setVisible(true);
                }
            }
        }
    }

    public void clearSearch() {
        searchTextField.clear();
        typeSearch.getSelectionModel().clearSelection();
        statusSearch.getSelectionModel().clearSelection();
    }

    //phương thức tìm kiếm phòng theo số phòng rồi lọc ra các nút trên gridpane
    public void searchRoom() {
        String roomNumber = searchTextField.getText();

        // List of all GridPanes
        List<GridPane> gridPanes = Arrays.asList(grid1, grid2, grid3, grid4);

        // Hide all buttons in all GridPanes
        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                    node.setVisible(false);
                }
            }
        }

        // Find the button with the matching room number and make it visible
        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    if (button.getText().equals("Room " + roomNumber)) {
                        button.setVisible(true);
                        break;
                    }
                }
            }
        }
    }

    //phương thức tim kiếm theo loại phòng và trạng thái
    public void searchRoomByTypeAndStatus() {
        String type = typeSearch.getValue();
        String status = statusSearch.getValue();

        // List of all GridPanes
        List<GridPane> gridPanes = Arrays.asList(grid1, grid2, grid3, grid4);

        // Hide all buttons in all GridPanes
        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                    node.setVisible(false);
                }
            }
        }

        // Find the button with the matching room type and/or status and make it visible
        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    String roomNumber = button.getText().substring(5);
                    RoomData room = getRoom(Integer.parseInt(roomNumber));
                    if (type != null && status != null) {
                        // If both type and status are selected, match both
                        if (room.getRoomType().equals(type) && room.getStatus().equals(status)) {
                            button.setVisible(true);
                        }
                    } else if (type != null) {
                        // If only type is selected, match type
                        if (room.getRoomType().equals(type)) {
                            button.setVisible(true);
                        }
                    } else if (status != null) {
                        // If only status is selected, match status
                        if (room.getStatus().equals(status)) {
                            button.setVisible(true);
                        }
                    }
                }
            }
        }
    }

    //phương thức thêm items vào combobox

    public void addItemsToComboBoxes() {
        // Thêm các loại phòng vào ComboBox typeSearch
        typeSearch.getItems().addAll("Single Room", "Double Room", "Triple Room", "Vip Room");

        // Thêm các trạng thái vào ComboBox statusSearch
        statusSearch.getItems().addAll("available", "occupied");
    }

    //phương thức lấy thông tin khách hàng từ database
    public ObservableList<CustomerData> getCustomerData(){
        ObservableList<CustomerData> customerList = FXCollections.observableArrayList();
        try{
            conn = JDBCUtil.getConnection();
            String query = "SELECT * FROM customer";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            CustomerData customer;
            while(rs.next()){
                customer = new CustomerData(rs.getString("customer_id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("phoneNumber"), rs.getString("email"), rs.getDate("checkIn"), rs.getDate("checkOut"), rs.getString("roomNumber"));
                customerList.add(customer);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return customerList;
    }
    //phương thức hiển thị thông tin khách hàng vào bảng
    public void setCustomerTable(){
        ObservableList<CustomerData> customerList = getCustomerData();
        colCusNum.setCellValueFactory(new PropertyValueFactory<>("num"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        colCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        colCusRoom.setCellValueFactory(new PropertyValueFactory<>("room"));
        customerTb.setItems(customerList);
    }
    //phương thức chọn hàng trên bảng và hiển thị trên textfield
    public void selectCustomer(){
        CustomerData customer = customerTb.getSelectionModel().getSelectedItem();
        if(customer != null){
            firstNameTF.setText(customer.getFirstName());
            lastNameTF.setText(customer.getLastName());
            phoneTF.setText(customer.getPhone());
            emailTF.setText(customer.getEmail());
            checkInDate.setValue(LocalDate.parse(customer.getCheckInDate().toString()));
            checkOutDate.setValue(LocalDate.parse(customer.getCheckOutDate().toString()));
            roomTF.setText(customer.getRoom());
        }
    }
    //phương thức clear textfield
    public void clearCustomer(){
        firstNameTF.clear();
        lastNameTF.clear();
        phoneTF.clear();
        emailTF.clear();
        checkInDate.setValue(null);
        checkOutDate.setValue(null);
        roomTF.clear();
    }
    //phương thức thêm khách hàng
    public void addCustomer(){
        try{
            LocalDate checkIn = checkInDate.getValue();
            LocalDate checkOut = checkOutDate.getValue();
            String roomType = getRoom(Integer.parseInt(roomTF.getText())).getRoomType();
            if( roomTF.getText().isEmpty() || firstNameTF.getText().isEmpty() || lastNameTF.getText().isEmpty() || phoneTF.getText().isEmpty() || emailTF.getText().isEmpty() || checkInDate.getValue() == null || checkOutDate.getValue() == null ){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Customer");
                alert.setHeaderText("Add Customer Failed");
                alert.setContentText("Please fill in all fields");
                alert.showAndWait();
            }else if(checkIn.isBefore(checkOut)){
                conn = JDBCUtil.getConnection();
                String query = "INSERT INTO customer (customer_id,firstName, lastName, phoneNumber, email, checkIn, checkOut, roomNumber,roomType) VALUES (?,?,?,?,?,?,?,?,?)";
                ps = conn.prepareStatement(query);
                ps.setInt(1, customerTb.getItems().size() + 1);
                ps.setString(2, firstNameTF.getText());
                ps.setString(3, lastNameTF.getText());
                ps.setString(4, phoneTF.getText());
                ps.setString(5, emailTF.getText());
                ps.setDate(6, Date.valueOf(checkInDate.getValue()));
                ps.setDate(7, Date.valueOf(checkOutDate.getValue()));
                ps.setString(8, roomTF.getText());
                ps.setString(9, roomType);
                ps.executeUpdate();
                setCustomerTable();
                long days = ChronoUnit.DAYS.between(checkIn, checkOut);
                double totalP = days * Double.parseDouble(getRoom(Integer.parseInt(roomTF.getText())).getPrice());
                //tự động cập nhật customer_receipt khi thêm khách hàng
                String insertCustomerReceipt = "INSERT INTO customer_receipt (customer_num,total,date,date_receipt) VALUES (?,?,?,?)";
                ps = conn.prepareStatement(insertCustomerReceipt);
                ps.setInt(1, customerTb.getItems().size());
                ps.setDouble(2, totalP);
                ps.setDouble(3, days);
                ps.setDate(4, Date.valueOf(checkInDate.getValue()));
                ps.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Add Customer");
                alert.setHeaderText(null);
                alert.setContentText("Customer added successfully");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Check In");
                alert.setHeaderText("Check In Failed");
                alert.setContentText("Check Out date must be after Check In date");
                alert.showAndWait();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //phương thức cập nhật thông tin khách hàng
    public void updateCustomer(){
        try{
            LocalDate checkIn = checkInDate.getValue();
            LocalDate checkOut = checkOutDate.getValue();
            String roomType = getRoom(Integer.parseInt(roomTF.getText())).getRoomType();
            int customerNum = Integer.parseInt(customerTb.getSelectionModel().getSelectedItem().getNum());

            if( roomTF.getText().isEmpty() || firstNameTF.getText().isEmpty() || lastNameTF.getText().isEmpty() || phoneTF.getText().isEmpty() || emailTF.getText().isEmpty() || checkInDate.getValue() == null || checkOutDate.getValue() == null ){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Update Customer");
                alert.setHeaderText("Update Customer Failed");
                alert.setContentText("Please fill in all fields");
                alert.showAndWait();
            }else if(checkIn.isBefore(checkOut)){
                conn = JDBCUtil.getConnection();
                String query = "UPDATE customer SET firstName = ?, lastName = ?, phoneNumber = ?, email = ?, checkIn = ?, checkOut = ?, roomNumber = ?, roomType = ? WHERE customer_id = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, firstNameTF.getText());
                ps.setString(2, lastNameTF.getText());
                ps.setString(3, phoneTF.getText());
                ps.setString(4, emailTF.getText());
                ps.setDate(5, Date.valueOf(checkInDate.getValue()));
                ps.setDate(6, Date.valueOf(checkOutDate.getValue()));
                ps.setString(7, roomTF.getText());
                ps.setString(8, roomType);
                ps.setInt(9, customerNum);
                ps.executeUpdate();
                setCustomerTable();
                long days = ChronoUnit.DAYS.between(checkIn, checkOut);
                double totalP = days * Double.parseDouble(getRoom(Integer.parseInt(roomTF.getText())).getPrice());
                //tự động cập nhật customer_receipt khi cập nhật thông tin khách hàng
                String updateCustomerReceipt = "UPDATE customer_receipt SET total = ?, date = ? WHERE customer_num = ?";
                ps = conn.prepareStatement(updateCustomerReceipt);
                ps.setDouble(1, totalP);
                ps.setDouble(2, days);
                ps.setInt(3, customerNum);
                ps.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Customer");
                alert.setHeaderText(null);
                alert.setContentText("Customer updated successfully");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Check In");
                alert.setHeaderText("Check In Failed");
                alert.setContentText("Check Out date must be after Check In date");
                alert.showAndWait();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    //phương thức xóa thông tin khách hàng
    public void deleteCustomer(){
        if (customerTb.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer in the table.");
            alert.showAndWait();
            return;
        }
        //thông báo xác nhận xóa
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Customer");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this customer?");
        //nếu chọn ok thì xóa
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try{
                    int customerNum = Integer.parseInt(customerTb.getSelectionModel().getSelectedItem().getNum());
                    conn = JDBCUtil.getConnection();
                    String query = "DELETE FROM customer WHERE customer_id = ?";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, customerNum);
                    ps.executeUpdate();
                    setCustomerTable();
                    //tự động cập nhật customer_receipt khi xóa thông tin khách hàng
                    String deleteCustomerReceipt = "DELETE FROM customer_receipt WHERE customer_num = ?";
                    ps = conn.prepareStatement(deleteCustomerReceipt);
                    ps.setInt(1, customerNum);
                    ps.executeUpdate();
                    alert.setTitle("Delete Customer");
                    alert.setHeaderText(null);
                    alert.setContentText("Customer deleted successfully");
                    alert.showAndWait();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    //phương thức tìm kiếm khách hàng theo tất cả các trường
    public void searchCustomer(){
        FilteredList<CustomerData> filter = new FilteredList<>(getCustomerData(), p -> true);
        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(customer -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(customer.getNum().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(customer.getFirstName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(customer.getLastName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(customer.getPhone().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(customer.getEmail().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(customer.getCheckInDate().toString().contains(lowerCaseFilter)){
                    return true;
                }else if(customer.getCheckOutDate().toString().contains(lowerCaseFilter)){
                    return true;
                }else if(customer.getRoom().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else return false;
            });
        });
        SortedList<CustomerData> sortedList = new SortedList<>(filter);
        sortedList.comparatorProperty().bind(customerTb.comparatorProperty());
        customerTb.setItems(sortedList);
    }
    //phương thức reset lại bảng khách hàng
    public void resetCustomerTable(){
        searchTF.clear();
        setCustomerTable();
    }

    //phương thức đổi mật khẩu
    public void changePassword() {
        String oldPassword = oldPasswordTF.getText();
        String newPassword = newPasswordTF.getText();
        String confirmPassword = confirmPasswordTF.getText();
        if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Change Password");
            alert.setHeaderText("Change Password Failed");
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
        }else if(!newPassword.equals(confirmPassword)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Change Password");
            alert.setHeaderText("Change Password Failed");
            alert.setContentText("New password and confirm password do not match");
            alert.showAndWait();

        }else if(!oldPassword.equals(LoginController.currentPass)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Change Password");
            alert.setHeaderText("Change Password Failed");
            alert.setContentText("Old password is incorrect");
            alert.showAndWait();
        }else {
            try {
                conn = JDBCUtil.getConnection();
                String query = "UPDATE admin SET password = ? WHERE username = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, newPassword);
                ps.setString(2, LoginController.currentUser);
                ps.executeUpdate();
                LoginController.currentPass = newPassword;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Change Password");
                alert.setHeaderText(null);
                alert.setContentText("Password changed successfully");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    //phương thức đổi tên
    public void changeName(){
        String newName = renameTF.getText();
        if(newName.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Change Name");
            alert.setHeaderText("Change Name Failed");
            alert.setContentText("Please fill in the new name");
            alert.showAndWait();
        }else{
            try{
                conn = JDBCUtil.getConnection();
                String query = "UPDATE admin SET name = ? WHERE username = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, newName);
                ps.setString(2, LoginController.currentUser);
                ps.executeUpdate();
                setNameLB();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Change Name");
                alert.setHeaderText(null);
                alert.setContentText("Name changed successfully");
                alert.showAndWait();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    //phương thức hiển thị tên người dùng lên nameLB
    public void setNameLB(){
        try {
            conn = JDBCUtil.getConnection();
            String query = "SELECT name FROM admin WHERE username = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, LoginController.currentUser);
            rs = ps.executeQuery();
            if (rs.next()) {
                nameLB.setText(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateChart(){
        displayNumberOfCustomer();
        displayIncomeToday();
        displayTotalIncome();
        displayTotalIncomeThisMonth();
        displayNOCChart();
        displayIPDChart();
        displayIPMChart();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAvtImage();
        setNameLB();
        updateChart();
        //theem hình tròn cho ảnh đại diện
        Circle circle = new Circle(60, 60, 60);
        avtImage.setClip(circle);
        setRoomTable();
        updateRoomStatus();
        addItemsToComboBoxes();
        setCustomerTable();
        searchCustomer();
        updateRoomStatusInDB();
    }
}

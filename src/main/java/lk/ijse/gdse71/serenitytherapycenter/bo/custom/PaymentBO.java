package lk.ijse.gdse71.serenitytherapycenter.bo.custom;

import lk.ijse.gdse71.serenitytherapycenter.bo.SuperBO;
import lk.ijse.gdse71.serenitytherapycenter.dto.PaymentDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PaymentBO extends SuperBO {

    List<PaymentDTO> getAllPayments() throws SQLException, ClassNotFoundException;

    Optional<String> getNextPaymentId() throws SQLException, ClassNotFoundException;

    boolean savePayment(PaymentDTO dto) throws SQLException, ClassNotFoundException;

    boolean deletePayment(String s) throws SQLException, ClassNotFoundException;

    boolean updatePayment(PaymentDTO dto) throws SQLException, ClassNotFoundException;

    List<String> getAllPaymentIds() throws SQLException, ClassNotFoundException;

    PaymentDTO findByPaymentId(String id) throws SQLException, ClassNotFoundException;

    double calculateSessionFee(Double upfront, Double programFee, String duration);
}

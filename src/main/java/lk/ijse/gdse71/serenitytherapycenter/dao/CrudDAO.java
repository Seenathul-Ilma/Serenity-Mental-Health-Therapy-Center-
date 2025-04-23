package lk.ijse.gdse71.serenitytherapycenter.dao;

import lk.ijse.gdse71.serenitytherapycenter.entity.SuperEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudDAO<T extends SuperEntity, ID> extends SuperDAO {
    List<T> getAll() throws SQLException, ClassNotFoundException;      // Returns a 'DTO' Arraylist

    Optional<ID> getNextId() throws SQLException, ClassNotFoundException;         // Returns a String value

    boolean save(T dto) throws SQLException, ClassNotFoundException;        // a DTO as a parameter & Returns a boolean

    boolean delete(ID id) throws SQLException, ClassNotFoundException;  // Returns a single attribute/variable

    boolean update(T dto) throws SQLException, ClassNotFoundException;      // a DTO as a parameter& Returns a boolean

    List<String> getAllIds() throws SQLException, ClassNotFoundException;  // Returns a 'String' ArrayList

    Optional<T> findById(String id) throws SQLException, ClassNotFoundException;      // A String value as parameter, & Returns a 'DTO'
}
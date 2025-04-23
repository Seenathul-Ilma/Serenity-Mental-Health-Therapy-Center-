package lk.ijse.gdse71.serenitytherapycenter.dao;

import lk.ijse.gdse71.serenitytherapycenter.dao.custom.Impl.*;
import lk.ijse.gdse71.serenitytherapycenter.dao.custom.Impl.LogInCredentialsDAOImpl;

public class DAOFactory {
    private static DAOFactory daoFactory;
    private DAOFactory() {
        super();
    }

    public static DAOFactory getDaoFactory() {
        if (daoFactory == null) {
            return daoFactory = new DAOFactory();
        }else{
            return daoFactory;
        }
    }

    public enum DAOType{
        USER, PATIENT, THERAPIST, THERAPY_SESSION, THERAPY_PROGRAM, PAYMENT, ENROLLMENT, QUERY
    }

    public SuperDAO getDAO(DAOType type) {
        switch (type) {
            case USER:
                return new LogInCredentialsDAOImpl();
            case PATIENT:
                return new PatientDAOImpl();
            case THERAPIST:
                return new TherapistDAOImpl();
            case THERAPY_PROGRAM:
                return new ProgramDAOImpl();
            case ENROLLMENT:
                return new EnrollmentDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            case THERAPY_SESSION:
                return new TherapySessionDAOImpl();
            //case QUERY:
                //return new QueryDAOImpl();
            default:
                return null;
        }
    }
}

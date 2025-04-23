package lk.ijse.gdse71.serenitytherapycenter.bo;

import lk.ijse.gdse71.serenitytherapycenter.bo.custom.*;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.Impl.*;
import lk.ijse.gdse71.serenitytherapycenter.bo.custom.Impl.LogInCredentialsBOImpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {

    }

    public static BOFactory getBoFactory(){
        if (boFactory == null){
            boFactory = new BOFactory();
        }
        return boFactory;
    }

    public enum BOTypes{
        USER, PATIENT, THERAPIST, THERAPY_PROGRAM, ENROLLMENT, PAYMENT, THERAPY_SESSION
    }

    public SuperBO getBO(BOTypes boType){
        switch (boType){
            case USER:
                return new LogInCredentialsBOImpl();
            case PATIENT:
                return new PatientBOImpl();
            case THERAPIST:
                return new TherapistBOImpl();
            case THERAPY_PROGRAM:
                return new ProgramBOImpl();
            case ENROLLMENT:
                return new RegistrationBOImpl();
            case PAYMENT:
                return new PaymentBOImpl();
            case THERAPY_SESSION:
                return new TherapySessionBOImpl();
            default:
                return null;
        }
    }
}


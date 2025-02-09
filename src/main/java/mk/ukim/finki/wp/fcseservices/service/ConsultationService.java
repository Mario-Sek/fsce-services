package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.consultations.Consultation;
import mk.ukim.finki.wp.fcseservices.model.consultations.ConsultationType;

import java.util.List;

public interface ConsultationService {
    List<Consultation> listNextWeekConsultationsByProfessor(String id, ConsultationType type);
}

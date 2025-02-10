package mk.ukim.finki.wp.fcseservices.model.requests;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class InstallmentPaymentStudentRequest extends StudentRequest {

    private Integer installmentsNum;
}

package mk.ukim.finki.wp.fcseservices.model.memorandum;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class MemorandumPaymentInfo {

    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    public CompanySubscription companySubscription;

    public LocalDate paymentDate;

    public Double amountMkd;

    @Column(length = 1_000)
    public String note;
}

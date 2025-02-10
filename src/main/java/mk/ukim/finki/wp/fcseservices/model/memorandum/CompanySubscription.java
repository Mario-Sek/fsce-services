package mk.ukim.finki.wp.fcseservices.model.memorandum;

import jakarta.persistence.*;
import mk.ukim.finki.wp.fcseservices.model.base.Room;

@Entity
public class CompanySubscription {

    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    public Company company;

    public Short year;

    @Enumerated(EnumType.STRING)
    public MemorandumPackage memorandumPackage;

    @Enumerated(EnumType.STRING)
    public MemorandumStatus status;

    public byte[] contract;

    // silver that pay extra, gold, supportedBy
    @ManyToOne
    public Room brandedRoom;

    @Column(length = 20_000)
    public String note;

}

package mk.ukim.finki.wp.fcseservices.model.memorandum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Company {

    @Id
    public String id;

    public String name;

    public String phone;

    public String email;

    public MemorandumPackage memorandumPackage;

    @Column(length = 10_000)
    public String companyDescription;

    public String websiteUrl;

    public byte[] logoImage;

    public byte[] banner;

    public Boolean active;

    public String password;

    public String token;

    public LocalDateTime validTo;

}

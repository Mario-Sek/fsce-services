package mk.ukim.finki.wp.fcseservices.model.memorandum;

import jakarta.persistence.*;

@Entity
public class WebsitePosting {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Company company;

    public String title;

    @Column(length = 20_000)
    public String body;

    public byte[] image;

    @Enumerated(EnumType.STRING)
    public WebsitePostingStatus status;
}

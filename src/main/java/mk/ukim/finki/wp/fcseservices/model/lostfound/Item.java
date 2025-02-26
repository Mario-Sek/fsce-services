package mk.ukim.finki.wp.fcseservices.model.lostfound;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mk.ukim.finki.wp.fcseservices.model.base.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

    private String description;

    private LocalDate dateIssueCreated;

    private String location;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private byte[] image;

    private boolean handover;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ItemComment> comments = new ArrayList<>();

    private String phoneNumber;

    public Item(String name, ItemCategory itemCategory, String description, LocalDate dateIssueCreated, String location, ItemType itemType, byte[] image, User user, String phoneNumber) {

        this.name = name;
        this.itemCategory = itemCategory;
        this.description = description;
        this.dateIssueCreated = dateIssueCreated;
        this.location = location;
        this.itemType = itemType;
        this.image = image;
        this.handover = false;
        this.user = user;
        this.phoneNumber = phoneNumber;
    }
    public String getBase64Image() {
        return this.image != null ? Base64.getEncoder().encodeToString(this.image) : null;
    }

}
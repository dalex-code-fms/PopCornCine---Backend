package fr.popcorncine.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "t_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false, foreignKey = @ForeignKey(name = "fk_event_user"))
    private User host;

    private Integer movieId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String location;

    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false)
    private Boolean isPrivate = false;

    private Integer ageMin;
    private Integer ageMax;

    @ElementCollection
    private List<String> genderPreferences;

    @Column(nullable = false)
    private Boolean requiresVerifiedBadge = false;

    @ElementCollection
    private List<String> contributions;

    @Column(nullable = false)
    private Double price;

    @ElementCollection
    private List<Integer> seasons;

    @ElementCollection
    private List<Integer> episodes;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participants;
}

package fr.popcorncine.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EventDTO {
    private Long hostId;
    private Integer movieId;
    private String title;
    private LocalDateTime date;
    private String location;
    private Double latitude;
    private Double longitude;
    private Integer maxParticipants;
    private Boolean isPrivate;
    private Integer ageMin;
    private Integer ageMax;
    private List<String> genderPreferences;
    private Boolean requiresVerifiedBadge;
    private List<String> contributions;
    private Double price;
    private List<Integer> seasons;
    private List<Integer> episodes;

}

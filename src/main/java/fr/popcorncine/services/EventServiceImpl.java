package fr.popcorncine.services;

import fr.popcorncine.DTO.EventDTO;
import fr.popcorncine.Entities.Event;
import fr.popcorncine.Entities.User;
import fr.popcorncine.Repository.EventRepository;
import fr.popcorncine.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService{

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public Event createEvent(EventDTO eventDTO){
        User host = userRepository.findById(eventDTO.getHostId()).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé."));

        boolean eventExists = eventRepository.existsByHostAndDate(host, eventDTO.getDate());

        if (eventExists) throw new RuntimeException("L'utilisateur a déjà un événement prévu a cette date et heure.");

        Event event = new Event();
        event.setHost(host);
        event.setMovieId(eventDTO.getMovieId());
        event.setTitle(eventDTO.getTitle());
        event.setDate(eventDTO.getDate());
        event.setLocation(eventDTO.getLocation());
        event.setLatitude(eventDTO.getLatitude());
        event.setLongitude(eventDTO.getLongitude());
        event.setMaxParticipants(eventDTO.getMaxParticipants());
        event.setIsPrivate(eventDTO.getIsPrivate());
        event.setAgeMin(eventDTO.getAgeMin());
        event.setAgeMax(eventDTO.getAgeMax());
        event.setGenderPreferences(eventDTO.getGenderPreferences());
        event.setRequiresVerifiedBadge(eventDTO.getRequiresVerifiedBadge());
        event.setContributions(eventDTO.getContributions());
        event.setPrice(eventDTO.getPrice());
        event.setSeasons(eventDTO.getSeasons());
        event.setEpisodes(eventDTO.getEpisodes());

        return eventRepository.save(event);
    }
}

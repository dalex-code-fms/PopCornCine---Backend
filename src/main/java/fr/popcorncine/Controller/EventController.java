package fr.popcorncine.Controller;

import fr.popcorncine.DTO.EventDTO;
import fr.popcorncine.Entities.Event;
import fr.popcorncine.Repository.EventRepository;
import fr.popcorncine.Repository.UserRepository;
import fr.popcorncine.services.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventServiceImpl eventService;

    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@RequestBody EventDTO eventDTO){
        Event savedEvent = eventService.createEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }
}

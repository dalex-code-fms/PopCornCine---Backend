package fr.popcorncine.Repository;

import fr.popcorncine.Entities.Event;
import fr.popcorncine.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {

    boolean existsByHostAndDate(User host, LocalDateTime date);

}

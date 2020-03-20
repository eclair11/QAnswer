package sem8.intero.proj.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sem8.intero.proj.model.Bot;

public interface BotRepository extends JpaRepository<Bot, Long> {

}
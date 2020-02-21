package sem8.intero.proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sem8.intero.proj.model.Demande;

/**
 * DemandeRepository
 */
public interface DemandeRepository extends JpaRepository<Demande, Long>{

    List<Demande> findAllByOrderBySujet();

    Demande findDemandeById(Long Id);
    
}
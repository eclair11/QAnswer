package sem8.intero.proj.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sem8.intero.proj.model.Enterprise;

/**
 * EnterpriseRepository
 */
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {
    
}
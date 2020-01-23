package sem8.intero.proj.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * Demande
 */
@Entity
@Data
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pseudo;
    private String sujet;
    private String question;
    
    @Temporal(TemporalType.DATE)
    private Date dateQuestion = new Date();

    public Demande() {
    }
    
}
package sem8.intero.proj.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Turnover
 */
@Entity
public class Turnover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String raisonSocial;

    String codePostal;

    String ville;

    String chiffreAffaires;

    public Turnover() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaisonSocial() {
        return raisonSocial;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getChiffreAffaires() {
        return chiffreAffaires;
    }

    public void setChiffreAffaires(String chiffreAffaires) {
        this.chiffreAffaires = chiffreAffaires;
    }

}
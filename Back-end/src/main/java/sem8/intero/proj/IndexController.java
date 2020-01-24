package sem8.intero.proj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sem8.intero.proj.Repository.DemandeRepository;
import sem8.intero.proj.model.Demande;

/**
 * IndexController
 */
@Controller
public class IndexController {

    @Autowired
    public DemandeRepository DemandeRepo;

    @RequestMapping("/")
    public String index(Model m){

        m.addAttribute("demande", new Demande());
        m.addAttribute("afficheDemande", DemandeRepo.findAll());


        return "index";
    }

    @RequestMapping("/addDemande")
    public String addDemande(Demande d){
        DemandeRepo.save(d);
        return "redirect:/";
    }

}
package sem8.intero.proj;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import sem8.intero.proj.Repository.BotRepository;
import sem8.intero.proj.Repository.DemandeRepository;
import sem8.intero.proj.model.AnswerQ;
import sem8.intero.proj.model.Bot;
import sem8.intero.proj.model.BotInsert;
import sem8.intero.proj.model.Demande;

/**
 * AnswerController
 */
@Controller
public class AnswerController {

    @Autowired
    public DemandeRepository DemandeRepo;

    @Autowired
    public BotRepository BotRepo;

    @RequestMapping("/")
    public String index(Model m) {
        m.addAttribute("demande", new Demande());
        m.addAttribute("afficheDemande", DemandeRepo.findAll());
        return "index";
    }

    @PostMapping("/demande")
    public String demande(Model m, Demande d, RedirectAttributes redirect) {
        DemandeRepo.save(d);
        ArrayList<Object> answer = AnswerQ.sendQuestion(d.getQuestion(), d.getLangue());
        redirect.addFlashAttribute("answer", answer);
        return "redirect:/";
    }


    /**
     * Page du Bot WIKIDATA
     */

     /* Hub de la page du Bot Wikidata */
    @RequestMapping("/bot")
    public String bote(Model m, String reponse) {

        m.addAttribute("bot", new Bot());

        /* On reset les messages de réponse*/
        if (m.getAttribute("bool") != null && m.getAttribute("bool").equals("true")) {
            m.addAttribute("reponseInsert", "");
            m.addAttribute("reponseUpdate", "");
            m.addAttribute("reponseTranscription", "");
            m.addAttribute("bool", false);        
        }

        return "bot";

    }

    /* Création d'une entité ou d'une propriété dans Wikidata */
    @PostMapping("/botinsert")
    public String botinsert(Model m, Bot b) throws MediaWikiApiErrorException, IOException, LoginFailedException {

        String label = b.getLabel();
        String description = b.getDescription();
        String lang = b.getLang();

        String reponse;

        if(BotInsert.estEntitePresente(label)){
            reponse="Déjà présent dans Wikidata, insertion de " + label +  " impossible";
        }
        else{
            BotInsert.botot(label, description, lang);
            reponse = "Vous avez créé l'entité pour la langue '" + lang + "'";
        }

        m.addAttribute("reponseInsert", reponse);
        m.addAttribute("bool", "true");

        return "bot";
    }

    /* Mise à jour d'une entité ou d'une propriété dans Wikidata */
    @PostMapping("/botupdate")
    public String botupdate(Model m, Bot b) throws LoginFailedException, MediaWikiApiErrorException, IOException {

        String reference = b.getReference();
        String label = b.getLabel();
        String description = b.getDescription();
        String lang = b.getLang();

        String reponse = "";

        if(!BotInsert.estEntitePresente(reference)){
            reponse="Absent de la base Wikidata, mise à jour de  " + reference +  " impossible";
        }
        else{
            BotInsert.updateBot(reference, label, description, lang);
            reponse = "Vous avez effectué la mis à jour de " + label + " pour la langue '" + lang + "'";
        }

        m.addAttribute("reponseUpdate", reponse);
        m.addAttribute("bool", "true");

        return "bot";
    }
    
    /* Transcription d'un code ou d'un label */
    @PostMapping("/bottranscription")
    public String bottranscription(Model m, Bot b) throws LoginFailedException, MediaWikiApiErrorException, IOException {

        String label = b.getLabel();
        String code = b.getCode();
        String lang = b.getLang();
        boolean type = b.isTranscription();

        String reponse = "";

        reponse = BotInsert.transcritEntiteCode(label, code, lang, type);

        /*
        if(!BotInsert.estEntitePresente(label)){
            reponse="Absent de la base Wikidata, mise à jour de  " + label +  " impossible";
        }
        else{
            BotInsert.updateBot(label, description, lang);
            reponse = "Vous avez effectué la mis à jour de " + label + " pour la langue '" + lang + "'";
        }
        */
        m.addAttribute("reponseTranscription", reponse);
        m.addAttribute("bool", "true");

        return "bot";
    }

}
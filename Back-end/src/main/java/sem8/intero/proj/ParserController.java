package sem8.intero.proj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import sem8.intero.proj.Repository.EnterpriseRepository;
import sem8.intero.proj.Repository.TurnoverRepository;
import sem8.intero.proj.model.Enterprise;
import sem8.intero.proj.model.Turnover;
import sem8.intero.proj.model.Upload;

@Controller
public class ParserController {

    @Autowired
    EnterpriseRepository enterRepo;

    @Autowired
    TurnoverRepository turnRepo;

    @RequestMapping("/parser")
    public String parser(Model m) {
        m.addAttribute("upload", new Upload());
        return "parser";
    }

    @PostMapping("/addfiles")
    public RedirectView addFiles(RedirectAttributes redirectAttributes, @ModelAttribute("upload") Upload upload) {
        MultipartFile[] files = upload.getFiles();
        List<String> results = new ArrayList<>();
        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            if (FilenameUtils.getExtension(name).equals("csv") && !file.isEmpty() && file.getSize() < 150000000) {
                try {
                    parserCSV(file);
                    results.add(name + " : fichier valide");
                } catch (Exception e) {
                    results.add(name + " : fichier avec un contenu non valide");
                }
            } else {
                results.add(name + " : fichier avec une extension ou une taille non valide");
            }
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setContextRelative(true);
        redirectView.setUrl("parser");
        redirectAttributes.addFlashAttribute("results", results);
        return redirectView;
    }

    @PostMapping("/addlinks")
    public RedirectView addLinks(RedirectAttributes redirectAttributes, @ModelAttribute("upload") Upload upload) {
        String link = upload.getLink();
        String status = "";
        if (link.length() > 0) {
            try {
                parserHTML(link);
                status = "lien valide";
            } catch (Exception e) {
                status = "lien non valide";
            }
        } else {
            status = "lien vide";
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setContextRelative(true);
        redirectView.setUrl("parser");
        redirectAttributes.addFlashAttribute("status", status);
        return redirectView;
    }

    private void parserCSV(MultipartFile file) throws Exception {
        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CsvToBean<Enterprise> csvToBean = new CsvToBeanBuilder(reader).withType(Enterprise.class)
                .withIgnoreLeadingWhiteSpace(true).build();
        List<Enterprise> enterprises = csvToBean.parse();
        enterRepo.saveAll(enterprises);
    }

    private void parserHTML(String link) throws Exception {
        Document doc = Jsoup.connect(link).get();
        List<Turnover> turnovers = new ArrayList<>();
        Iterator<Element> elementsCol1 = doc.getElementsByClass("verif_col1").listIterator(1);
        Iterator<Element> elementsCol2 = doc.getElementsByClass("verif_col2").listIterator(1);
        Iterator<Element> elementsCol3 = doc.getElementsByClass("verif_col3").listIterator(1);
        Iterator<Element> elementsCol5 = doc.getElementsByClass("verif_col5").listIterator(1);
        while (elementsCol1.hasNext()) {
            Turnover turnover = new Turnover();
            turnover.setRaisonSocial(elementsCol1.next().text());
            turnover.setCodePostal(elementsCol2.next().text());
            turnover.setVille(elementsCol3.next().text());
            turnover.setChiffreAffaires(elementsCol5.next().text());
            turnovers.add(turnover);
        }
        turnRepo.saveAll(turnovers);
    }

}
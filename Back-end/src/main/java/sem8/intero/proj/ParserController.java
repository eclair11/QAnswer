package sem8.intero.proj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import sem8.intero.proj.model.Enterprise;
import sem8.intero.proj.model.Upload;
import sem8.intero.proj.repository.EnterpriseRepository;

/**
 * ParserController
 */
@Controller
public class ParserController {

    @Autowired
    EnterpriseRepository enterRepo;

    @RequestMapping("/parser")
    public String parser(Model m) {
        m.addAttribute("upload", new Upload());
        return "parser";
    }

    @PostMapping("/addlinks")
    public String addLinks(Model m, @ModelAttribute("upload") Upload upload) throws IOException {
        Document doc = Jsoup.connect(upload.getLink()).get();
        m.addAttribute("document", doc);
        return "display";
    }

    @PostMapping("/addfiles")
    public String addFiles(Model m, @ModelAttribute("upload") Upload upload) throws IOException {
        MultipartFile[] files = upload.getFiles();
        List<String> results = new ArrayList<>();
        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            if (file.getSize() < 150000000 && !file.isEmpty()) {
                if (FilenameUtils.getExtension(name).equals("csv")) {
                    parserCSV(file);
                    results.add(name + " : fichier CSV valide");
                } else if (FilenameUtils.getExtension(name).equals("pdf")) {
                    parserPDF(file);
                    results.add(name + " : fichier PDF valide");
                } else {
                    results.add(name + " : fichier de type invalide");
                }
            } else {
                results.add(name + " : fichier vide ou de taille lourde");
            }
        }
        m.addAttribute("results", results);
        return "redirect:/parser";
    }

    private void parserCSV(MultipartFile file) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CsvToBean<Enterprise> csvToBean = new CsvToBeanBuilder(reader).withType(Enterprise.class)
                .withIgnoreLeadingWhiteSpace(true).build();
        List<Enterprise> enterprises = csvToBean.parse();
        enterRepo.saveAll(enterprises);
    }

    private void parserPDF(MultipartFile file) throws IOException {
        PDFTextStripper pdfStripper = new PDFTextStripper();
        PDDocument pdfDoc = PDDocument.load(file.getInputStream());
        String parsedText = pdfStripper.getText(pdfDoc);
        String[] lines = parsedText.split("\\r\\n\\r\\n");
        for (String line : lines) {
            System.out.println(line);
        }
    }

}
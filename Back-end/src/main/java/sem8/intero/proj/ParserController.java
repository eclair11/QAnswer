package sem8.intero.proj;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import sem8.intero.proj.model.Upload;

/**
 * ParserController
 */
@Controller
public class ParserController {

    @RequestMapping("/parser")
    public String parser(Model m) {
        m.addAttribute("upload", new Upload());
        return "parser";
    }

    @PostMapping("/addlinks")
    public String addLinks(Model m, @ModelAttribute("upload") Upload upload) {
        Document doc = null;
        try {
            doc = Jsoup.connect(upload.getLink()).get();
        } catch (IOException e) {
            System.err.println(e);
        }
        m.addAttribute("document", doc);
        return "display";
    }

    @PostMapping("/addfiles")
    public String addFiles(Model m, @ModelAttribute("upload") Upload upload) throws IOException {
        MultipartFile[] fileDatas = upload.getFiles();
        for (MultipartFile fileData : fileDatas) {
            String name = fileData.getOriginalFilename();
            if (name != null && name.length() > 0) {
                parserPDF(fileData);
            }
        }
        return "redirect:/parser";
    }

    private void parserPDF(MultipartFile data) throws IOException {
        PDFTextStripper pdfStripper = new PDFTextStripper();
        PDDocument pdfDoc = PDDocument.load(data.getInputStream());
        String parsedText = pdfStripper.getText(pdfDoc);
        String[] lines = parsedText.split("\\r\\n\\r\\n");
        for (String line : lines) {
            System.out.println(line);
        }
    }

    private <T> List<T> parserCSV(Class<T> type, String fileName) throws IOException {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();
        File file = new ClassPathResource(fileName).getFile();
        MappingIterator<T> readValues = mapper.reader(type).with(bootstrapSchema).readValues(file);
        System.out.println(readValues.readAll());
        return readValues.readAll();
    }

}
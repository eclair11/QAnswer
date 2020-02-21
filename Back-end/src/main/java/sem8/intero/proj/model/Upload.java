package sem8.intero.proj.model;

import org.springframework.web.multipart.MultipartFile;

/**
 * Upload
 */
public class Upload {

    private MultipartFile[] files;
    private String link;

    public Upload() {
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
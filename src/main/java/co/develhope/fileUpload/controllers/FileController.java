package co.develhope.fileUpload.controllers;

import co.develhope.fileUpload.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Tania Ielpo
 */

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/download")
    public byte[] download(@RequestParam String fileName, HttpServletResponse response) throws IOException {
        byte[] toReturn = fileStorageService.download(fileName, response);
        //System.out.println(response.getContentType());
        return toReturn;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile fileName) throws IOException {

            return fileStorageService.upload(fileName);

    }
}

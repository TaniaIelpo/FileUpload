package co.develhope.fileUpload.services;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Tania Ielpo
 */

@Service
public class FileStorageService {
    @Value("${fileRepositoryFolder}")
    private String fileRepositoryFolder;

    /**
     * it extracts the extension of the file given in input
     * generate a random name and save the file with this new name in a temporary directory
     * check that the directory where we want to save it exists and is a directory
     * creates an object of type File with the generated name
     *check that there is no object of the same name (therefore a repeated file)
     * transfer the file in the final directory
     * @param file file to upload
     * @return the random name generated for the file
     */

    @SneakyThrows
    public String upload(MultipartFile file){

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName= UUID.randomUUID().toString()+"."+extension;
        File finalFolder = new File(fileRepositoryFolder);
        if (!finalFolder.exists()) throw new IOException("Folder doesn't exist");
        if (!finalFolder.isDirectory()) throw new IOException("Folder doesn't a folder");
        File fileDestination=new File(fileRepositoryFolder+"\\"+newFileName);

        if (fileDestination.exists()) throw new IOException("File conflict");
        file.transferTo(fileDestination);
        return newFileName;
    }

    /**
     * it extracts the extension of the file given in input
     * check the file extension and set the contentType of the response
     * sets the header "ontent-Disposition"
     * takes the file from the directory and returns it like byte Array
     * @param fileName name of file to download
     * @param response
     * @return an array of byte
     */

    @SneakyThrows
    public @ResponseBody byte[] download(String fileName, HttpServletResponse response){
        System.out.println("Downloading: "+fileName);
        String extension = FilenameUtils.getExtension(fileName);
        switch (extension){
            case"gif":
                response.setContentType(MediaType.IMAGE_GIF_VALUE);
                break;
            case"jpg":
            case"jpeg":
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                break;
            case"png":
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                break;
        }
        //System.out.println(response.getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
        File fileFromRepository= new File(fileRepositoryFolder+"\\"+fileName);
        return IOUtils.toByteArray(new FileInputStream(fileFromRepository));

    }
}

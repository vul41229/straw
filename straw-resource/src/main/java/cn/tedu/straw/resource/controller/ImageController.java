package cn.tedu.straw.resource.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@Slf4j
@CrossOrigin
public class ImageController {

    @Value("${straw.resource.path}")
    private File resourcePath;

    @Value("${straw.resource.host}")
    private String resourceHost;

    @PostMapping
    public String upload(MultipartFile imageFile) throws IOException {


        String path= DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDateTime.now());
        log.info("圖片路徑{}",path);

        File folder=new File(resourcePath,path);

        folder.mkdirs();


        String filename=imageFile.getOriginalFilename();

        String ext=filename.substring(filename.lastIndexOf("."));

        String name= UUID.randomUUID().toString()+ext;


        File file=new File(folder,name);

        log.debug("文件上傳路徑:{}",file.getAbsolutePath());

        imageFile.transferTo(file);

        String url=resourceHost+"/"+path+"/"+name;
        log.debug("回顯上傳圖片的url:{}",url);

        return url;

    }
}

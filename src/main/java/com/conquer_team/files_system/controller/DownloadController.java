package com.conquer_team.files_system.controller;


import com.conquer_team.files_system.repository.ArchiveRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/downloads")
public class DownloadController {

    @Value("${image.directory}")
    private String uploadImageDirectory;
    private final ArchiveRepo repo;

    @GetMapping("/file")
    public ResponseEntity<?> viewFile(@RequestParam String filename) throws IOException {

        File file = new File(uploadImageDirectory + "/" + filename);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found!");
        }

        long fileSize = file.length();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        if (fileSize >= 2097152) {
            System.out.println(">=2m");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (FileInputStream fileInputStream = new FileInputStream(file);
                 GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fileInputStream.read(buffer)) > 0) {
                    gzipOutputStream.write(buffer, 0, length);
                }
                gzipOutputStream.finish();
            }

            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName() + ".gz");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(byteArrayOutputStream.size())
                    .contentType(MediaType.parseMediaType("application/gzip"))
                    .body(new ByteArrayResource(byteArrayOutputStream.toByteArray()));
        } else {
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileSize)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(new FileInputStream(file)));
        }

    }
}


//        System.out.println(filename);
//        File file = new File(uploadImageDirectory + "/" + filename);
//
//        HttpHeaders header = new HttpHeaders();
//        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
//        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        header.add("Pragma", "no-cache");
//        header.add("Expires", "0");
//
//        System.out.println(file.getAbsolutePath());
//        Path filePath = Paths.get(file.getAbsolutePath());
//        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));
//
//        return ResponseEntity.ok()
//                .headers(header)
//                .contentLength(file.length())
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(resource);


// @GetMapping("/report")
//    public void exportToCSV(HttpServletResponse response) throws IOException {
//        response.setContentType("text/csv");
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
//        String currentDateTime = dateFormatter.format(new Date());
//
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
//        response.setHeader(headerKey, headerValue);
//
//        List<Archive> archives = repo.findAll(Sort.by("id"));
//
//        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
//        String[] csvHeader = {"User", "File", "Update_Size", "Update_type", "Number_Of_Modified_Lines", "Details"};
//        String[] nameMapping = {"user", "file", "size", "type", "numOfUpdateLines", "details"};
//
//        csvWriter.writeHeader(csvHeader);
//
//        for (Archive archive : archives) {
//            csvWriter.write(archive, nameMapping);
//        }
//
//        csvWriter.close();
//
//    }


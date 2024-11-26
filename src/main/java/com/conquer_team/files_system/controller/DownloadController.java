package com.conquer_team.files_system.controller;

import com.conquer_team.files_system.model.entity.Archive;
import com.conquer_team.files_system.repository.ArchiveRepo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/downloads")
public class DownloadController {

    @Value("${image.directory}")
    private String uploadImageDirectory;
    private final ArchiveRepo repo;

    @GetMapping("/file")
    public ResponseEntity<?> viewFile(@RequestParam String filename) throws IOException {
        System.out.println(filename);
        File file = new File(uploadImageDirectory + "/" + filename);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".txt");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        System.out.println(file.getAbsolutePath());
        Path filePath = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }


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
}

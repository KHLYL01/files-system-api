package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.model.entity.Archive;
import com.conquer_team.files_system.repository.ArchiveRepo;
import com.conquer_team.files_system.services.ArchiveService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArchiveServiceImpl implements ArchiveService {
    private final ArchiveRepo repo;

    @Override
    public List<Archive> getAllByUserId(long id) {
        return null;
    }

    @Override
    public List<Archive> getAllByFileId(long id) {
        return null;
    }

    @Override
    public void reportForUser(HttpServletResponse response, long id, long folderId) throws IOException {
        List<Archive> archives = repo.findAllByUserId(id);
        this.exportToCSV(response, archives);
    }

    @Override
    public void reportForFile(HttpServletResponse response, long id, long folderId) throws IOException {
        List<Archive> archives = repo.findAllByFileId(id);
        this.exportToCSV(response, archives);
    }

    @Override
    public void reportForAll(HttpServletResponse response) throws IOException {
        List<Archive> archives = repo.findAll();
        this.exportToCSV(response,archives);
    }

    private void exportToCSV(HttpServletResponse response, List<Archive> archives) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        System.out.println("whyyyy?");
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"User", "File", "Update_Size", "Details","UpdateAt"};
        String[] nameMapping = {"user", "file", "size",  "details","createdAt"};

        csvWriter.writeHeader(csvHeader);

        for (Archive archive : archives) {
            csvWriter.write(archive, nameMapping);
        }

        csvWriter.close();

        log.info("Export To CSV Successfully");
    }
}

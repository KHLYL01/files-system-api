package com.conquer_team.files_system.services.serviceImpl;

import com.conquer_team.files_system.model.entity.Archive;
import com.conquer_team.files_system.repository.ArchiveRepo;
import com.conquer_team.files_system.services.ArchiveService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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


    public void exportToCSV(HttpServletResponse response, List<Archive> archives) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);


        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"User", "File", "Update_Size", "Update_type", "Number_Of_Modified_Lines", "Details"};
        String[] nameMapping = {"user", "file", "size", "type", "numOfUpdateLines", "details"};

        csvWriter.writeHeader(csvHeader);

        for (Archive archive : archives) {
            csvWriter.write(archive, nameMapping);
        }

        csvWriter.close();
    }

    @Override
    public void reportForAll(HttpServletResponse response) throws IOException {
        List<Archive> archives = repo.findAll();
        this.exportToCSV(response,archives);
    }
}

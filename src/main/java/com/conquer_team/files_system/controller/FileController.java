package com.conquer_team.files_system.controller;

import com.conquer_team.files_system.model.dto.requests.*;
import com.conquer_team.files_system.notation.AdminFolder;
import com.conquer_team.files_system.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
public class FileController {

    final private FileService fileService;
    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(fileService.findAll());
    }

//    @GetMapping("/users/{id}")
//    public ResponseEntity<?> findAllByUserId(@PathVariable Long id) {
//        return ResponseEntity.ok(fileService.findAllByUserId(id));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id){
        return ResponseEntity.status(200).body(fileService.getById(id));
    }

    @GetMapping("booked/users/{id}")
    public ResponseEntity<?> findAllBookedByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.findAllBookedFileByUserId(id));
    }

    @GetMapping("/folders/{id}")
    public ResponseEntity<?> findAllByFolderId(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.findAllByFolderId(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @AdminFolder
    public ResponseEntity<?> save(@ModelAttribute AddFileRequest request) throws IOException {
        return new ResponseEntity<>(fileService.save(request) , HttpStatus.CREATED);
    }

    @PostMapping("/check-in")
    public ResponseEntity<?> checkIn(@RequestBody CheckInFileRequest request) {
        return new ResponseEntity<>(fileService.checkIn(request) , HttpStatus.CREATED);
    }

    @PostMapping("/check-in-all")
    public ResponseEntity<?> checkInAll(@RequestBody CheckInAllFileRequest request) {
        return new ResponseEntity<>(fileService.checkInAll(request) , HttpStatus.CREATED);
    }

    @PutMapping(value = "/check-out/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> checkOut(@PathVariable long id
            , @ModelAttribute CheckOutRequest request)throws IOException{
        return ResponseEntity.status(200).body(fileService.checkOut(request,id));
    }

    @DeleteMapping("/{id}")
    @AdminFolder
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        fileService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

package com.backend.backend.controller;

import com.backend.backend.dto.ApplicationHistoryDTO;
import com.backend.backend.dto.UpdateJobApplicationDTO;
import com.backend.backend.entity.CandidateApplication;
import com.backend.backend.handler.GlobalExceptionHandler;
import com.backend.backend.repository.CvRepository;
import com.backend.backend.response.ApiResponse;
import com.backend.backend.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService applicationService;
    private final CvRepository cvRepository;

    private static final String WORD_DOC_VALUE = "application/msword";
    private static final String WORD_DOCX_VALUE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    public JobApplicationController(JobApplicationService applicationService, CvRepository cvRepository) {
        this.applicationService = applicationService;
        this.cvRepository = cvRepository;
    }

    @PostMapping(value = "/apply", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> applyToJob(
            @RequestParam("jobOfferId") String jobOfferId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {

        String currentUserId = authentication.getName();
        String contentType = file.getContentType();

        if (!MediaType.APPLICATION_PDF_VALUE.equals(contentType) &&
                !WORD_DOC_VALUE.equals(contentType) &&
                !WORD_DOCX_VALUE.equals(contentType)) {

            throw new IllegalArgumentException("Only PDF and Word documents are allowed!");
        }

        applicationService.applyToJobWithFile(jobOfferId, file, currentUserId);

        return ResponseEntity.ok(Map.of("message", "Application submitted successfully with your CV!"));
    }

    @GetMapping("/download-cv/{applicationId}")
    public ResponseEntity<Resource> downloadCv(@PathVariable String applicationId, Authentication authentication) throws MalformedURLException { // <--- Adăugat aici

        JobApplicationService.FileDownloadModel fileModel = applicationService.getCvFileForApplication(applicationId,
                authentication.getName(), authentication);

        Path filePath = Paths.get(fileModel.filePath());
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new GlobalExceptionHandler.ResourceNotFoundException(
                    "The requested CV file is missing from the server storage or corrupted.");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileModel.fileName() + "\"")
                .body(resource);
    }

    @GetMapping("/my-applications")
    public ResponseEntity<List<ApplicationHistoryDTO>> getMyApplications(Authentication authentication) {
        String currentUserId = authentication.getName();
        List<ApplicationHistoryDTO> apps = applicationService.getApplicationsByUserId(currentUserId);
        return ResponseEntity.ok(apps);
    }

    @PreAuthorize("hasAuthority('ROLE_HR') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    @GetMapping("/job-offers/{jobOfferId}/applications")
    public ResponseEntity<List<ApplicationHistoryDTO>> getJobApplications(
            @PathVariable String jobOfferId,
            Authentication authentication){

        List<ApplicationHistoryDTO> apps = applicationService.getApplicationsByJobOffer(jobOfferId, authentication);
        return ResponseEntity.ok(apps);
    }

    @PreAuthorize("hasAuthority('ROLE_HR')")
    @PutMapping("/{appId}/review")
    public ResponseEntity<ApplicationHistoryDTO> updateCandidateApplication(
            @PathVariable String appId,
            @Valid @RequestBody UpdateJobApplicationDTO request,
            Authentication authentication){

        ApplicationHistoryDTO updatedApplication = applicationService.updateApplicationReview(appId, request, authentication);
        return ResponseEntity.ok(updatedApplication);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_HR')")
    @DeleteMapping("/delete/{appId}")
    public ResponseEntity<ApiResponse> deleteApplication(@PathVariable String appId){
        applicationService.deleteApplicationWithCv(appId);
        return ResponseEntity.noContent().build();
    }
}
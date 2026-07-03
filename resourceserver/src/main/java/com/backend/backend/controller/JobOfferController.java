package com.backend.backend.controller;

import com.backend.backend.dto.*;
import com.backend.backend.entity.JobOffer;
import com.backend.backend.response.ApiResponse;
import com.backend.backend.service.JobOfferService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobOfferController {

    private final JobOfferService jobOfferService;

    public JobOfferController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }

    @GetMapping("/all-jobs")
    public PagedModel<JobOffersDTO> getAllActiveJobs(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "applicationEndDate-desc") String sortOption) {

        return jobOfferService.getAllActiveJobOffers(title, page, size, sortOption);
    }

    @GetMapping("/job-offer/{id}")
    public ResponseEntity<ShortJobOfferDTO> getJobOffer(@PathVariable String id) {
        ShortJobOfferDTO dto = jobOfferService.findJobOfferById(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_HR')")
    public ResponseEntity<ApiResponse> createJobOffer(@Valid @RequestBody CreateJobOfferDTO createJobOfferDTO, Authentication authentication){
        JobOffer offer = jobOfferService.createOffer(createJobOfferDTO, authentication);

        ApiResponse response = new ApiResponse("Job offer created successfully!", 200);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-job-offers")
    @PreAuthorize("hasAuthority('ROLE_HR')")
    public ResponseEntity<List<JobOfferRequestDTO>> getMyJobOffers(Authentication authentication){
        List<JobOfferRequestDTO> myJobs = jobOfferService.getJobsById(authentication);
        return ResponseEntity.ok(myJobs);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_HR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<JobOfferRequestDTO> updateJobOffer(
            @PathVariable String id,
            @Valid @RequestBody JobOfferRequestDTO dto,
            Authentication authentication) {

        JobOfferRequestDTO updatedOffer = jobOfferService.updateOffer(id, dto, authentication);
        return ResponseEntity.ok(updatedOffer);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_HR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteJobOffer(@PathVariable String id, Authentication authentication){
        jobOfferService.deleteOffer(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/job-offers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<JobOfferAdminDTO>> getAllJobOffers() {
        List<JobOfferAdminDTO> offers = jobOfferService.getAllJobOffersForAdmin();
        return ResponseEntity.ok(offers);
    }

    @PutMapping("/admin/job-offers/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<JobOfferAdminDTO> updateJobOffer(
            @PathVariable String id,
            @RequestBody JobOfferAdminDTO dto) {

        JobOfferAdminDTO updatedDto = jobOfferService.updateOffer(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

}

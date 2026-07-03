package com.backend.backend.service;

import com.backend.backend.Exception;
import com.backend.backend.JobStatus;
import com.backend.backend.dto.*;
import com.backend.backend.entity.JobOffer;
import com.backend.backend.entity.UserProfile;
import com.backend.backend.mapper.JobOfferMapper;
import com.backend.backend.repository.JobOfferRepository;
import com.backend.backend.repository.UserProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final JobOfferMapper jobOfferMapper;
    private final UserProfileRepository userProfileRepository;

    public JobOfferService(JobOfferRepository jobOfferRepository, JobOfferMapper jobOfferMapper, UserProfileRepository userProfileRepository) {
        this.jobOfferRepository = jobOfferRepository;
        this.jobOfferMapper = jobOfferMapper;
        this.userProfileRepository = userProfileRepository;
    }

    public JobOffer createOffer(CreateJobOfferDTO dto, Authentication authentication) {

        if (dto.getMaxSalary() < dto.getMinSalary()) {
            throw new IllegalArgumentException("Maximum salary cannot be lower than minimum salary.");
        }

        if (dto.getApplicationEndDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Application end date must be in the future.");
        }

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }

        if(dto.getJobTitle() == null || dto.getJobTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Job title cannot be empty.");
        }

        String recruiterId = authentication.getName();

        JobOffer offer = new JobOffer();
        offer.setJobTitle(dto.getJobTitle());
        offer.setDescription(dto.getDescription());
        offer.setMinSalary(dto.getMinSalary());
        offer.setMaxSalary(dto.getMaxSalary());
        offer.setApplicationEndDate(dto.getApplicationEndDate());
        offer.setReviewedBy(recruiterId);
        offer.setApplicationStartDate(LocalDateTime.now());
        offer.setJobStatus(JobStatus.ACTIVE);

        return jobOfferRepository.save(offer);
    }

    public PagedModel<JobOffersDTO> getAllActiveJobOffers(String title, int page, int size, String sortOption) {

        String sortBy = "applicationEndDate";
        Sort.Direction direction = Sort.Direction.DESC;

        if (sortOption != null && sortOption.contains("-")) {
            String[] parts = sortOption.split("-");
            sortBy = parts[0];
            direction = parts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        String titleFilter = (title != null && !title.trim().isEmpty()) ? title.trim() : null;

        Page<JobOffer> entityPage = jobOfferRepository.findActiveJobsWithFilter(titleFilter, pageable);
        Page<JobOffersDTO> dtoPage = entityPage.map(jobOfferMapper::offerToJobOffersDTO);

        return new PagedModel<>(dtoPage);
    }

    public ShortJobOfferDTO findJobOfferById(String id) {

        JobOffer offer = jobOfferRepository.findById(id).orElseThrow(() ->
                new Exception.ResourceNotFoundException("Job offer with id " + id + " not found."));

        ShortJobOfferDTO dto = new ShortJobOfferDTO();
        dto.setId(offer.getId());
        dto.setJobTitle(offer.getJobTitle());
        return dto;
    }

    public List<JobOfferRequestDTO> getJobsById(Authentication authentication){

        String id = authentication.getName();
        List<JobOffer> hrJobOffers = jobOfferRepository.findByReviewedBy(id);

        return hrJobOffers.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public JobOfferRequestDTO updateOffer(String id, JobOfferRequestDTO dto, Authentication authentication){
        JobOffer offer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new Exception.ResourceNotFoundException("Couldn't find job offer with id: " + id));

        boolean isOwner = authentication.getName().equals(offer.getReviewedBy());

        boolean isAdmin = false;

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            String userRole = jwtAuth.getToken().getClaimAsString("role");

            if (userRole != null) {
                isAdmin = userRole.equalsIgnoreCase("ROLE_ADMIN");
            }
        }
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You don't have permissions to edit this!");
        }

        offer.setJobTitle(dto.getJobTitle());
        offer.setJobStatus(dto.getJobStatus());
        offer.setDescription(dto.getDescription());
        offer.setMinSalary(dto.getMinSalary());
        offer.setMaxSalary(dto.getMaxSalary());
        offer.setApplicationEndDate(dto.getApplicationEndDate());

        JobOffer savedJobOffer = jobOfferRepository.save(offer);
        return convertToDTO(savedJobOffer);
    }

    @Transactional
    public void deleteOffer(String id, Authentication authentication){
        JobOffer offer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new Exception.ResourceNotFoundException("Job offer with id: " + id + " doesn't exist!"));

        boolean isOwner = authentication.getName().equals(offer.getReviewedBy());

        boolean isAdmin = false;

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            String userRole = jwtAuth.getToken().getClaimAsString("role");

            if (userRole != null) {
                isAdmin = userRole.equalsIgnoreCase("ROLE_ADMIN") || userRole.equalsIgnoreCase("ROLE_SUPER_ADMIN");
            }
        }

        if(!isOwner && !isAdmin){
            throw new AccessDeniedException("You don't have permissions to delete this!");
        }

        jobOfferRepository.delete(offer);
    }

    @Transactional(readOnly = true)
    public List<JobOfferAdminDTO> getAllJobOffersForAdmin(){

        List<JobOfferAdminDTO> offers = jobOfferRepository.findAll().stream()
                .map(jobOfferMapper::offerToAdminDTO)
                .toList();

        offers.forEach(dto -> {
            String currentUuid = dto.getReviewedBy();
            System.out.println(currentUuid);
            if (currentUuid != null && !currentUuid.isBlank()) {
                String email = userProfileRepository.findById(currentUuid)
                        .map(UserProfile::getEmail)
                        .orElse("Unknown HR");
                dto.setReviewedBy(email);
            }
        });

        return offers;
    }

    @Transactional
    public JobOfferAdminDTO updateOffer(String id, JobOfferAdminDTO dto) {

        JobOffer existingOffer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job offer with id: " + id + " doesn't exist!"));

        JobOffer updatedOfferEntity = jobOfferMapper.adminDtoToOffer(dto);

        String targetUuid = null;
        String inputEmail = dto.getReviewedBy();

        if (inputEmail != null && !inputEmail.isBlank()) {
            UserProfile userProfile = userProfileRepository.findByEmail(inputEmail)
                    .orElseThrow(() -> new IllegalArgumentException("User with email: " + inputEmail + " was not found!"));

            targetUuid = userProfile.getId();
        }

        updatedOfferEntity.setReviewedBy(targetUuid);
        updatedOfferEntity.setApplications(existingOffer.getApplications());

        JobOffer savedOffer = jobOfferRepository.save(updatedOfferEntity);

        JobOfferAdminDTO responseDto = jobOfferMapper.offerToAdminDTO(savedOffer);
        responseDto.setReviewedBy(inputEmail);

        return responseDto;
    }

    private JobOfferRequestDTO convertToDTO(JobOffer jobOffer) {
        JobOfferRequestDTO dto = new JobOfferRequestDTO();

        dto.setId(jobOffer.getId());
        dto.setJobTitle(jobOffer.getJobTitle());
        dto.setDescription(jobOffer.getDescription());
        dto.setMinSalary(jobOffer.getMinSalary());
        dto.setMaxSalary(jobOffer.getMaxSalary());
        dto.setJobStatus(jobOffer.getJobStatus());
        dto.setApplicationStartDate(jobOffer.getApplicationStartDate());
        dto.setApplicationEndDate(jobOffer.getApplicationEndDate());
        return dto;
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void expirePastJobOffers() {
        LocalDateTime now = LocalDateTime.now();
        jobOfferRepository.updateExpiredJobs(now, JobStatus.ACTIVE, JobStatus.INACTIVE);
    }


}

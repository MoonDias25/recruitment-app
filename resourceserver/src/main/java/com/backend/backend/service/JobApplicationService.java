package com.backend.backend.service;

import com.backend.backend.ApplicationStatus;
import com.backend.backend.Exception;
import com.backend.backend.dto.ApplicationHistoryDTO;
import com.backend.backend.dto.UpdateJobApplicationDTO;
import com.backend.backend.entity.CV;
import com.backend.backend.entity.CandidateApplication;
import com.backend.backend.entity.JobOffer;
import com.backend.backend.entity.UserProfile;
import com.backend.backend.mapper.JobApplicationMapper;
import com.backend.backend.repository.CandidateApplicationRepository;
import com.backend.backend.repository.CvRepository;
import com.backend.backend.repository.JobOfferRepository;
import com.backend.backend.repository.UserProfileRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    private final CandidateApplicationRepository applicationRepository;
    private final JobOfferRepository jobOfferRepository;
    private final CvRepository cvRepository;
    private final UserProfileRepository userProfileRepository;
    private final String uploadDirectory = "uploads/cvs/";
    private final JobApplicationMapper applicationMapper;

    public JobApplicationService(CandidateApplicationRepository applicationRepository,
                                 JobOfferRepository jobOfferRepository,
                                 CvRepository cvRepository,
                                 UserProfileRepository userProfileRepository,
                                 JobApplicationMapper applicationMapper) {
        this.applicationRepository = applicationRepository;
        this.jobOfferRepository = jobOfferRepository;
        this.cvRepository = cvRepository;
        this.userProfileRepository = userProfileRepository;
        this.applicationMapper = applicationMapper;
    }

    public record FileDownloadModel(String fileName, String filePath) {}

    @Transactional
    public void applyToJobWithFile(String jobOfferId, MultipartFile file, String userId) throws IOException {

        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> new IllegalArgumentException("Job offer not found!"));

        if (applicationRepository.existsByUserIdAndJobOfferId(userId, jobOffer.getId())) {
            throw new IllegalStateException("You have already applied for this job!");
        }

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty!");
        }

        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;

        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        CV cvEntity = new CV();
        cvEntity.setUserId(userId);
        cvEntity.setFileName(originalFileName);
        cvEntity.setFilePath(filePath.toString());
        cvEntity.setUploadedAt(LocalDateTime.now());
        CV savedCv = cvRepository.save(cvEntity);

        CandidateApplication application = new CandidateApplication();
        application.setUserId(userId);
        application.setJobOffer(jobOffer);
        application.setCvId(savedCv.getId());
        application.setAppliedAt(LocalDateTime.now());
        application.setStatus(ApplicationStatus.PENDING);

        String hrManager = jobOffer.getReviewedBy();
        application.setReviewedBy(hrManager);

        applicationRepository.save(application);
    }

    public List<ApplicationHistoryDTO> getApplicationsByUserId(String userId) {
        List<CandidateApplication> candidateApplications = applicationRepository.findByUserId(userId);

        if (candidateApplications.isEmpty()) {
            return List.of();
        }

        UserProfile applicant = userProfileRepository.findById(userId).orElse(null);

        List<String> cvIds = candidateApplications.stream()
                .map(CandidateApplication::getCvId)
                .filter(cvId -> cvId != null)
                .distinct()
                .toList();

        Map<String, CV> cvMap = cvRepository.findAllById(cvIds).stream()
                .collect(Collectors.toMap(CV::getId, cv -> cv));

        return candidateApplications.stream()
                .map(app -> applicationMapper.toHistoryDto(
                        app,
                        applicant,
                        cvMap.get(app.getCvId())
                ))
                .toList();
    }

    public List<ApplicationHistoryDTO> getApplicationsByJobOffer(String id, Authentication authentication) {

        JobOffer offer = jobOfferRepository.findJobOfferById(id).
                orElseThrow(() -> new Exception.ResourceNotFoundException("Couldn't find job offer with id: " + id));

        boolean isOwner = offer.getReviewedBy().equals(authentication.getName());
        boolean isAdmin = false;

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            String userRole = jwtAuth.getToken().getClaimAsString("role");
            if (userRole != null) {
                isAdmin = userRole.equalsIgnoreCase("ROLE_ADMIN") || userRole.equalsIgnoreCase("ROLE_SUPER_ADMIN");
            }
        }

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You are not authorized to view applications for this job offer.");
        }

        List<CandidateApplication> candidateApplications = applicationRepository.findApplicationsByJobOfferId(id);

        List<String> userIds = candidateApplications.stream()
                .map(CandidateApplication::getUserId)
                .distinct()
                .toList();

        List<String> cvIds = candidateApplications.stream()
                .map(CandidateApplication::getCvId)
                .filter((cvId -> cvId != null))
                .distinct()
                .toList();

        Map<String, UserProfile> userProfileMap = userProfileRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(UserProfile::getId, profile -> profile));

        Map<String, CV> cvMap = cvRepository.findAllById(cvIds).stream()
                .collect(Collectors.toMap(CV::getId, cv -> cv));

        return candidateApplications.stream()
                .map(app -> applicationMapper.toHistoryDto(
                        app,
                        userProfileMap.get(app.getUserId()),
                        cvMap.get(app.getCvId())
                ))
                .toList();
    }

    public FileDownloadModel getCvFileForApplication(String applicationId, String currentUserId, Authentication authentication) {

        CandidateApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new Exception.ResourceNotFoundException("Application not found with id: " + applicationId));

        boolean isOwner = application.getUserId().equals(currentUserId);
        boolean isHR = false;
        boolean isAdmin = false;

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            String userRole = jwtAuth.getToken().getClaimAsString("role");

            if (userRole != null) {
                isHR = userRole.equalsIgnoreCase("ROLE_HR") || userRole.equalsIgnoreCase("HR");
                isAdmin = userRole.equalsIgnoreCase("ROLE_ADMIN") || userRole.equalsIgnoreCase("ADMIN");
            }
        }

        if (!isOwner && !isAdmin && !isHR) {
            throw new AccessDeniedException("You are not authorized to download this file!");
        }

        if (application.getCvId() == null) {
            throw new IllegalArgumentException("This application does not have a CV attached.");
        }

        CV cvEntity = cvRepository.findById(application.getCvId())
                .orElseThrow(() -> new Exception.ResourceNotFoundException("CV not found for this application."));

        return new FileDownloadModel(cvEntity.getFileName(), cvEntity.getFilePath());
    }

    private ApplicationHistoryDTO convertToAppHistoryDTO(String appId) {

        CandidateApplication app = applicationRepository.findById(appId)
                .orElseThrow(() -> new Exception.ResourceNotFoundException("Couldn't find the resource with id: " + appId));
        UserProfile applicant = userProfileRepository.findById(app.getUserId()).orElse(null);

        CV cv = null;
        if (app.getCvId() != null) {
            cv = cvRepository.findById(app.getCvId()).orElse(null);
        }

        return applicationMapper.toHistoryDto(app, applicant, cv);
    }

    @Transactional
    public ApplicationHistoryDTO updateApplicationReview(String appId, UpdateJobApplicationDTO request, Authentication authentication){

        CandidateApplication application = applicationRepository.findById(appId).orElseThrow(
                ()-> new Exception.ResourceNotFoundException("Couldn't find application with id: " + appId));
        application.setStatus(request.getStatus());
        application.setRecruiterNotes(request.getRecruiterNotes());
        application.setReviewedAt(LocalDateTime.now());

        CandidateApplication savedApp = applicationRepository.save(application);

        return applicationMapper.appToDto(savedApp);
    }

    @Transactional
    public void deleteApplicationWithCv(String appId){

        CandidateApplication application = applicationRepository.findById(appId).orElseThrow(
                () -> new Exception.ResourceNotFoundException("Couldn't find application with id: " + appId));

        CV cv =  cvRepository.findById(application.getCvId()).orElseThrow(
                () -> new Exception.ResourceNotFoundException("CV not found for this application."));

        applicationRepository.delete(application);
        cvRepository.delete(cv);
    }
}
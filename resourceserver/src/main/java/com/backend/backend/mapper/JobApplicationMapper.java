package com.backend.backend.mapper;

import com.backend.backend.dto.ApplicationHistoryDTO;
import com.backend.backend.entity.CV;
import com.backend.backend.entity.CandidateApplication;
import com.backend.backend.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {

    ApplicationHistoryDTO appToDto(CandidateApplication entity);

    CandidateApplication dtoToEntity(ApplicationHistoryDTO dto);

    @Mapping(source = "app.id", target = "id")
    @Mapping(source = "app.appliedAt", target = "appliedAt")
    @Mapping(source = "app.candidateStatus", target = "status")
    @Mapping(source = "app.recruiterNotes", target = "recruiterNotes")
    @Mapping(source = "app.reviewedBy", target = "reviewedBy")
    @Mapping(source = "app.reviewedAt", target = "reviewedAt")

    @Mapping(source = "applicant.firstName", target = "firstName", defaultValue = "Deleted")
    @Mapping(source = "applicant.lastName", target = "lastName", defaultValue = "User")
    @Mapping(source = "applicant.email", target = "email", defaultValue = "N/A")

    @Mapping(target = "jobTitle", expression = "java(app.getJobOffer() != null ? app.getJobOffer().getJobTitle() : \"Unknown Position (Job may be deleted)\")")
    @Mapping(target = "jobDescription", expression = "java(app.getJobOffer() != null ? app.getJobOffer().getDescription() : \"No Description (Job may be deleted)\")")

    @Mapping(target = "cvFileName", expression = "java(cv != null ? cv.getFileName() : \"No CV\")")
    ApplicationHistoryDTO toHistoryDto(CandidateApplication app, UserProfile applicant, CV cv);
}

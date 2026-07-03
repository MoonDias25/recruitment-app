package com.backend.backend.mapper;

import com.backend.backend.dto.CreateJobOfferDTO;
import com.backend.backend.dto.JobOfferAdminDTO;
import com.backend.backend.dto.JobOffersDTO;
import com.backend.backend.entity.JobOffer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobOfferMapper {

    @Mapping(source = "reviewedBy", target = "reviewedBy")
    JobOfferAdminDTO offerToAdminDTO(JobOffer offer);

    @Mapping(source = "reviewedBy", target = "reviewedBy")
    JobOffer adminDtoToOffer(JobOfferAdminDTO dto);

    CreateJobOfferDTO offerToDTO(JobOffer offer);

    JobOffer dtoToOffer(CreateJobOfferDTO dto);

    JobOffersDTO offerToJobOffersDTO(JobOffer offer);

    JobOffer jobOffersDtoToOffer(JobOffersDTO dto);
}

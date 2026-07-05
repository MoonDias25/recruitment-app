package com.backend.backend.controller;

import com.backend.backend.dto.JobOffersDTO;
import com.backend.backend.dto.JobOfferAdminDTO;
import com.backend.backend.handler.GlobalExceptionHandler;
import com.backend.backend.service.JobOfferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class JobOfferControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private JobOfferService jobOfferService;

    @InjectMocks
    private JobOfferController jobOfferController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.objectMapper = new ObjectMapper();

        this.mockMvc = MockMvcBuilders.standaloneSetup(jobOfferController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllActiveJobs_ShouldReturnPagedModel_WhenCalled() throws Exception {
        JobOffersDTO offerDto = new JobOffersDTO();
        PageImpl<JobOffersDTO> page = new PageImpl<>(List.of(offerDto), PageRequest.of(0, 6), 1);
        PagedModel<JobOffersDTO> pagedModel = new PagedModel<>(page);

        when(jobOfferService.getAllActiveJobOffers(any(), anyInt(), anyInt(), anyString()))
                .thenReturn(pagedModel);

        // Act & Assert
        mockMvc.perform(get("/api/jobs/all-jobs")
                        .param("page", "0")
                        .param("size", "6")
                        .param("sortOption", "applicationEndDate-desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(jobOfferService, times(1))
                .getAllActiveJobOffers(null, 0, 6, "applicationEndDate-desc");
    }

    @Test
    void getAllJobOffers_ShouldReturnAdminList_WhenUserIsAuthorized() throws Exception {
        JobOfferAdminDTO adminDto = new JobOfferAdminDTO();
        when(jobOfferService.getAllJobOffersForAdmin()).thenReturn(List.of(adminDto));

        mockMvc.perform(get("/api/jobs/admin/job-offers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(jobOfferService, times(1)).getAllJobOffersForAdmin();
    }

    @Test
    void updateJobOffer_ShouldReturnUpdatedAdminDto_WhenAdminPayloadIsValid() throws Exception {
        String jobId = "job-abc-123";
        JobOfferAdminDTO requestDto = new JobOfferAdminDTO();
        JobOfferAdminDTO responseDto = new JobOfferAdminDTO();

        when(jobOfferService.updateOffer(eq(jobId), any(JobOfferAdminDTO.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/jobs/admin/job-offers/update/{id}", jobId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        verify(jobOfferService, times(1)).updateOffer(eq(jobId), any(JobOfferAdminDTO.class));
    }

    @Test
    void deleteJobOffer_ShouldReturnNoContent_WhenIdIsValid() throws Exception {
        // Arrange
        String jobId = "job-xyz";
        Authentication mockAuth = mock(Authentication.class);

        // Act & Assert
        mockMvc.perform(delete("/api/jobs/delete/{id}", jobId)
                        .principal(mockAuth)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(jobOfferService, times(1)).deleteOffer(jobId, mockAuth);
    }
}
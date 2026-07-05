package com.backend.backend.controller;

import com.backend.backend.handler.GlobalExceptionHandler;
import com.backend.backend.service.JobApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JobApplicationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JobApplicationService applicationService;

    @InjectMocks
    private JobApplicationController jobApplicationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(jobApplicationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void applyToJob_ShouldReturnOk_WhenFileIsPdf() throws Exception {
        MockMultipartFile pdfFile = new MockMultipartFile(
                "file", "cv.pdf", MediaType.APPLICATION_PDF_VALUE, "dummy pdf content".getBytes()
        );

        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getName()).thenReturn("user-123");

        mockMvc.perform(multipart("/apply")
                        .file(pdfFile)
                        .param("jobOfferId", "job-999")
                        .principal(mockAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application submitted successfully with your CV!"));

        verify(applicationService, times(1)).applyToJobWithFile("job-999", pdfFile, "user-123");
    }

    @Test
    void applyToJob_ShouldReturnBadRequest_WhenFileIsInvalidType() throws Exception {
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file", "photo.png", MediaType.IMAGE_PNG_VALUE, "dummy png content".getBytes()
        );

        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getName()).thenReturn("user-123");

        mockMvc.perform(multipart("/apply")
                        .file(invalidFile)
                        .param("jobOfferId", "job-999")
                        .principal(mockAuth))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Only PDF and Word documents are allowed!"));

        verifyNoInteractions(applicationService);
    }
}
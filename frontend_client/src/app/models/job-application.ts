import { JobOffer } from "./job-offer";

export interface ApplicationResponse {
  message: string;
}

export interface CandidateApplication{
  id: string;
  jobTitle: string;
  jobDescription: string;
  appliedAt: string;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED' | 'REVIEWED';
  recruiterNotes?: string;
  reviewedBy?: string;
  reviewedAt?: string;
  cvFileName: string;
  firstName: string;
  lastName: string;
  email: string;
}

export interface UpdateJobApplicationRequest {
  status: string;
  recruiterNotes: string;
}
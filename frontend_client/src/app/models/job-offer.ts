export interface JobOffer {
    id: string;
    jobTitle: string;
    jobStatus: 'ACTIVE' | 'INACTIVE' | 'PENDING';
    applicationStartDate: string;
    applicationEndDate: string;
    description: string;
    minSalary: number;
    maxSalary: number;
    reviewedBy: string | null;
}

export interface AllJobOffers {
  id: string;
  jobTitle: string;
  description: string;
  minSalary: number;
  maxSalary: number;
  applicationEndDate: string; 
}

export interface SpringPageResponse {
  content: AllJobOffers[];
  page: {
    size: number;
    number: number;
    totalElements: number;
    totalPages: number;
  };
}

export interface JobOfferRequest {
  jobTitle: string;
  description: string;
  minSalary: number;
  maxSalary: number;
  applicationEndDate: string; 
}

export interface JobCreationResponse {
  message: string;
  id: string; 
}

export interface JobOfferAdminDTO {
  id: string;
  jobTitle: string;
  description: string;
  minSalary: number;
  maxSalary: number;
  jobStatus: string; 
  applicationStartDate: string; 
  applicationEndDate: string;
  reviewedBy: string | null;
}

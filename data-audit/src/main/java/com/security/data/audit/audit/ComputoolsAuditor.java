//package com.security.data.audit.audit;
//
//import com.security.data.audit.details.CompUserDetails;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import javax.persistence.PrePersist;
//import javax.persistence.PreUpdate;
//
//public class ComputoolsAuditor  {
//
//    @PrePersist
//    public void getCurrentAuditor(CellPhone cellPhone) {
//        cellPhone.setOwner(getCurrentUser());
//        cellPhone.setCreatedBy(getCurrentUser());
//        cellPhone.setUpdatedBy(getCurrentUser());
//    }
//
//    @PreUpdate
//    public void setUpdatedBy(CellPhone cellPhone){
//        cellPhone.setUpdatedBy(getCurrentUser());
//    }
//
//    private CompUser getCurrentUser(){
//        return ((CompUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
//    }
//}

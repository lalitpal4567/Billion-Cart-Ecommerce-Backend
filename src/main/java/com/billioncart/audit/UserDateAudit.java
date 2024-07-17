package com.billioncart.audit;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class UserDateAudit extends DateAudit{
	@CreatedBy
	@Column(name = "created_by")
	private String createdBy;
	
	@LastModifiedBy
	@Column(name = "updated_by")
	private String UpdatedBy;	
}

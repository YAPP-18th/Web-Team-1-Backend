package com.yapp18.retrospect.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter @Setter
@MappedSuperclass // JPA Entity 클래스들이 BaseTimeEntity를 상속할 경우 필드(createdDate, modifiedDate)들도 칼럼으로 인식하도록 합니다.
@EntityListeners(AuditingEntityListener.class)//BaseTimeEntity 클래스에 Auditing 기능을 포함시킵니다.
public abstract class BaseTimeEntity {

    @CreatedDate// Entity가 생성되어 저장될 때 시간이 자동 저장됩니다.
    @Column(name ="created_at")
    protected LocalDateTime createdAt;

    @LastModifiedDate// 조회한 Entity의 값을 변경할 때 시간이 자동 저장됩니다.
    @Column(name = "modified_at")
    protected LocalDateTime modifiedAt;
}


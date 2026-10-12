package net.likelion.practice.jpasns.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass   //  공통 매핑 정보를 담고 있는 추상 클래스라고 선언
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
    @CreatedDate    // 엔티티가 최초 저장될 때 생성일시를 자동으로 주입
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate   // 엔티티가 등록/수정될 때 수정일시를 자동으로 주입
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

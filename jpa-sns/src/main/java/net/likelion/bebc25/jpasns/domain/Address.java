package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable // 기존 address 테이블에서 컬럼을 추가하고 싶은 것일때 다른 Entity에 내장되어 있을 때
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(length = 10)
    private String zipcode;

    @Column
    private String roadAddress;

    @Column(length = 100)
    private String detailAddress;

    public Address(String zipcode, String roadAddress, String detailAddress) {
        this.zipcode = zipcode;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;


    }
}

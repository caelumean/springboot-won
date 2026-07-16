package net.likelion.bebc25.member01.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private int id;

    private String user_id;
    private String password;
    private String name;
    private String email;
    private LocalDate createdAt;
}

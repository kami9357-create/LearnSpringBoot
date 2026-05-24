package com.devamateur.administrator.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity // Báo cho Spring (JPA) biết đây là một thực thể CSDL
@Table(name = "users") // Đặt tên bảng trong CSDL là "users"
@Data // Tự động sinh getter, setter, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE) // Mặc định tất cả các fields đều là private
public class User {
    @Id  // Đánh dấu 'id' là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dob;
    Set<String> roles;
}

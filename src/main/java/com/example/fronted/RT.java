package com.example.fronted;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class RT {

    private String sex;
    private String interest;
    private String city;
    private Long idTelegramUser;
    private String avatarPhotoId;
    private String name;
    private Integer age;
    private String description;
}
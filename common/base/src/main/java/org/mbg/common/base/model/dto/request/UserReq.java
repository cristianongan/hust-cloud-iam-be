package org.mbg.anm.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 2313939242662473639L;

    private Long id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private int gender;

    private LocalDate dob;

    private String address;

    private int status;

    private String fullname;

    private int type;

    private List<Long> ids;

    private List<String> roles;
}

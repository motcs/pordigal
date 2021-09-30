package com.prodigal.security.user;

import com.prodigal.security.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;

/**
 * @author jjh
 * @classname User
 * @date 2021/9/29 create
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "prodigal_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String username;
    private String password;

    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();

    @CreatedBy
    private LocalDateTime createTime;
    @LastModifiedBy
    private LocalDateTime updateTime;

}

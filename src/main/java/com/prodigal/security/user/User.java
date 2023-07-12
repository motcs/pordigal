package com.prodigal.security.user;

import com.prodigal.security.entity.BaseEntity;
import com.prodigal.security.role.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-26 星期一
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "用户表")
@Entity(name = "prodigal_user")
public class User implements Serializable, BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String username;

    private String phone;

    private String avatar;

    private String password;

    @ManyToMany(fetch = EAGER)
    private Collection<Role> role = new ArrayList<>();

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

}

package com.prodigal.security.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author jjh
 * @classname Role
 * @date 2021/9/29 create
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "prodigal_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}

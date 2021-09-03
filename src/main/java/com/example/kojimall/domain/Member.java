package com.example.kojimall.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(schema = "kojimall")
public class Member {
    
    @Id @GeneratedValue
    @Column(name = "mem_id")
    private Long id;
    @Column(name = "mem_nm")
    private String name;
    @Column(name = "mem_eml")
    private String email;
    @Column(name = "mem_pw")
    private String password;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "cd", referencedColumnName = "cd"),
            @JoinColumn(name = "grp_cd", referencedColumnName = "grp_cd")
    })
    private Code memberRole;

    @Embedded
    private Address address;

    @Column(name = "reg_dt")
    private LocalDateTime regDate;
    @Column(name = "mod_dt")
    private LocalDateTime modDate;
}

package com.logmaven.exmaven.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "promotion")
@AllArgsConstructor
@NoArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Basic(optional = false)
    @Column(name = "startdate")
    private Date startdate;


    @Basic(optional = false)
    @Column(name = "enddate")
    private Date enddate;

    @Lob
    @Column(name = "banner", nullable = false)
    private byte[] banner;


    @ManyToOne
    @JoinColumn(name ="user_id",referencedColumnName = "id")
    private User user_id;


}

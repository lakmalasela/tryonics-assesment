package com.logmaven.exmaven.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_has_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHasRole implements Serializable {


    @Id
    @ManyToOne
    @JoinColumn(name ="user_id",referencedColumnName = "id")
    private User user_id;

    @Id
    @ManyToOne
    @JoinColumn(name ="role_id",referencedColumnName = "id")
    private Role role_id;


}

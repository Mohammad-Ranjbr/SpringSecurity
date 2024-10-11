package com.example.SpringSecurity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "authorities")
public class Authority {

    // Authority: A specific permission or action that a user can perform. For example, "View Account" or "View Cards". These licenses refer to specific and individual operations.
    // Role: A set of permissions or actions defined as a group. Roles typically contain multiple actions or permissions. For example,
    // the "User" role could include permissions such as "View Account" and "View Cards", and the "Administrator" role could include "Update Account" and "Update Cards" permissions.
    // Using roles to reduce complexity:
    // In real projects, there may be thousands of operations that users can perform. Defining all these operations separately as authorities can be a complex and time-consuming process.
    // To reduce this complexity, we can group several operations in a role. This reduces the number of configurations in Spring Security and simplifies the management of permissions.
    //Access level:
    // Fine-grained (partial access): If we use authorities, access is controlled in a detailed and detailed manner, that is, we define a permission for each specific action.
    // Coarse-grained (general access): If we use roles, access is controlled more generally and at a high level, that is, we group several permissions in a role and define general access.
    // Use the prefix for roles:
    // In Spring Security, when you define roles, you must use a specific prefix for them. By default, this prefix is ROLE_. For example,
    // if you have a role called "Admin", you should define it as ROLE_ADMIN. However,
    // Spring Security allows you to change this default and define a custom prefix. This is done by creating a GrantedAuthorityDefaults bean.
    //@Bean
    //static GrantedAuthorityDefaults grantedAuthorityDefaults(){
    //    return new GrantedAuthorityDefaults("MYPREFIX_");
    //}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}

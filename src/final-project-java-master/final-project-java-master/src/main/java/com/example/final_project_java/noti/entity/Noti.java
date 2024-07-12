package com.example.final_project_java.noti.entity;

import com.example.final_project_java.userapi.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "noti_tbl")
public class Noti {

   @Id
   @Column(name = "noti_id")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int notiId;

   @Column(nullable = false)
   private String notiTitle;

   @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinColumns({
         @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
         @JoinColumn(name = "email", referencedColumnName = "email"),
         @JoinColumn(name = "login_method", referencedColumnName = "login_method")
   })
   private User notiWriter;

   @Column(nullable = false)
   private String notiContent;

   @CreationTimestamp
   private LocalDateTime regDate;

   @UpdateTimestamp
   private LocalDateTime updatedDate;

   private int views;


}













































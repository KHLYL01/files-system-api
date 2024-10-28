package com.conquer_team.files_system.model.entity;

import com.conquer_team.files_system.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String fullname;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enable;

    private String verificationCode;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER,orphanRemoval = true)
    private List<File> listOfFiles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserFolder> userFolders;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Folder> folders;

//    public void addFolder(Folder folder){
//        if(folders == null){
//            folders = new ArrayList<>();
//            folders.add(folder);
//        }else {
//            folders.add(folder);
//        }
//    }

//    public void addFile(File file){
//        if(listOfFiles == null){
//            listOfFiles = new ArrayList<>();
//            listOfFiles.add(file);
//        }else {
//            listOfFiles.add(file);
//        }
//    }

//    public void addUserFolders(UserFolder userFolder){
//        if(userFolders == null){
//            userFolders = new ArrayList<>();
//            userFolders.add(userFolder);
//        }else {
//            userFolders.add(userFolder);
//        }
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }
}

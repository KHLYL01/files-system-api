package com.conquer_team.files_system.config;

import com.conquer_team.files_system.model.dto.requests.LoginRequest;
import com.conquer_team.files_system.model.dto.response.LoginResponse;
import com.conquer_team.files_system.model.entity.Folder;
import com.conquer_team.files_system.model.entity.User;
import com.conquer_team.files_system.model.enums.Role;
import com.conquer_team.files_system.repository.FolderRepo;
import com.conquer_team.files_system.repository.UserRepo;
import com.conquer_team.files_system.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Startup implements CommandLineRunner {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final FolderRepo folderRepo;


    @Override
    public void run(String... args){
        if (userRepo.findAll().isEmpty()) {
            User admin = User.builder()
                    .fullname("admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("12345678"))
                    .role(Role.ADMIN)
                    .verificationCode("123456")
                    .listOfFiles(null)
                    .enable(true)
                    .build();
            User user = User.builder()
                    .fullname("user")
                    .email("user@gmail.com")
                    .password(passwordEncoder.encode("12345678"))
                    .role(Role.USER)
                    .verificationCode("123456")
                    .listOfFiles(null)
                    .enable(true)
                    .build();

            Folder adminFolder = Folder.builder()
                    .name("Admin Folder")
                    .user(admin)
                    .build();

            Folder userFolder = Folder.builder()
                    .name("User Folder")
                    .user(user)
                    .build();


            userRepo.save(admin);
            userRepo.save(user);
            folderRepo.save(adminFolder);
            folderRepo.save(userFolder);
        }

        LoginResponse adminLogin = authService.login(
                LoginRequest.builder()
                        .email("admin@gmail.com")
                        .password("12345678")
                        .fcmToken("eogQzu94a6BcTt_wfph7uH:APA91bEujxERyNWOno4Xe_yhjPvZzkWmDt8F2jVciXIBzs0JFjq3ExdjjtZwtM_nx-e8p0_Q5xVL8ld0KB6wSHwsvTllVlnl0PJMO0ZMOupniESSRUM0TEM")
                        .build()
        );
        LoginResponse userLogin = authService.login(
                LoginRequest.builder()
                        .email("user@gmail.com")
                        .password("12345678")
                        .fcmToken("eogQzu94a6BcTt_wfph7uH:APA91bEujxERyNWOno4Xe_yhjPvZzkWmDt8F2jVciXIBzs0JFjq3ExdjjtZwtM_nx-e8p0_Q5xVL8ld0KB6wSHwsvTllVlnl0PJMO0ZMOupniESSRUM0TEM")
                        .build()
        );
        printInfo(adminLogin);
        printInfo(userLogin);

    }

    private void printInfo(LoginResponse response){
        System.out.println("+=====================+ "+ response.getRole() +" +=====================+");
        System.out.println("Id: "+ response.getId());
        System.out.println("Fullname: "+ response.getFullname());
        System.out.println("Email: "+ response.getEmail());
        System.out.println("Role: "+ response.getRole());
        System.out.println("Token: "+ response.getToken());
        System.out.println("Refresh token: "+ response.getRefreshToken());
    }



  //  {"title":"New Join Request for Your Group","message":"userhas requested to join the group [Admin Folder]. Review and approve or deny the request.","folderId":null,"userId":1}

}

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

//
//            User user1 = User.builder()
//                    .fullname("user1")
//                    .email("user1@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//            User user2 = User.builder()
//                    .fullname("user2")
//                    .email("user2@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//                        User admin2 = User.builder()
//                    .fullname("admin")
//                    .email("admin2@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.ADMIN)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//            User user2 = User.builder()
//                    .fullname("user2")
//                    .email("user@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//                        User admin3 = User.builder()
//                    .fullname("admin")
//                    .email("admin3@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.ADMIN)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//            User user3 = User.builder()
//                    .fullname("user")
//                    .email("user3@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//                        User admin4 = User.builder()
//                    .fullname("admin")
//                    .email("admin4@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.ADMIN)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//            User user4 = User.builder()
//                    .fullname("user")
//                    .email("user4@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//                        User admin5 = User.builder()
//                    .fullname("admin")
//                    .email("admin5@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.ADMIN)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//            User user5 = User.builder()
//                    .fullname("user")
//                    .email("user6@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//                        User admin6 = User.builder()
//                    .fullname("admin")
//                    .email("admin7@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.ADMIN)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//            User user6 = User.builder()
//                    .fullname("user")
//                    .email("user8@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//                        User admin7 = User.builder()
//                    .fullname("admin")
//                    .email("admin9@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.ADMIN)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//            User user7 = User.builder()
//                    .fullname("user")
//                    .email("user10@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//                        User admin8 = User.builder()
//                    .fullname("admin")
//                    .email("admin11@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.ADMIN)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//            User user8 = User.builder()
//                    .fullname("user")
//                    .email("user11@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//                        User admin9 = User.builder()
//                    .fullname("admin")
//                    .email("admin12@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.ADMIN)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//            User user9 = User.builder()
//                    .fullname("user")
//                    .email("user12@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//                        User admin10 = User.builder()
//                    .fullname("admin")
//                    .email("admin13@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.ADMIN)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//            User user10 = User.builder()
//                    .fullname("user")
//                    .email("user13@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//                        User admin11 = User.builder()
//                    .fullname("admin")
//                    .email("admin14@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.ADMIN)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//            User user11 = User.builder()
//                    .fullname("user")
//                    .email("user14@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//                        User admin12 = User.builder()
//                    .fullname("admin")
//                    .email("admin15@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.ADMIN)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//            User user12 = User.builder()
//                    .fullname("user")
//                    .email("user15@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//                        User admin13 = User.builder()
//                    .fullname("admin")
//                    .email("admin16@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.ADMIN)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//            User user13 = User.builder()
//                    .fullname("user")
//                    .email("user16@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//                        User admin14 = User.builder()
//                    .fullname("admin")
//                    .email("admin17@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.ADMIN)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();
//
//            User user14 = User.builder()
//                    .fullname("user")
//                    .email("user17@gmail.com")
//                    .password(passwordEncoder.encode("12345678"))
//                    .role(Role.USER)
//                    .verificationCode("123456")
//                    .listOfFiles(null)
//                    .enable(true)
//                    .build();


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
//            userRepo.save(user1);
//            userRepo.save(user2);

            folderRepo.save(userFolder);
            folderRepo.save(adminFolder);
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

//                LoginResponse userLogin1 = authService.login(
//                LoginRequest.builder()
//                        .email("1@gmail.com")
//                        .password("12345678")
//                        .fcmToken("eogQzu94a6BcTt_wfph7uH:APA91bEujxERyNWOno4Xe_yhjPvZzkWmDt8F2jVciXIBzs0JFjq3ExdjjtZwtM_nx-e8p0_Q5xVL8ld0KB6wSHwsvTllVlnl0PJMO0ZMOupniESSRUM0TEM")
//                        .build()
//        );
//        LoginResponse userLogin1 = authService.login(
//                LoginRequest.builder()
//                        .email("user1@gmail.com")
//                        .password("12345678")
//                        .fcmToken("eogQzu94a6BcTt_wfph7uH:APA91bEujxERyNWOno4Xe_yhjPvZzkWmDt8F2jVciXIBzs0JFjq3ExdjjtZwtM_nx-e8p0_Q5xVL8ld0KB6wSHwsvTllVlnl0PJMO0ZMOupniESSRUM0TEM")
//                        .build()
//        );
//        LoginResponse userLogin2 = authService.login(
//                LoginRequest.builder()
//                        .email("user2@gmail.com")
//                        .password("12345678")
//                        .fcmToken("eogQzu94a6BcTt_wfph7uH:APA91bEujxERyNWOno4Xe_yhjPvZzkWmDt8F2jVciXIBzs0JFjq3ExdjjtZwtM_nx-e8p0_Q5xVL8ld0KB6wSHwsvTllVlnl0PJMO0ZMOupniESSRUM0TEM")
//                        .build()
//        );
//
//                LoginResponse adminLogin2 = authService.login(
//                LoginRequest.builder()
//                        .email("admin2@gmail.com")
//                        .password("12345678")
//                        .fcmToken("eogQzu94a6BcTt_wfph7uH:APA91bEujxERyNWOno4Xe_yhjPvZzkWmDt8F2jVciXIBzs0JFjq3ExdjjtZwtM_nx-e8p0_Q5xVL8ld0KB6wSHwsvTllVlnl0PJMO0ZMOupniESSRUM0TEM")
//                        .build()
//        );
//        LoginResponse userLogin2 = authService.login(
//                LoginRequest.builder()
//                        .email("user2@gmail.com")
//                        .password("12345678")
//                        .fcmToken("eogQzu94a6BcTt_wfph7uH:APA91bEujxERyNWOno4Xe_yhjPvZzkWmDt8F2jVciXIBzs0JFjq3ExdjjtZwtM_nx-e8p0_Q5xVL8ld0KB6wSHwsvTllVlnl0PJMO0ZMOupniESSRUM0TEM")
//                        .build()
//        );
//
//                LoginResponse adminLogin3 = authService.login(
//                LoginRequest.builder()
//                        .email("admin3@gmail.com")
//                        .password("12345678")
//                        .fcmToken("eogQzu94a6BcTt_wfph7uH:APA91bEujxERyNWOno4Xe_yhjPvZzkWmDt8F2jVciXIBzs0JFjq3ExdjjtZwtM_nx-e8p0_Q5xVL8ld0KB6wSHwsvTllVlnl0PJMO0ZMOupniESSRUM0TEM")
//                        .build()
//        );
//        LoginResponse userLogin3 = authService.login(
//                LoginRequest.builder()
//                        .email("user4@gmail.com")
//                        .password("12345678")
//                        .fcmToken("eogQzu94a6BcTt_wfph7uH:APA91bEujxERyNWOno4Xe_yhjPvZzkWmDt8F2jVciXIBzs0JFjq3ExdjjtZwtM_nx-e8p0_Q5xVL8ld0KB6wSHwsvTllVlnl0PJMO0ZMOupniESSRUM0TEM")
//                        .build()
//        );
//
//
//        printInfo(adminLogin);
//        printInfo(userLogin);
//        printInfo(userLogin1);
//        printInfo(userLogin2);
//        printInfo(userLogin3);
//       // printInfo(adminLogin1);
        printInfo(adminLogin);
        printInfo(userLogin);
//        printInfo(userLogin1);
       // printInfo(userLogin2);
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

package com.mabphun.HotelServer.services.auth;

import com.mabphun.HotelServer.dto.AuthenticationResponse;
import com.mabphun.HotelServer.dto.LoginRequest;
import com.mabphun.HotelServer.dto.SingupRequest;
import com.mabphun.HotelServer.dto.UserDto;
import com.mabphun.HotelServer.entity.User;
import com.mabphun.HotelServer.enums.UserRole;
import com.mabphun.HotelServer.repository.UserRepository;
import com.mabphun.HotelServer.services.jwt.UserService;
import com.mabphun.HotelServer.utill.JwtUtil;
import jakarta.annotation.PostConstruct;

import jakarta.persistence.EntityExistsException;
import org.hibernate.QueryParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    public UserDto createUser(SingupRequest singupRequest) {
        if (userRepository.findFirstByEmail(singupRequest.getEmail()).isPresent()) {
            throw new EntityExistsException("User already exists with email: " + singupRequest.getEmail());
        }

        User user = new User();
        user.setEmail(singupRequest.getEmail());
        user.setName(singupRequest.getName());
        user.setUserRole(UserRole.CUSTOMER);
        user.setPassword(new BCryptPasswordEncoder().encode(singupRequest.getPassword()));

        User createdUser = userRepository.save(user);
        return createdUser.getUserDto();
    }

    public AuthenticationResponse loginUser(LoginRequest request){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid email or password");
        }

        final UserDetails userDetails = userService.getUserDetailsService().loadUserByUsername(request.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        AuthenticationResponse response = new AuthenticationResponse();
        if(optionalUser.isPresent()){
            response.setJwt(jwt);
            response.setId(optionalUser.get().getId());
            response.setUserRole(optionalUser.get().getUserRole());
        }
        return response;
    }

//    public UserDto loginUser(LoginRequest request) {
//        Optional<User> user = userRepository.findSingleByEmail(request.getEmail());
//        if (user.isEmpty()) {
//            throw new QueryParameterException("Invalid email");
//        }
//        System.out.println(user.get().getPassword());
//        System.out.println(new BCryptPasswordEncoder().encode(request.getPassword()));
//        if (!user.get().getPassword().equals(new BCryptPasswordEncoder().encode(request.getPassword()))) {
//            throw new QueryParameterException("Invalid password");
//        }
//
//        return user.get().getUserDto();
//    }

    @PostConstruct
    public void createAdminAccount(){
        Optional<User> admin = userRepository.findByUserRole(UserRole.ADMIN);
        if(admin.isEmpty()){
            User user = new User();
            Long id = user.getId();
            user.setEmail("admin@gmail.com");
            user.setName("admin");
            user.setUserRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));

            userRepository.save(user);
            System.out.println("Admin account created successfully");
        }else{
            System.out.println("Admin account already exists");
        }
    }
}

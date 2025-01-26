package com.mabphun.HotelServer.services.auth;

import com.mabphun.HotelServer.dto.AuthenticationResponse;
import com.mabphun.HotelServer.dto.LoginRequest;
import com.mabphun.HotelServer.dto.SingupRequest;
import com.mabphun.HotelServer.dto.UserDto;

public interface AuthService {
    UserDto createUser(SingupRequest singupRequest);

    AuthenticationResponse loginUser(LoginRequest request);
}

package com.mabphun.HotelServer.services.customer.room;

import com.mabphun.HotelServer.dto.RoomsResponseDto;

public interface RoomService {
    RoomsResponseDto getAvailableRooms(int pageNumber);
}

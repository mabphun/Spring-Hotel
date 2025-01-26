package com.mabphun.HotelServer.services.admin.rooms;

import com.mabphun.HotelServer.dto.RoomDto;
import com.mabphun.HotelServer.dto.RoomsResponseDto;

public interface RoomsService {
    boolean postRoom(RoomDto roomDto);
    RoomsResponseDto getAllRooms(int pageNumber);
    RoomDto getRoomById(Long roomId);
    boolean updateRoom(Long id, RoomDto roomDto);
    void deleteRoom(Long id);
}

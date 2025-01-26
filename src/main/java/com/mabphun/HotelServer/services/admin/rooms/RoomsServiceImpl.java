package com.mabphun.HotelServer.services.admin.rooms;

import com.mabphun.HotelServer.dto.RoomDto;
import com.mabphun.HotelServer.dto.RoomsResponseDto;
import com.mabphun.HotelServer.entity.Room;
import com.mabphun.HotelServer.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomsServiceImpl implements RoomsService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomsServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public boolean postRoom(RoomDto roomDto) {
        try{
            Room room = new Room();
            room.setName(roomDto.getName());
            room.setType(roomDto.getType());
            room.setPrice(roomDto.getPrice());
            room.setAvailable(true);

            roomRepository.save(room);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public RoomsResponseDto getAllRooms(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 6);
        Page<Room> roomPage = roomRepository.findAll(pageable);

        RoomsResponseDto roomsResponseDto = new RoomsResponseDto();
        roomsResponseDto.setPageNumber(roomPage.getPageable().getPageNumber());
        roomsResponseDto.setTotalPages(roomPage.getTotalPages());
        roomsResponseDto.setRooms(roomPage.stream().map(Room::getRoomDto).collect(Collectors.toList()));

        return roomsResponseDto;
    }

    public RoomDto getRoomById(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isPresent()){
            return room.get().getRoomDto();
        }
        else {
            throw new EntityNotFoundException("Room not found");
        }
    }

    public boolean updateRoom(Long id, RoomDto roomDto) {
        Optional<Room> optRoom = roomRepository.findById(id);
        if (optRoom.isPresent()) {
            Room oldRoom = optRoom.get();
            oldRoom.setName(roomDto.getName());
            oldRoom.setType(roomDto.getType());
            oldRoom.setPrice(roomDto.getPrice());
            oldRoom.setAvailable(roomDto.isAvailable());
            roomRepository.save(oldRoom);
            return true;
        }
        return false;
    }

    public void deleteRoom(Long id) {
        Optional<Room> optRoom = roomRepository.findById(id);
        if (optRoom.isPresent()) {
            roomRepository.delete(optRoom.get());
        } else{
            throw new EntityNotFoundException("Room not found");
        }
    }
}

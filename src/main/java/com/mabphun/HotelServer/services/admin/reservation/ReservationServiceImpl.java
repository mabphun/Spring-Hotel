package com.mabphun.HotelServer.services.admin.reservation;

import com.mabphun.HotelServer.dto.ReservationResponseDto;
import com.mabphun.HotelServer.entity.Reservation;
import com.mabphun.HotelServer.entity.Room;
import com.mabphun.HotelServer.enums.ReservationStatus;
import com.mabphun.HotelServer.repository.ReservationRepository;
import com.mabphun.HotelServer.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    public static final int SEARCH_RESULT_PER_PAGE = 4;


    public ReservationResponseDto getAllReservations(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, SEARCH_RESULT_PER_PAGE);
        Page<Reservation> reservations = reservationRepository.findAll(pageable);

        ReservationResponseDto reservationResponseDto = new ReservationResponseDto();
        reservationResponseDto.setReservationsDtoList(reservations.stream()
                .map(Reservation::getReservationDto).collect(Collectors.toList()));
        reservationResponseDto.setPageNumber(reservations.getNumber());
        reservationResponseDto.setTotalPages(reservations.getTotalPages());

        return reservationResponseDto;
    }

    public boolean changeReservationStatus(Long reservationId, String status) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();

            if (Objects.equals(status, "Approve")){
                reservation.setReservationStatus(ReservationStatus.APPROVED);
            }
            else{
                reservation.setReservationStatus(ReservationStatus.REJECTED);
            }

            reservationRepository.save(reservation);

            Room room = reservation.getRoom();
            room.setAvailable(false);

            roomRepository.save(room);

            return true;
        }
        return false;
    }
}

package com.mabphun.HotelServer.services.admin.reservation;

import com.mabphun.HotelServer.dto.ReservationResponseDto;

public interface ReservationService {
    ReservationResponseDto getAllReservations(int pageNumber);
    boolean changeReservationStatus(Long reservationId, String status);
}

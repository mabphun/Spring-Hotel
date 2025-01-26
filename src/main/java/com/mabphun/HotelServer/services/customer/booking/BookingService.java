package com.mabphun.HotelServer.services.customer.booking;

import com.mabphun.HotelServer.dto.ReservationDto;
import com.mabphun.HotelServer.dto.ReservationResponseDto;

public interface BookingService {
    boolean postReservation(ReservationDto reservationDto);
    ReservationResponseDto getAllReservationByUserId(Long userId, int pageNumber);
}

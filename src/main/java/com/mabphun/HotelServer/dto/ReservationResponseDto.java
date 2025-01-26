package com.mabphun.HotelServer.dto;

import java.util.List;

public class ReservationResponseDto {
    private int totalPages;
    private int pageNumber;

    private List<ReservationDto> reservationsDtoList;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<ReservationDto> getReservationsDtoList() {
        return reservationsDtoList;
    }

    public void setReservationsDtoList(List<ReservationDto> reservationsDtoList) {
        this.reservationsDtoList = reservationsDtoList;
    }
}

package service

import (
	"Task_6/internal/app/model"
	"Task_6/internal/app/repository"
)

type BookingsService struct {
	repo repository.Bookings
}

func NewBookingsService(repo repository.Bookings) *BookingsService {
	return &BookingsService{repo: repo}
}

func (s *BookingsService) CreateBooking(request model.BookingRequest) (model.BookingResponse, error) {
	amount, flightIds, err := s.repo.GetFlightPrice(request.Flights, request.FareConditions)
	request.FlightIds = flightIds
	if err != nil {
		return model.BookingResponse{}, err
	}
	var totalAmount float64
	for _, f := range amount {
		totalAmount += f
	}
	bookRef, err := s.repo.CreateBooking(totalAmount)
	if err != nil {
		return model.BookingResponse{}, err
	}
	tickets, err := s.repo.CreateTickets(request, bookRef, amount)
	if err != nil {
		return model.BookingResponse{}, err
	}
	return model.BookingResponse{
		BookRef:     bookRef,
		TotalAmount: totalAmount,
		Tickets:     tickets,
	}, nil
}

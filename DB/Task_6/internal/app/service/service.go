package service

import (
	"Task_6/internal/app/model"
	"Task_6/internal/app/repository"
)

type Cities interface {
	GetAllCities(limit string, page string, lang string) ([]string, error)
}

type Airports interface {
	GetAllAirports(limit string, page string, lang string) ([]model.Airport, error)
	GetAirportsInCity(city string, lang string) ([]model.Airport, error)
	GetScheduleForAirport(scheduleType string, airport string) (any, error)
}

type Flights interface {
}

type Bookings interface {
	CreateBooking(request model.BookingRequest) (model.BookingResponse, error)
}

type Checkins interface {
	CheckIn(ticketNo string, flightId int) (model.BoardingPass, error)
}

type Service struct {
	Cities
	Airports
	Flights
	Bookings
	Checkins
}

func NewService(repo *repository.Repository) *Service {
	return &Service{
		Cities:   NewCitiesService(repo),
		Airports: NewAirportsService(repo),
		Flights:  NewFlightsService(repo),
		Bookings: NewBookingsService(repo),
		Checkins: NewCheckinsService(repo),
	}
}

package repository

import (
	"Task_6/internal/app/model"
	"github.com/jmoiron/sqlx"
)

type Cities interface {
	GetAllCities(limit int, offset int, lang string) ([]string, error)
}

type Airports interface {
	GetAllAirports(limit int, offset int, lang string) ([]model.Airport, error)
	GetAirportsInCity(city string, lang string) ([]model.Airport, error)
	GetInboundScheduleForAirport(airport string) ([]model.InboundSchedule, error)
	GetOutboundScheduleForAirport(airport string) ([]model.OutboundSchedule, error)
}

type Flights interface {
}

type Bookings interface {
	GetFlightPrice(flightsIds []model.FlightData, fareConditions string) ([]float64, []int, error)
	CreateBooking(amount float64) (string, error)
	CreateTickets(info model.BookingRequest, ref string, amount []float64) ([]model.Ticket, error)
}

type Checkins interface {
	GetFareConditions(ticketNo string, flightId int) (string, error)
	GetFreeSeatForFlight(flightId int, fareConditions string) (string, error)
	GetNextBoardingNo() (int, error)
	CreateBoardingPass(boardingNo, flightId int, seatNo, ticketNo string) (model.BoardingPass, error)
}

type Repository struct {
	Cities
	Airports
	Flights
	Bookings
	Checkins
}

func NewRepository(db *sqlx.DB) *Repository {
	return &Repository{
		Cities:   NewCitiesPostgres(db),
		Airports: NewAirportsPostgres(db),
		Flights:  NewFlightsPostgres(db),
		Bookings: NewBookingsPostgres(db),
		Checkins: NewCheckinsPostgres(db),
	}
}

package model

import (
	"github.com/gin-gonic/gin"
	"github.com/lib/pq"
	"time"
)

type Response struct {
	Status  int    `json:"status"`
	Message string `json:"message"`
	Payload any    `json:"payload"`
}

type Airport struct {
	Code      string  `json:"airportCode" db:"airport_code"`
	Name      string  `json:"airportName" db:"airport_name"`
	City      string  `json:"city" db:"city"`
	Latitude  float64 `json:"latitude" db:"latitude"`
	Longitude float64 `json:"longitude" db:"longitude"`
	Timezone  string  `json:"timezone" db:"timezone"`
}

type InboundSchedule struct {
	DaysOfWeek    pq.Int64Array `json:"daysOfWeek" db:"days_of_week"`
	TimeOfArrival string        `json:"timeOfArrival" db:"arrival_time"`
	FlightNo      string        `json:"flightNo" db:"flight_no"`
	Origin        string        `json:"origin" db:"origin"`
}

type OutboundSchedule struct {
	DaysOfWeek      pq.Int64Array `json:"daysOfWeek" db:"days_of_week"`
	TimeOfDeparture string        `json:"timeOfDeparture" db:"departure_time"`
	FlightNo        string        `json:"flightNo" db:"flight_no"`
	Destination     string        `json:"destination" db:"destination"`
}

type FlightPrice struct {
	FlightNo       string  `json:"flightNo" db:"flight_no"`
	DepartureDow   int     `json:"departure" db:"departure_dow"`
	FareConditions string  `json:"fareConditions" db:"fare_conditions"`
	AveragePrice   float64 `json:"averagePrice" db:"average_price"`
}

type FlightData struct {
	FlightNo   string    `json:"flightNo"`
	FlightDate time.Time `json:"flightDate"`
}

type Ticket struct {
	TicketNo       string  `json:"ticketNo" db:"ticket_no"`
	FareConditions string  `json:"fareConditions" db:"fare_conditions"`
	Amount         float64 `json:"amount" db:"amount"`
	FlightId       int     `json:"flightId" db:"flight_id"`
}

type BookingRequest struct {
	PassengerName  string       `json:"passengerName"`
	ContactData    gin.H        `json:"contactData"`
	FareConditions string       `json:"fareConditions"`
	Flights        []FlightData `json:"flights"`
	FlightIds      []int
}

type BookingResponse struct {
	BookRef     string   `json:"bookRef"`
	TotalAmount float64  `json:"totalAmount"`
	Tickets     []Ticket `json:"tickets"`
}

type BoardingPass struct {
	BoardingNo int    `json:"boardingNo" db:"boarding_no" example:"376"`
	SeatNo     string `json:"seatNo" db:"seat_no" example:"11F"`
	TicketNo   string `json:"ticketNo" db:"ticket_no" example:"_086b048c8144"`
	FlightId   int    `json:"flightId" db:"flight_id" example:"7343"`
}

type CheckInRequest struct {
	TicketNo string `json:"ticketNo"`
	FlightId int    `json:"flightId"`
}

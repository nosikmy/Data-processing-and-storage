package repository

import (
	"Task_6/internal/app/model"
	"database/sql"
	"errors"
	"fmt"
	"github.com/google/uuid"
	"github.com/jmoiron/sqlx"
	"strings"
	"time"
)

type BookingsPostgres struct {
	db *sqlx.DB
}

func NewBookingsPostgres(db *sqlx.DB) *BookingsPostgres {
	return &BookingsPostgres{db: db}
}

func (r *BookingsPostgres) GetFlightPrice(flights []model.FlightData, fareConditions string) ([]float64, []int, error) {
	queryPrice := fmt.Sprintf(`SELECT flight_no, date_part('dow', scheduled_departure) AS departure_dow, fare_conditions, MIN(amount) AS average_price
										FROM ticket_flights
												 JOIN flights
													  ON ticket_flights.flight_id = flights.flight_id
										WHERE date_part('dow', scheduled_departure) = date_part('dow', $1::date) AND flight_no = $2 AND fare_conditions = $3
										GROUP BY flight_no, date_part('dow', scheduled_departure), fare_conditions`)

	tx, err := r.db.Beginx()
	if err != nil {
		return nil, nil, err
	}
	defer tx.Rollback()

	flightsPrices := make([]model.FlightPrice, len(flights))
	amount := make([]float64, len(flights))
	for i, flight := range flights {
		err := tx.Get(&flightsPrices[i], queryPrice, flight.FlightDate.Format(time.DateOnly), flight.FlightNo, fareConditions)
		if err != nil {
			if errors.Is(err, sql.ErrNoRows) {
				return nil, nil, fmt.Errorf("flight number %s does not exist %s", flight.FlightNo, flight.FlightDate.Format(time.DateOnly))
			} else {
				return nil, nil, err
			}
		}
		amount[i] = flightsPrices[i].AveragePrice
	}

	queryFlight := `SELECT flight_id FROM flights WHERE flight_no = $1 AND scheduled_departure::DATE = $2::DATE`
	flightIds := make([]int, len(flights))
	if err != nil {
		return nil, nil, err
	}
	fmt.Println(flights)
	for i, flight := range flights {
		err := tx.Get(&flightIds[i], queryFlight, flight.FlightNo, flight.FlightDate.Format(time.DateOnly))
		if err != nil {
			if errors.Is(err, sql.ErrNoRows) {
				queryInsertFlight := fmt.Sprintf(`INSERT INTO flights (flight_no, scheduled_departure, scheduled_arrival, departure_airport, arrival_airport,
                     								status, aircraft_code)
													SELECT 
													    flight_no,
														   make_timestamp(
																   %d::INT, %d::INT, %d::INT,
																   date_part('hour', scheduled_departure)::int,
																   date_part('minutes', scheduled_departure)::int,
																   date_part('seconds', scheduled_departure)::int
														   )::timestamp AS scheduled_departure,
														   make_timestamp(
																   %d::INT, %d::INT, %d::INT,
																   date_part('hour', scheduled_arrival)::int,
																   date_part('minutes', scheduled_arrival)::int,
																   date_part('seconds', scheduled_arrival)::int
														   )::timestamp AS scheduled_arrival,
														   departure_airport,
														   arrival_airport,
														   'Scheduled' AS status,
														   aircraft_code
													FROM flights WHERE flight_no = $1 AND date_part('dow', scheduled_departure::DATE) = date_part('dow', $2::date) LIMIT 1
													RETURNING flight_id;`,
					flight.FlightDate.Year(), flight.FlightDate.Month(), flight.FlightDate.Day(),
					flight.FlightDate.Year(), flight.FlightDate.Month(), flight.FlightDate.Day())
				err := tx.Get(&flightIds[i], queryInsertFlight, flight.FlightNo, flight.FlightDate.Format(time.DateOnly))
				if err != nil {
					return nil, nil, err
				}
			} else {
				return nil, nil, err
			}
		}

	}

	if err = tx.Commit(); err != nil {
		return nil, nil, err
	}
	return amount, flightIds, nil
}

func (r *BookingsPostgres) CreateBooking(amount float64) (string, error) {

	query := `INSERT INTO bookings (book_ref, book_date, total_amount) VALUES ($1, $2, $3)`
	bookRef := "_" + uuid.New().String()[1:6]
	_, err := r.db.Exec(query, bookRef, time.Now(), amount)
	if err != nil {
		return "", err
	}
	//for err != nil {
	//	bookRef = "_" + uuid.New().String()[1:6]
	//	_, err = r.db.Exec(query, bookRef, time.Now(), amount)
	//}
	return bookRef, nil
}

func (r *BookingsPostgres) CreateTickets(info model.BookingRequest, ref string, amount []float64) ([]model.Ticket, error) {
	queryCreateTicket := `INSERT INTO tickets (ticket_no, book_ref, passenger_id, passenger_name, contact_data)
							VALUES ($1, $2, $3, $4, $5)`
	queryCreateTicketFlights := `INSERT INTO ticket_flights (ticket_no, flight_id, fare_conditions, amount)
									VALUES ($1, $2, $3, $4) RETURNING ticket_no, flight_id, fare_conditions, amount`
	ticketsNo := make([]string, 0, len(info.FlightIds))
	passengerIds := make([]string, 0, len(info.FlightIds))
	for range info.FlightIds {
		ticketsNo = append(ticketsNo, "_"+strings.Replace(uuid.New().String(), "-", "", -1)[1:13])
		passengerIds = append(passengerIds, "_"+strings.Replace(uuid.New().String(), "-", "", -1)[1:20])
	}

	tx, err := r.db.Beginx()
	if err != nil {
		return nil, err
	}
	defer tx.Rollback()
	stmtTicket, err := tx.Preparex(queryCreateTicket)
	if err != nil {
		return nil, err
	}
	defer stmtTicket.Close()

	stmtTicketFlights, err := tx.Preparex(queryCreateTicketFlights)
	if err != nil {
		return nil, err
	}
	defer stmtTicketFlights.Close()

	tickets := make([]model.Ticket, 0, len(amount))
	for i, price := range amount {

		_, err = stmtTicket.Exec(ticketsNo[i], ref, passengerIds[i], info.PassengerName, "{}")
		if err != nil {
			return nil, err
		}

		var ticket model.Ticket
		err = stmtTicketFlights.Get(&ticket, ticketsNo[i], info.FlightIds[i], info.FareConditions, price)
		if err != nil {
			return nil, err
		}
		fmt.Println(ticket)
		tickets = append(tickets, ticket)
	}

	if err = tx.Commit(); err != nil {
		return nil, err
	}
	return tickets, nil
}

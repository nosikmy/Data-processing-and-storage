package repository

import (
	"Task_6/internal/app/model"
	"github.com/jmoiron/sqlx"
)

type CheckinsPostgres struct {
	db *sqlx.DB
}

func NewCheckinsPostgres(db *sqlx.DB) *CheckinsPostgres {
	return &CheckinsPostgres{db: db}
}

func (r *CheckinsPostgres) GetFareConditions(ticketNo string, flightId int) (string, error) {
	query := `SELECT fare_conditions FROM ticket_flights WHERE flight_id=$1 AND ticket_no=$2`

	var fareConditions string
	err := r.db.Get(&fareConditions, query, flightId, ticketNo)
	if err != nil {
		return "", err
	}
	return fareConditions, nil
}

func (r *CheckinsPostgres) GetFreeSeatForFlight(flightId int, fareConditions string) (string, error) {
	query := `SELECT s.seat_no FROM seats s
				JOIN flights f ON s.aircraft_code = f.aircraft_code
					WHERE f.flight_id=$1 AND s.fare_conditions=$2
				EXCEPT
				SELECT b.seat_no FROM boarding_passes b
				JOIN seats s
					ON b.seat_no = s.seat_no
				WHERE b.flight_id=$3 AND s.fare_conditions=$4
				LIMIT 1`

	var seatNo string
	err := r.db.Get(&seatNo, query, flightId, fareConditions, flightId, fareConditions)
	if err != nil {
		return "", err
	}
	return seatNo, nil
}

func (r *CheckinsPostgres) GetNextBoardingNo() (int, error) {
	query := `SELECT boarding_no + 1 as id FROM boarding_passes ORDER BY boarding_no DESC LIMIT 1`

	var id int
	err := r.db.Get(&id, query)
	if err != nil {
		return 0, err
	}
	return id, nil
}

func (r *CheckinsPostgres) CreateBoardingPass(boardingNo, flightId int, seatNo, ticketNo string) (model.BoardingPass, error) {
	query := `INSERT INTO boarding_passes (ticket_no, flight_id, boarding_no, seat_no)
				VALUES ($1, $2, $3, $4) RETURNING ticket_no, flight_id, boarding_no, seat_no`

	var boardingPass model.BoardingPass
	err := r.db.Get(&boardingPass, query, ticketNo, flightId, boardingNo, seatNo)
	if err != nil {
		return model.BoardingPass{}, err
	}
	return boardingPass, nil
}

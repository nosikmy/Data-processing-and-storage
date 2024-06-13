package repository

import (
	"Task_6/internal/app/model"
	"fmt"
	"github.com/jmoiron/sqlx"
)

type AirportsPostgres struct {
	db *sqlx.DB
}

func NewAirportsPostgres(db *sqlx.DB) *AirportsPostgres {
	return &AirportsPostgres{db: db}
}

func (r *AirportsPostgres) GetAllAirports(limit int, offset int, lang string) ([]model.Airport, error) {
	query := fmt.Sprintf(`SELECT airport_code,
								   airport_name ->> '%s' AS airport_name,
								   city ->> '%s' AS city,
								   coordinates[0] AS longitude,
       							   coordinates[1] AS latitude,
								   timezone
								FROM airports_data
								LIMIT $1
								OFFSET $2`, lang, lang)
	var airports []model.Airport
	err := r.db.Select(&airports, query, limit, offset)
	if err != nil {
		return nil, err
	}
	return airports, nil
}

func (r *AirportsPostgres) GetAirportsInCity(city string, lang string) ([]model.Airport, error) {
	query := fmt.Sprintf(`SELECT airport_code,
								   airport_name ->> '%s' AS airport_name,
								   city ->> '%s' AS city,
								   coordinates[0] AS longitude,
       							   coordinates[1] AS latitude,
								   timezone
								FROM airports_data
								WHERE city ->> 'ru' = '%s' OR city ->> 'en' = '%s'
								`, lang, lang, city, city)
	var airports []model.Airport
	err := r.db.Select(&airports, query)
	if err != nil {
		return nil, err
	}
	return airports, nil
}

func (r *AirportsPostgres) GetInboundScheduleForAirport(airport string) ([]model.InboundSchedule, error) {
	query := fmt.Sprintf(`SELECT DISTINCT ON(f.flight_no)
									f.flight_no AS flight_no,
									CAST(f.scheduled_arrival:: timestamp::time AS VARCHAR) AS arrival_time,
									r.days_of_week AS days_of_week,
									r.departure_city AS origin
								FROM flights AS f
										 JOIN routes AS r ON f.flight_no = r.flight_no
								WHERE f.arrival_airport = '%s' AND f.status != 'Arrived'`, airport)
	var schedule []model.InboundSchedule
	err := r.db.Select(&schedule, query)
	if err != nil {
		return nil, err
	}
	return schedule, nil
}

func (r *AirportsPostgres) GetOutboundScheduleForAirport(airport string) ([]model.OutboundSchedule, error) {
	query := fmt.Sprintf(`SELECT DISTINCT ON(f.flight_no)
									f.flight_no AS flight_no,
									CAST(f.scheduled_departure:: timestamp::time AS VARCHAR) AS departure_time,
									r.days_of_week AS days_of_week,
									r.arrival_city AS destination
								FROM bookings.flights AS f
										 JOIN bookings.routes AS r ON f.flight_no = r.flight_no
								WHERE f.departure_airport = '%s' AND f.status != 'Arrived'`, airport)
	var schedule []model.OutboundSchedule
	err := r.db.Select(&schedule, query)
	if err != nil {
		return nil, err
	}
	return schedule, nil
}

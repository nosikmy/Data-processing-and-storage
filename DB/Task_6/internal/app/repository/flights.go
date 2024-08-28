package repository

import "github.com/jmoiron/sqlx"

type FlightsPostgres struct {
	db *sqlx.DB
}

func NewFlightsPostgres(db *sqlx.DB) *FlightsPostgres {
	return &FlightsPostgres{db: db}
}

package repository

import (
	"fmt"
	"github.com/jmoiron/sqlx"
)

type CitiesPostgres struct {
	db *sqlx.DB
}

func NewCitiesPostgres(db *sqlx.DB) *CitiesPostgres {
	return &CitiesPostgres{db: db}
}

func (r *CitiesPostgres) GetAllCities(limit int, offset int, lang string) ([]string, error) {
	query := fmt.Sprintf(`SELECT city ->> '%s' AS city
								FROM airports_data
								LIMIT $1
								OFFSET $2`, lang)
	var cities []string
	err := r.db.Select(&cities, query, limit, offset)
	if err != nil {
		return nil, err
	}
	return cities, err
}

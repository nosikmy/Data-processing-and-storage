package service

import "Task_6/internal/app/repository"

type FlightsService struct {
	repo repository.Flights
}

func NewFlightsService(repo repository.Flights) *FlightsService {
	return &FlightsService{repo: repo}
}

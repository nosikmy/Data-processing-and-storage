package service

import (
	"Task_6/internal/app/model"
	"Task_6/internal/app/repository"
	"fmt"
	"strconv"
)

type AirportsService struct {
	repo repository.Airports
}

func NewAirportsService(repo repository.Airports) *AirportsService {
	return &AirportsService{repo: repo}
}

func (s *AirportsService) GetAllAirports(limit string, page string, lang string) ([]model.Airport, error) {
	limitInt, err := strconv.Atoi(limit)
	if err != nil {
		return nil, err
	}
	pageInt, err := strconv.Atoi(page)
	if err != nil {
		return nil, err
	}
	airports, err := s.repo.GetAllAirports(limitInt, pageInt*limitInt, lang)
	if err != nil {
		return nil, err
	}
	return airports, nil
}

func (s *AirportsService) GetAirportsInCity(city string, lang string) ([]model.Airport, error) {
	airports, err := s.repo.GetAirportsInCity(city, lang)
	if err != nil {
		return nil, err
	}
	return airports, nil
}

func (s *AirportsService) GetScheduleForAirport(scheduleType string, airport string) (any, error) {
	if scheduleType == "inbound" {
		schedule, err := s.repo.GetInboundScheduleForAirport(airport)
		if err != nil {
			return nil, err
		}
		return schedule, nil
	} else if scheduleType == "outbound" {
		schedule, err := s.repo.GetOutboundScheduleForAirport(airport)
		if err != nil {
			return nil, err
		}
		return schedule, nil
	}
	return nil, fmt.Errorf("bad type of schedule")
}

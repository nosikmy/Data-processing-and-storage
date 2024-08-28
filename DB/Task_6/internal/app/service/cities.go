package service

import (
	"Task_6/internal/app/repository"
	"strconv"
)

type CitiesService struct {
	repo repository.Cities
}

func NewCitiesService(repo repository.Cities) *CitiesService {
	return &CitiesService{repo: repo}
}

func (s *CitiesService) GetAllCities(limit string, page string, lang string) ([]string, error) {
	limitInt, err := strconv.Atoi(limit)
	if err != nil {
		return nil, err
	}
	pageInt, err := strconv.Atoi(page)
	if err != nil {
		return nil, err
	}
	cities, err := s.repo.GetAllCities(limitInt, pageInt*limitInt, lang)
	if err != nil {
		return nil, err
	}
	return cities, nil
}

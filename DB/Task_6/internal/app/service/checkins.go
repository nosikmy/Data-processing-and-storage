package service

import (
	"Task_6/internal/app/model"
	"Task_6/internal/app/repository"
)

type CheckinsService struct {
	repo repository.Checkins
}

func NewCheckinsService(repo repository.Checkins) *CheckinsService {
	return &CheckinsService{repo: repo}
}

func (s *CheckinsService) CheckIn(ticketNo string, flightId int) (model.BoardingPass, error) {
	fareConditions, err := s.repo.GetFareConditions(ticketNo, flightId)
	if err != nil {
		return model.BoardingPass{}, err
	}

	seatNo, err := s.repo.GetFreeSeatForFlight(flightId, fareConditions)
	boardingNo, err := s.repo.GetNextBoardingNo()
	if err != nil {
		return model.BoardingPass{}, err
	}
	return s.repo.CreateBoardingPass(boardingNo, flightId, seatNo, ticketNo)
}

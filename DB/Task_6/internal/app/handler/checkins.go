package handler

import (
	"Task_6/internal/app/model"
	"github.com/gin-gonic/gin"
	"net/http"
)

func (h *Handler) CreateCheckIn(c *gin.Context) {
	var request model.CheckInRequest

	if err := c.ShouldBindJSON(&request); err != nil {
		NewErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	boardingPass, err := h.services.CheckIn(request.TicketNo, request.FlightId)
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, model.Response{
		Status:  http.StatusOK,
		Message: "ok",
		Payload: boardingPass,
	})
}

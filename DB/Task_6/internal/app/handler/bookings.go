package handler

import (
	"Task_6/internal/app/model"
	"github.com/gin-gonic/gin"
	"net/http"
)

func (h *Handler) CreateBooking(c *gin.Context) {
	var request model.BookingRequest

	if err := c.ShouldBindJSON(&request); err != nil {
		NewErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}
	amount, err := h.services.CreateBooking(request)
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}
	c.JSON(http.StatusOK, model.Response{
		Status:  http.StatusOK,
		Message: "Ok",
		Payload: amount,
	})
}

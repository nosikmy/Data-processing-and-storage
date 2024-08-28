package handler

import (
	"Task_6/internal/app/model"
	"github.com/gin-gonic/gin"
	"net/http"
)

func (h *Handler) GetAllAirports(c *gin.Context) {
	limit := c.Query("limit")
	if limit == "" {
		limit = "10"
	}
	page := c.Query("page")
	if page == "" {
		page = "0"
	}
	lang := c.Param("lang")
	airports, err := h.services.GetAllAirports(limit, page, lang)
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}
	c.JSON(http.StatusOK, model.Response{
		Status:  http.StatusOK,
		Message: "Ok",
		Payload: airports,
	})
}

func (h *Handler) GetAirportsInCity(c *gin.Context) {
	city := c.Param("city")
	lang := c.Param("lang")
	airports, err := h.services.GetAirportsInCity(city, lang)
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}
	c.JSON(http.StatusOK, model.Response{
		Status:  http.StatusOK,
		Message: "Ok",
		Payload: airports,
	})
}

func (h *Handler) GetScheduleForAirport(c *gin.Context) {
	airport := c.Param("airport")
	scheduleType := c.Query("type")

	schedule, err := h.services.GetScheduleForAirport(scheduleType, airport)
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}
	c.JSON(http.StatusOK, model.Response{
		Status:  http.StatusOK,
		Message: "Ok",
		Payload: schedule,
	})
}

package handler

import (
	"Task_6/internal/app/model"
	"Task_6/internal/app/service"
	"github.com/gin-gonic/gin"
	"github.com/sirupsen/logrus"
)

type Handler struct {
	services *service.Service
}

func NewHandler(services *service.Service) *Handler {
	return &Handler{services: services}
}

func (h *Handler) InitRoutes() *gin.Engine {
	router := gin.New()

	cities := router.Group("/cities")
	{
		cities.GET("/:lang", h.GetAllCities)
	}
	airports := router.Group("/airports")
	{
		airports.GET("/:lang", h.GetAllAirports)
		airports.GET("/city/:city/:lang", h.GetAirportsInCity)
		airports.GET("/schedule/:airport", h.GetScheduleForAirport)

	}
	router.POST("/booking", h.CreateBooking)
	router.POST("/check-in", h.CreateCheckIn)
	return router
}

func NewErrorResponse(c *gin.Context, statusCode int, message string) {
	logrus.Error("method: ", c.Request.Method, ", url: ", c.Request.URL, ", statusCode: ", statusCode, ", msg: ", message)

	c.AbortWithStatusJSON(statusCode, model.Response{
		Status:  statusCode,
		Message: message,
		Payload: nil,
	})
}

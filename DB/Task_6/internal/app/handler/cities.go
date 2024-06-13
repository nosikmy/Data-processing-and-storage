package handler

import (
	"Task_6/internal/app/model"
	"github.com/gin-gonic/gin"
	"net/http"
)

func (h *Handler) GetAllCities(c *gin.Context) {
	limit := c.Query("limit")
	if limit == "" {
		limit = "10"
	}
	page := c.Query("page")
	if page == "" {
		page = "0"
	}
	lang := c.Param("lang")

	cities, err := h.services.GetAllCities(limit, page, lang)
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}
	c.JSON(http.StatusOK, model.Response{
		Status:  http.StatusOK,
		Message: "Ok",
		Payload: cities,
	})
}

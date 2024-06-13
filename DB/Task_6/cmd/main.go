package main

import (
	"Task_6/internal/app/handler"
	"Task_6/internal/app/repository"
	"Task_6/internal/app/server"
	"Task_6/internal/app/service"
	"context"
	"errors"
	"github.com/joho/godotenv"
	_ "github.com/lib/pq"
	"github.com/sirupsen/logrus"
	"net/http"
	"os"
	"os/signal"
	"syscall"
)

func main() {
	logrus.SetFormatter(new(logrus.JSONFormatter))

	if err := godotenv.Load(); err != nil {
		logrus.Fatalf("Error loading .env file: %s", err.Error())
	}

	db, err := repository.NewPostgresDB(repository.ConnectionData{
		Host:     os.Getenv("DB_HOST"),
		Port:     os.Getenv("DB_PORT"),
		Username: os.Getenv("DB_USERNAME"),
		Password: os.Getenv("DB_PASSWORD"),
		Name:     os.Getenv("DB_NAME"),
		SSLMode:  os.Getenv("DB_SSLMODE"),
	})
	if err != nil {
		logrus.Fatalf("Can't connect to db: %s", err.Error())
	}

	repos := repository.NewRepository(db)
	services := service.NewService(repos)
	handlers := handler.NewHandler(services)

	srv := new(server.Server)
	bindAddr := os.Getenv("BIND_ADDR")
	go func() {
		if err = srv.Run(bindAddr, handlers.InitRoutes()); !errors.Is(err, http.ErrServerClosed) {
			logrus.Fatalf("Error while running server: %s", err.Error())
		}
		logrus.Info("Server is shutting down")
	}()
	logrus.Infof("Server started on port %s", bindAddr)

	quitSignal := make(chan os.Signal)
	signal.Notify(quitSignal, syscall.SIGINT, syscall.SIGTERM)
	<-quitSignal

	if err = srv.Shutdown(context.Background()); err != nil {
		logrus.Errorf("Can't terminate server: %s", err.Error())
	}
	if err = db.Close(); err != nil {
		logrus.Errorf("Can't close DB: %s", err.Error())
	}
}

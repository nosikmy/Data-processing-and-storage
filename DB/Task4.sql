SELECT flight_no, TO_CHAR(scheduled_departure, 'Day') AS departure, fare_conditions, ROUND(AVG(amount), 2) AS AveragePrice
FROM ticket_flights
    JOIN flights
        ON ticket_flights.flight_id = flights.flight_id
GROUP BY flight_no, TO_CHAR(scheduled_departure, 'Day'), fare_conditions
ORDER BY flight_no, departure;
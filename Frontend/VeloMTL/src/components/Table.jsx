import "../styles/TripTable.css"
import api from "../api/api"
import { useState, useEffect } from "react";

function calculateDuration(startTime, endTime) {
    const date1 = new Date(startTime);
    const date2 = new Date(endTime);
    const diff = date2 - date1;
    const diffInMinutes = diff/(1000 * 60);   
    return Math.floor(diffInMinutes);
}


export default function TripTable( {search, startDateFilter, endDateFilter, bikeFilter} ) {
    const [trips, setTrips] = useState([]);
    const [filterTrips, setFilterTrips] = useState([]);
    const [selectedTrip, setSelectedTrip] = useState(-1);

    const handleTripClick = (index) => {
        setSelectedTrip(selectedTrip === index ? null : index);
        console.log(selectedTrip);
    }

    useEffect(() => {
        const loadTrips = async () => {
            try {
                const res = await api.get("/api/history/fetch");
                const sortedTrips = [...res.data].sort((a, b) => new Date(a.endTime) - new Date(b.endTime));
                console.log(sortedTrips);
                setTrips(sortedTrips);
                setFilterTrips(sortedTrips);
            } catch (err) {
                console.error(err);
            }
        };
        loadTrips();

    },[])
        
    useEffect(() => {
        if (!search && !startDateFilter && !endDateFilter && bikeFilter != "") {
            setFilterTrips(trips);
        } else {
            const filtered = trips.filter((trip) => {
                const tripIdMatch = search != "" ? trip.tripId.includes(search) : true;
                const startDateMatch = startDateFilter != "" ? new Date(startDateFilter) <= new Date(trip.startTime) : true;
                const endDateMatch = endDateFilter != "" ? new Date(endDateFilter) >= new Date(trip.endTime) : true;
                console.log(startDateMatch);
                const bikeTypeMatch = bikeFilter != "" ? trip.bikeType == bikeFilter : true;
                return tripIdMatch && startDateMatch && endDateMatch && bikeTypeMatch;
            });
            setFilterTrips(filtered);
        }
    }, [trips, search, startDateFilter, endDateFilter, bikeFilter]);
        

    return (
        <div className="PageSpace">
            <table>
                <tr>
                    <th>TripID</th>
                    <th>Username</th>
                    <th>BikeID</th>
                    <th>Reservation Date</th>
                    <th>Start Station</th>
                    <th>End Station</th>
                    <th>Bike Type</th>
                    <th>Cost</th>
                </tr>
                {trips.length === 0 ? 
                <tr>No User Data Found</tr> : 
                filterTrips.length === 0 ? 
                <tr>Search Term and Filters don't match any trips :( try again!</tr> :
                filterTrips.map((trip, index) => (
                    <>
                    <tr className="tripData" key={trip.tripId} onClick={() => handleTripClick(index)}>
                        <td>{trip.tripId}</td>
                        <td>{trip.riderId}</td>
                        <td>{trip.bikeId}</td>
                        <td>{trip.reserveTime}</td>
                        <td>{trip.originStation}</td>
                        <td>{trip.arrivalStation}</td>
                        <td>{trip.bikeType}</td>
                        <td>{trip.cost}</td>
                    </tr>
                    {index === selectedTrip &&
                        <tr>
                            <th>Start Time: {trip.startTime}</th>
                            <th>End Time: {trip.endTime}</th>
                            <th>Duration: {calculateDuration(trip.startTime, trip.endTime)}</th>
                            <th></th>
                        </tr>
                        }
                    </>
                ))}
            </table>
        </div>
    );
}
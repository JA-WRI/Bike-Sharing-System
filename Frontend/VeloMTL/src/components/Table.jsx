import "../styles/TripTable.css"
import api from "../api/api"
import { useState, useEffect } from "react";



export default function TripTable() {
    const [trips, setTrips] = useState([]);

    useEffect(() => {
        const loadTrips = async () => {
            try {
                const res = await api.get("/api/history/fetch");
                setTrips(res.data);
                console.log(res.data);
                console.log(typeof res.data);
            } catch (err) {
                console.error(err);
            }
        };
        loadTrips();
    },[])

    return (
        <div className="PageSpace">
            <table>
                <tr>
                    <th>TripID</th>
                    <th>Username</th>
                    <th>Reservation Date</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Bike Type</th>
                    <th>Cost</th>
                </tr>
                {trips.length === 0 ? 
                <tr>No User Data Found</tr> : 
                trips.map((trip) => (
                    <tr key={trip.tripId}>
                        <td>{trip.tripId}</td>
                        <td>{trip.riderId}</td>
                        <td>-</td>
                        <td>{trip.startTime}</td>
                        <td>{trip.endTime}</td>
                        <td>{trip.bikeType}</td>
                        <td>-</td>
                    </tr>
                ))}
            </table>
        </div>
    );
}
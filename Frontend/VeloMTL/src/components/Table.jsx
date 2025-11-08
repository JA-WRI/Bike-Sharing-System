import "../styles/TripTable.css"
import api from "../api/api"
import { useState, useEffect } from "react";

const ITEMS_PER_PAGE = 10;

function calculateDuration(startTime, endTime) {
    if (startTime == null) {
        return 0;
    }
    var date1 = new Date(startTime);
    var date2;
    if (endTime == null) {
        date2 = new Date()
    } else {
        date2 = new Date(endTime);
    }
    console.log(date2);
    const diff = date2 - date1;
    const diffInMinutes = diff/(1000 * 60);   
    console.log(diffInMinutes);
    return Math.floor(diffInMinutes);
}


export default function TripTable( {search, startDateFilter, endDateFilter, bikeFilter} ) {
    const [trips, setTrips] = useState([]);
    const [filterTrips, setFilterTrips] = useState([]);
    const [selectedTrip, setSelectedTrip] = useState(-1);
    const [currentPage, setCurrentPage] = useState(1);

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
        
    // Search and filter function
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

    // Reset to page 1 when filters change
    useEffect(() => {
        setCurrentPage(1);
    }, [search, startDateFilter, endDateFilter, bikeFilter]);

    // Calculate pagination
    const totalPages = Math.ceil(filterTrips.length / ITEMS_PER_PAGE);
    const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
    const endIndex = startIndex + ITEMS_PER_PAGE;
    const currentTrips = filterTrips.slice(startIndex, endIndex);

    // Adjust selectedTrip index to match the original filterTrips array
    const handleTripClick = (pageIndex) => {
        const actualIndex = startIndex + pageIndex;
        setSelectedTrip(selectedTrip === actualIndex ? null : actualIndex);
    };

    const handleNextPage = () => {
        if (currentPage < totalPages) {
            setCurrentPage(currentPage + 1);
            setSelectedTrip(null); // Reset selection when changing pages
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }
    };

    const handlePreviousPage = () => {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
            setSelectedTrip(null); // Reset selection when changing pages
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }
    };

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
                currentTrips.map((trip, pageIndex) => {
                    const actualIndex = startIndex + pageIndex;
                    return (
                    <>
                    {/* Main Details */}
                    <tr className="tripData" key={trip.tripId} onClick={() => handleTripClick(pageIndex)}>
                        <td>{trip.tripId}</td>
                        <td>{trip.riderId}</td>
                        <td>{trip.bikeId}</td>
                        <td>{trip.reserveTime}</td>
                        <td>{trip.originStation}</td>
                        <td>{trip.arrivalStation}</td>
                        <td>{trip.bikeType}</td>
                        <td>{trip.cost}</td>
                    </tr>
                    {/* Drop down section for additional details */}
                    {actualIndex === selectedTrip &&
                        <tr>
                            <th>Start Time: {trip.startTime != null ? new Date(trip.startTime).toLocaleString() : null}</th>
                            <th>End Time: {trip.endTime != null ? new Date(trip.endTime).toLocaleString() : null}</th>
                            <th>Duration: {calculateDuration(trip.startTime, trip.endTime)}</th>
                            <th></th>
                        </tr>
                        }
                    </>
                    );
                })}
            </table>
            
            {/* Pagination Controls */}
            {filterTrips.length > ITEMS_PER_PAGE && (
                <div style={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    gap: "16px",
                    marginTop: "32px",
                    padding: "20px"
                }}>
                    <button
                        onClick={handlePreviousPage}
                        disabled={currentPage === 1}
                        style={{
                            padding: "10px 20px",
                            borderRadius: "6px",
                            border: "1px solid #ddd",
                            backgroundColor: currentPage === 1 ? "#f5f5f5" : "white",
                            color: currentPage === 1 ? "#999" : "#333",
                            cursor: currentPage === 1 ? "not-allowed" : "pointer",
                            fontSize: "14px",
                            fontWeight: "500",
                            display: "flex",
                            alignItems: "center",
                            gap: "8px"
                        }}
                    >
                        ← Previous
                    </button>
                    
                    <span style={{
                        fontSize: "14px",
                        color: "#666",
                        fontWeight: "500"
                    }}>
                        Page {currentPage} of {totalPages}
                    </span>
                    
                    <button
                        onClick={handleNextPage}
                        disabled={currentPage === totalPages}
                        style={{
                            padding: "10px 20px",
                            borderRadius: "6px",
                            border: "1px solid #ddd",
                            backgroundColor: currentPage === totalPages ? "#f5f5f5" : "white",
                            color: currentPage === totalPages ? "#999" : "#333",
                            cursor: currentPage === totalPages ? "not-allowed" : "pointer",
                            fontSize: "14px",
                            fontWeight: "500",
                            display: "flex",
                            alignItems: "center",
                            gap: "8px"
                        }}
                    >
                        Next →
                    </button>
                </div>
            )}
        </div>
    );
}
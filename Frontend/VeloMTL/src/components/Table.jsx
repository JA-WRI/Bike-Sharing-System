import "../styles/TripTable.css"
import api from "../api/api"
import { useState, useEffect, useContext } from "react";
import { AuthContext } from "../context/AuthContext";

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
    const { activeRole } = useContext(AuthContext);

    useEffect(() => {
        const loadTrips = async () => {
            try {
                // Pass activeRole as query parameter if it exists
                const params = activeRole ? { activeRole } : {};
                const res = await api.get("/api/history/fetch", { params });
                const sortedTrips = [...res.data].sort((a, b) => new Date(a.endTime) - new Date(b.endTime));
                console.log(sortedTrips);
                setTrips(sortedTrips);
                setFilterTrips(sortedTrips);
            } catch (err) {
                console.error(err);
            }
        };
        loadTrips();

    }, [activeRole])
        
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
                <tr className="empty-state"><td colSpan="8">No User Data Found</td></tr> : 
                filterTrips.length === 0 ? 
                <tr className="empty-state"><td colSpan="8">Search Term and Filters don't match any trips 😔 Try again!</td></tr> :
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
                        <tr className="expanded-row">
                            <td colSpan="8" style={{ display: "grid", gridTemplateColumns: "repeat(4, 1fr)", gap: "1rem" }}>
                                <div><strong>Start Time:</strong><br />{trip.startTime != null ? new Date(trip.startTime).toLocaleString() : "N/A"}</div>
                                <div><strong>End Time:</strong><br />{trip.endTime != null ? new Date(trip.endTime).toLocaleString() : "N/A"}</div>
                                <div><strong>Duration:</strong><br />{calculateDuration(trip.startTime, trip.endTime)} minutes</div>
                                <div></div>
                            </td>
                        </tr>
                        }
                    </>
                    );
                })}
            </table>
            
            {/* Pagination Controls */}
            {filterTrips.length > ITEMS_PER_PAGE && (
                <div className="pagination-container">
                    <button
                        className="pagination-button"
                        onClick={handlePreviousPage}
                        disabled={currentPage === 1}
                    >
                        ← Previous
                    </button>
                    
                    <span className="pagination-info">
                        Page {currentPage} of {totalPages}
                    </span>
                    
                    <button
                        className="pagination-button"
                        onClick={handleNextPage}
                        disabled={currentPage === totalPages}
                    >
                        Next →
                    </button>
                </div>
            )}
        </div>
    );
}
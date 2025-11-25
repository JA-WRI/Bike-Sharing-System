import TripTable from "../components/Table";
import { useState } from "react";
import "../styles/History.css";

export default function History() {
    const [searchTerm, setSearchTerm] = useState("");
    const [startDateFilter, setStartDateFilter] = useState("");
    const [endDateFilter, setEndDateFilter] = useState("");
    const [bikeFilter, setBikeFilter] = useState("");
    
    const handleInput = (e) => {
        setSearchTerm(e.target.value);
    }

    const handleStartDateInput = (e) => {
        setStartDateFilter(e.target.value);
    }

    const handleEndDateInput = (e) => {
        setEndDateFilter(e.target.value);
    }

    const handleBikeFilterInput = (e) => {
        setBikeFilter(e.target.value);
    }
    
    return (
        <div className="history-page">
            <div className="history-container">
                <div className="history-header">
                    <h1>Trip History</h1>
                    <p>View and filter your past trips</p>
                </div>

                <div className="filters-section">
                    <h2 className="filters-title">Filters</h2>
                    <div className="filters-grid">
                        <div className="filter-group">
                            <label className="filter-label">Search Trip ID</label>
                            <input 
                                className="filter-input"
                                id="Trip Search" 
                                type="text" 
                                placeholder="Enter trip ID..." 
                                value={searchTerm}
                                onChange={handleInput}
                            />
                        </div>
                        
                        <div className="filter-group">
                            <label className="filter-label">Start Date</label>
                            <input 
                                className="filter-input"
                                id="startDateFilter" 
                                type="date" 
                                value={startDateFilter}
                                onChange={handleStartDateInput}
                            />
                        </div>
                        
                        <div className="filter-group">
                            <label className="filter-label">End Date</label>
                            <input 
                                className="filter-input"
                                id="endDateFilter" 
                                type="date" 
                                value={endDateFilter}
                                onChange={handleEndDateInput}
                            />
                        </div>
                        
                        <div className="filter-group">
                            <label className="filter-label">Bike Type</label>
                            <select 
                                className="filter-select"
                                id="bikeType" 
                                value={bikeFilter}
                                onChange={handleBikeFilterInput}
                            >
                                <option value="">All Types</option>
                                <option value="regular">Regular</option>
                                <option value="e-Bike">E-Bike</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div className="table-section">
                    <TripTable
                        search={searchTerm}
                        startDateFilter={startDateFilter}
                        endDateFilter={endDateFilter}
                        bikeFilter={bikeFilter}
                    />
                </div>
            </div>
        </div>
    );
}